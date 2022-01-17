package hanze.nl.bussimulator;

import hanze.nl.bussimulator.Bedrijven;
import hanze.nl.bussimulator.Bus;
import hanze.nl.bussimulator.Lijnen;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class BussimulatorSpec {
    public static void main(String[] args) throws InterruptedException {
        Bus bus = new Bus(Lijnen.LIJN1, Bedrijven.ARRIVA, 1);
        bus.start();
        assertFalse(bus.halteBereikt());
        while(!bus.halteBereikt()){
            bus.move();
        }
        assertTrue(bus.halteBereikt());
    }

    public void halteBereiktWanneerEindeBereiktReturnTrue() {
        Bus bus = new Bus(Lijnen.LIJN1, Bedrijven.ARRIVA, 1);
        bus.start();
        assertFalse(bus.halteBereikt());
        while(!bus.halteBereikt()){
            bus.move();
        }
        assertTrue(bus.halteBereikt());
    }
}
