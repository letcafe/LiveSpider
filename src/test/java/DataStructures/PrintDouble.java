package DataStructures;

import org.junit.Test;

public class PrintDouble {

    @Test
    public void main() {
        printDouble(-134233.2211f);
    }

    private void printDouble(float val) {
        // 去除符号的影响
        boolean flag = (int) Math.floor(val) >= 0;
        if (! flag) {
            System.out.print("-");
            // 将负数转正
            val *= -1;
        }
        // 得到绝对值
        // 负责打印出整数部分
        printDigit((int) val);
        System.out.print(".");
        // 开始打印小数部分
        double doubleVal = val - (int)(val);
        while (doubleVal - Math.floor(doubleVal) != 0) {
            doubleVal *= 10;
        }
        printDigit((int) doubleVal);
        System.out.println();
    }

    private void printDigit(int v) {
        if (v >= 10) {
            printDigit(v / 10);
        }
        System.out.print(v % 10);
    }
}
