package com.letcafe.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ACM {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for (int i = 0; i < T; i++) {
            int peopleNum = scanner.nextInt();
            int avgScore = scanner.nextInt();
            List<Integer> scoreList = new ArrayList<>();
            int totalScore = 0;
            for (int j = 0; j < peopleNum; j++) {
                int number = scanner.nextInt();
                scoreList.add(number);
                totalScore += number;
            }
            scoreList.sort((o1, o2) -> (o1 - o2));
            if (totalScore >= peopleNum * avgScore) {
                System.out.println(0);
                return;
            }
            for (int k = 0; k < peopleNum; k ++) {
                int _100Value = 100 - scoreList.get(k);
                totalScore += _100Value;
                if (totalScore > peopleNum * avgScore) {
                    System.out.println(k + 1);
                    break;
                }
            }
        }

    }
}
