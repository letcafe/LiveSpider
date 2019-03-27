package design.factory;

public abstract class Car implements Transport {
}

class Banz extends Car {
    @Override
    public void run() {
        System.out.println("Car:Banz run");
    }
}

class Jeep extends Car {
    @Override
    public void run() {
        System.out.println("Car:Jeep run");
    }
}
