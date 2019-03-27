package design.factory;


import java.util.Random;

public interface TransFactory {
    Transport newInstance();
}

class CarFactory implements TransFactory {
    @Override
    public Car newInstance() {
        Random rand = new Random();
        if (rand.nextBoolean()) {
            return new Banz();
        } else {
            return new Jeep();
        }
    }
}

class BikeFactory implements TransFactory {
    @Override
    public Bike newInstance() {
        Random rand = new Random();
        if (rand.nextBoolean()) {
            return new Mobike();
        } else {
            return new Ofo();
        }
    }
}