package proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class TimeHandler implements InvocationHandler {
    private Object target;

    public TimeHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("【开始时间】 = " + LocalDateTime.now());
        method.invoke(target);
        System.out.println("【结束时间】 = " + LocalDateTime.now());
        return null;
    }
}
