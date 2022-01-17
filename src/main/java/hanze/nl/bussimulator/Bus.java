package hanze.nl.bussimulator;

import com.thoughtworks.xstream.XStream;
import hanze.nl.bussimulator.Halte.Positie;

public class Bus {

    private Bedrijven bedrijf;
    private Lijnen lijn;
    private int halteNummer;
    private int totVolgendeHalte;
    private int richting;
    private boolean bijHalte;
    private String busID;
    private Logger logger;

    Bus(Lijnen lijn, Bedrijven bedrijf, int richting) {
        this.lijn = lijn;
        this.bedrijf = bedrijf;
        this.richting = richting;
        this.halteNummer = -1;
        this.totVolgendeHalte = 0;
        this.bijHalte = false;
        this.busID = "Niet gestart";
        this.logger = new Logger();
    }

    public void setbusID(int starttijd) {
        this.busID = starttijd + lijn.name() + richting;
    }

    public void naarVolgendeHalte() {
        Positie volgendeHalte = lijn.getHalte(halteNummer + richting).getPositie();
        totVolgendeHalte = lijn.getHalte(halteNummer).afstand(volgendeHalte);
    }

    public void bijHalteAangekomen() {
        halteNummer += richting;
        bijHalte = true;
        if (eindHalteBereikt()) {
            logger.logEindpuntBereikt(lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer));
        } else {
            logger.logHalteBereikt(lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer));
            naarVolgendeHalte();
        }
    }

    public boolean eindHalteBereikt() {
        return (halteNummer >= lijn.getLengte() - 1) || (halteNummer == 0);
    }

    //bus starten en loggen
    public void start() {
        halteNummer = (richting == 1) ? 0 : lijn.getLengte() - 1;
        System.out.printf("Bus %s is vertrokken van halte %s in richting %d.%n",
                lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer));
        naarVolgendeHalte();
    }

    public boolean move() {
        boolean eindpuntBereikt = false;
        bijHalte = false;
        if (halteNummer == -1) {
            start();
        } else {
            totVolgendeHalte--;
            if (totVolgendeHalte == 0) {
                eindpuntBereikt = eindHalteBereikt();
                bijHalteAangekomen();
            }
        }
        return eindpuntBereikt;
    }

    public void sendETAs(int nu) {
        int i = 0;
        Bericht bericht = new Bericht(lijn.name(), bedrijf.name(), busID, nu);
        if (bijHalte) {
            ETA eta = new ETA(lijn.getHalte(halteNummer).name(), lijn.getRichting(halteNummer), 0);
            bericht.ETAs.add(eta);
        }
        Positie eerstVolgende = lijn.getHalte(halteNummer + richting).getPositie();
        int tijdNaarHalte = totVolgendeHalte + nu;
        for (i = halteNummer + richting; !(i >= lijn.getLengte()) && !(i < 0); i = i + richting) {
            tijdNaarHalte += lijn.getHalte(i).afstand(eerstVolgende);
            ETA eta = new ETA(lijn.getHalte(i).name(), lijn.getRichting(i), tijdNaarHalte);
            bericht.ETAs.add(eta);
            eerstVolgende = lijn.getHalte(i).getPositie();
        }
        bericht.eindpunt = lijn.getHalte(i - richting).name();
        sendBericht(bericht);
    }

    public void sendLastETA(int nu) {
        Bericht bericht = new Bericht(lijn.name(), bedrijf.name(), busID, nu);
        String eindpunt = lijn.getHalte(halteNummer).name();
        ETA eta = new ETA(eindpunt, lijn.getRichting(halteNummer), 0);
        bericht.ETAs.add(eta);
        bericht.eindpunt = eindpunt;
        sendBericht(bericht);
    }

    public void sendBericht(Bericht bericht) {
        XStream xstream = new XStream();
        xstream.alias("Bericht", Bericht.class);
        xstream.alias("ETA", ETA.class);
        String xml = xstream.toXML(bericht);
        Producer producer = new Producer();
        producer.sendBericht(xml);
    }
}
