package design.factory;

public abstract class Bike implements Transport {
}

class Mobike extends Bike {
    @Override
    public void run() {
        System.out.println("Bike:Mobike run");
    }
}

class Ofo extends Bike {
    @Override
    public void run() {
        System.out.println("Bike:Ofo run");
    }
}
