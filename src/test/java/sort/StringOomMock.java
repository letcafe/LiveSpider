package sort;


public class StringOomMock {

    public static void main(String[] args) {
        String base = "base";
        while (true) {
            call();
        }
    }

    private static void call() {
        System.out.println("hello world");
        call();
    }
}
