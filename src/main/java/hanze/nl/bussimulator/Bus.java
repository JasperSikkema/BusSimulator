package hanze.nl.bussimulator;

import hanze.nl.bussimulator.Halte.Positie;



public class Bus{

	private Bedrijven bedrijf;
	private Lijnen lijn;
	private int halteNummer;
	private int totVolgendeHalte;
	private int richting;
	private boolean bijHalte;
	private String busID;
	private ETAZender zender;
	private Logger logger;

	Bus(Lijnen lijn, Bedrijven bedrijf, int richting){
		this.lijn=lijn;
		this.bedrijf=bedrijf;
		this.richting=richting;
		this.halteNummer = -1;
		this.totVolgendeHalte = 0;
		this.bijHalte = false;
		this.busID = "Niet gestart";
		this.zender = new ETAZender();
		this.logger = new Logger();
	}

	public void setbusID(int starttijd){
		this.busID=starttijd+lijn.name()+richting;
	}
	
	public void naarVolgendeHalte(){
		Positie volgendeHalte = lijn.getHalte(halteNummer+richting).getPositie();
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
	
	public void start() {
		halteNummer = (richting==1) ? 0 : lijn.getLengte()-1;
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

	public void sendETAs(int nu){
		BusInfo busInfo = new BusInfo(lijn, bedrijf, richting, halteNummer, totVolgendeHalte, bijHalte, busID);
		zender.sendETAs(nu, busInfo);
	}

	public void sendLastETA(int nu) {
		BusInfo busInfo = new BusInfo(lijn, bedrijf, richting, halteNummer, totVolgendeHalte, bijHalte, busID);
		zender.sendETAs(nu, busInfo);
	}
}
