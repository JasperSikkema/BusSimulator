package hanze.nl.bussimulator;

public class BusInfo {
    private final Bedrijven bedrijf;
    private final Lijnen lijn;
    private final int halteNummer;
    private final int totVolgendeHalte;
    private final int richting;
    private final boolean bijHalte;
    private final String busID;

    public BusInfo(Lijnen lijn, Bedrijven bedrijf, int richting, int halteNummer, int totVolgendeHalte, boolean bijHalte, String busID){
        this.lijn=lijn;
        this.bedrijf=bedrijf;
        this.richting=richting;
        this.halteNummer = halteNummer;
        this.totVolgendeHalte = totVolgendeHalte;
        this.bijHalte = bijHalte;
        this.busID = busID;
    }

    public Bedrijven getBedrijf() {
        return bedrijf;
    }

    public Lijnen getLijn() {
        return lijn;
    }

    public int getHalteNummer() {
        return halteNummer;
    }

    public int getTotVolgendeHalte() {
        return totVolgendeHalte;
    }

    public int getRichting() {
        return richting;
    }

    public boolean isBijHalte() {
        return bijHalte;
    }

    public String getBusID() {
        return busID;
    }
}
