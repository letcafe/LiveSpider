package proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        UserManager manager = new UserMangerImpl();

        // 1.第一层代理——通过动态代理实现，添加事务处理
        InvocationHandler transactionHandler = new TransactionHandler(manager);
        UserManager transactionManager = (UserManager) Proxy.newProxyInstance(manager
                        .getClass().getClassLoader(),
                manager.getClass().getInterfaces(), transactionHandler);

        // 2.第二层代理——通过动态代理实现，添加时间处理
        InvocationHandler timeHandler = new TimeHandler(transactionManager);
        UserManager timeAndTransactionManager = (UserManager) Proxy.newProxyInstance(transactionManager
                        .getClass().getClassLoader(),
                transactionManager.getClass().getInterfaces(), timeHandler);

        timeAndTransactionManager.addUser();
        System.out.println("=====================================");
        timeAndTransactionManager.delUser();
    }
}
