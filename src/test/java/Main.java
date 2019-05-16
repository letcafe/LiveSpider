import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Main().solution();
    }

    void solution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][] arr = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                arr[i][j] = scanner.nextInt();
            }
        }
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        getLineList(list1, list2, arr);
        System.out.println(6);
    }

    void getLineList(List<Integer> list1, List<Integer> list2, int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    list1.add(arr[i][j]);
                } else {
                    list2.add(arr[i][j]);
                }
            }
        }
    }

}
