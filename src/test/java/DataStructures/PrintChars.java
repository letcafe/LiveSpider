package DataStructures;

import org.junit.Test;

public class PrintChars {

    @Test
    public void printChars() {
        printDigit(10086);
    }

    public void printDigit(int num) {
        if (num >= 10) {
            printDigit(num / 10);
        }
        System.out.print(num % 10);
    }
}
