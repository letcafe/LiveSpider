package design;

public class SingletonHungry {
    // 饿汉式
    private static SingletonHungry instance = new SingletonHungry();

    private SingletonHungry() {

    }

    public static SingletonHungry getInstance() {
        return instance;
    }
}
