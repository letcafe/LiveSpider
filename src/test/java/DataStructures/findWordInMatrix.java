package DataStructures;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class findWordInMatrix {

    @Test
    public void main() {
        List<String> dictionary = new ArrayList<>();
        Collections.addAll(dictionary, "this", "two", "fat", "ts", "ther");
        char[][] matrix = {
                {'t', 'h', 'i', 's'},
                {'w', 'a', 't', 's'},
                {'o', 'a', 'h', 'g'},
                {'f', 'g', 'd', 't'},
        };
        findWordInMatrix(dictionary, matrix);
    }

    public void findWordInMatrix(List<String> dict, char[][] matrix) {
        for (String word : dict) {
            // 检查横向的时候满足
            for (int i = 0; i < matrix.length; i++) {
                StringBuilder leftStringBuilder = new StringBuilder();
                StringBuilder rightStringBuilder = new StringBuilder();
                for (int j = 0; j < matrix[i].length; j++) {
                    leftStringBuilder.append(matrix[i][j]);
                    rightStringBuilder.append(matrix[i][matrix[i].length - j - 1]);
                }
                if (leftStringBuilder.toString().contains(word)) {
                    int startIndex = leftStringBuilder.toString().indexOf(word) + 1;
                    int endIndex = startIndex + word.length() - 1;
                    System.out.println(word + " = (" + (i + 1) + "," + startIndex + ") => " + "(" + (i + 1) + "," + endIndex + ")");
                    break;
                } else if (rightStringBuilder.toString().contains(word)) {
                    int startIndex = matrix[0].length - rightStringBuilder.toString().indexOf(word);
                    int endIndex = startIndex - word.length() + 1;
                    System.out.println(word + " = (" + (i + 1) + "," + startIndex + ") => " + "(" + (i + 1) + "," + endIndex + ")");
                    break;
                }
            }
            // 检查纵向的时候满足
            for (int i = 0; i < matrix[0].length; i++) {
                StringBuilder upStringBuilder = new StringBuilder();
                StringBuilder downStringBuilder = new StringBuilder();
                for (int j = 0; j < matrix.length; j++) {
                    upStringBuilder.append(matrix[j][i]);
                    downStringBuilder.append(matrix[matrix.length - j - 1][i]);
                }
                if (upStringBuilder.toString().contains(word)) {
                    int startIndex = upStringBuilder.toString().indexOf(word) + 1;
                    int endIndex = startIndex + word.length() - 1;
                    System.out.println(word + " = (" + startIndex + "," + (i + 1) + ") => " + "(" + endIndex + "," + (i + 1) + ")");
                    break;
                } else if (downStringBuilder.toString().contains(word)) {
                    int startIndex = matrix.length - downStringBuilder.toString().indexOf(word);
                    int endIndex = startIndex - word.length() + 1;
                    System.out.println(word + " = (" + startIndex + "," + (i + 1) + ") => " + "(" + endIndex + "," + (i + 1) + ")");
                    break;
                }
            }
            // 检查斜方向是否满足
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            StringBuilder line3 = new StringBuilder();
            StringBuilder line4 = new StringBuilder();
            for (int i = 0; i < matrix.length; i++) {
                line1.append(matrix[i][i]);
                line2.append(matrix[matrix.length - 1 - i][matrix.length - 1 - i]);
                line3.append(matrix[i][matrix.length - 1 - i]);
                line4.append(matrix[matrix.length - 1 - i][i]);
            }
            int startIndexI = -1;
            int startIndexJ = -1;
            int endIndexI = -1;
            int endIndexJ = -1;
            if (line1.toString().contains(word)) {
                startIndexI = line1.indexOf(word) + 1;
                startIndexJ = startIndexI;
                endIndexI = startIndexI + word.length() - 1;
                endIndexJ = endIndexI;
            } else if (line2.toString().contains(word)) {
                startIndexI = line2.length() - line2.indexOf(word);
                startIndexJ = startIndexI;
                endIndexI = startIndexI - word.length() + 1;
                endIndexJ = endIndexI;
            } else if (line3.toString().contains(word)) {
                startIndexI = line3.indexOf(word) + 1;
                startIndexJ = line3.length() - line3.indexOf(word);
                endIndexI = startIndexI + word.length() - 1;
                endIndexJ = startIndexJ - word.length() + 1;
            } else if (line4.toString().contains(word)) {
                startIndexI = line4.length() - line4.indexOf(word);
                startIndexJ = line4.indexOf(word) + 1;
                endIndexI = startIndexI - word.length() + 1;
                endIndexJ = startIndexJ + word.length() - 1;
            }
            if (startIndexI != -1) {
                System.out.println(word + " = (" + startIndexI + "," + startIndexJ + ") => " + "(" + endIndexI + "," + endIndexJ + ")");
            }
        }
    }
}
