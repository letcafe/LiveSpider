package design;

public class SingletonThreadSafeLazy {

    private static SingletonThreadSafeLazy instance;

    private SingletonThreadSafeLazy() {

    }

    public static SingletonThreadSafeLazy getInstance() {
        if (instance == null) {
            synchronized(SingletonThreadSafeLazy.class) {
                if (instance == null) {
                    instance = new SingletonThreadSafeLazy();
                }
            }
        }
        return instance;
    }
}
