package io.redis;


public class Main {

    public static void main(String[] args) {
        String s = "a  b  cd e f";
        char[] inputArray = s.toCharArray();
        for (int i = 0; i < inputArray.length; i ++) {
            if (inputArray[i] == ' ') {

            }
        }
        System.out.println(s.replaceAll("\\s+", " "));
    }
}
