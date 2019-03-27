package proxy.cglib;


public class Client {

    public static void main(String[] args) {
        PrintManager target = new PrintManager();
        // 添加CGLIB代理对象
        PrintInterceptor printInterceptor = new PrintInterceptor();
        // 注入被代理对象，并实现包裹
        PrintManager printManager = (PrintManager) printInterceptor.getProxyInstance(target);
        // 测试方法
        printManager.add();
        System.out.println("=====================================");
        printManager.minus();
    }
}
