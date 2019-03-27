package proxy.jdk;

public class UserMangerImpl implements UserManager {
    @Override
    public void addUser() {
        System.out.println("【添加了一名用户】");
    }

    @Override
    public void delUser() {
        System.out.println("【删除了一名用户】");
    }
}
