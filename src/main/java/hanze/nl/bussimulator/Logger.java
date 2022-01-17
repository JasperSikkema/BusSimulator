package hanze.nl.bussimulator;

public class Logger {
    public void logHalteBereikt(String naam, Halte halte, int richting){
        System.out.printf("Bus %s heeft halte %s, richting %d bereikt.%n",
                naam, halte, richting);
    }

    public void logEindpuntBereikt(String naam, Halte halte, int richting){
        System.out.printf("Bus %s heeft eindpunt (halte %s, richting %d) bereikt.%n",
                naam, halte, richting);
    }
}
