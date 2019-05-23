package bytedance;

public class SingletonDemo {

    private static SingletonDemo singletonDemo;

    private SingletonDemo() {

    }

    public static SingletonDemo getInstance() {
        if (singletonDemo == null) {
            synchronized(SingletonDemo.class) {
                if (singletonDemo == null) {
                    return new SingletonDemo();
                }
                return singletonDemo;
            }
        } else {
            return singletonDemo;
        }
    }
}
