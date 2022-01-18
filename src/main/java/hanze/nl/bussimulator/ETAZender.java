package hanze.nl.bussimulator;

import com.thoughtworks.xstream.XStream;

import java.util.HashMap;

public class ETAZender {

    public ETAZender(){

    }

    public void sendETAs(int nu, BusInfo busInfo){
        int i=0;
        Lijnen lijn = busInfo.getLijn();
        int halteNummer = busInfo.getHalteNummer();
        int richting = busInfo.getRichting();

        Bericht bericht = new Bericht(lijn.name(),busInfo.getBedrijf().name(),busInfo.getBusID(),nu);
        if (busInfo.isBijHalte()) {
            ETA eta = new ETA(lijn.getHalte(halteNummer).name(),lijn.getRichting(halteNummer),0);
            bericht.ETAs.add(eta);
        }
        Halte.Positie eerstVolgende=lijn.getHalte(halteNummer+richting).getPositie();
        int tijdNaarHalte=busInfo.getTotVolgendeHalte()+nu;
        for (i = halteNummer+richting ; !(i>=lijn.getLengte()) && !(i < 0); i=i+richting ){
            tijdNaarHalte+= lijn.getHalte(i).afstand(eerstVolgende);
            ETA eta = new ETA(lijn.getHalte(i).name(), lijn.getRichting(i),tijdNaarHalte);
            bericht.ETAs.add(eta);
            eerstVolgende=lijn.getHalte(i).getPositie();
        }
        bericht.eindpunt=lijn.getHalte(i-richting).name();
        sendBericht(bericht);
    }

    public void sendLastETA(int nu, BusInfo busInfo){
        Lijnen lijn = busInfo.getLijn();
        Bericht bericht = new Bericht(lijn.name(),busInfo.getBedrijf().name(), busInfo.getBusID(), nu);
        String eindpunt = lijn.getHalte(busInfo.getHalteNummer()).name();
        ETA eta = new ETA(eindpunt,lijn.getRichting(busInfo.getHalteNummer()),0);
        bericht.ETAs.add(eta);
        bericht.eindpunt = eindpunt;
        sendBericht(bericht);
    }

    public void sendBericht(Bericht bericht){
        XStream xstream = new XStream();
        xstream.alias("Bericht", Bericht.class);
        xstream.alias("ETA", ETA.class);
        String xml = xstream.toXML(bericht);
        Producer producer = new Producer();
        producer.sendBericht(xml);
    }
}
