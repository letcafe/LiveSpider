package bytedance;


public class Test {
    
    public void quickSort(int[] array, int startIndex, int endIndex) {
        int length = array.length;
        int left = startIndex;
        int right = endIndex;
        if (left < right) {
            int partitionIndex = partition(array, left, right);
            quickSort(array, left, partitionIndex -  1);
            quickSort(array, partitionIndex + 1, right);
        }
    }

    private int partition(int[] array, int left, int right) {
        int pivot = left;
        int index = pivot + 1;
        for (int i = 0; i <= right; i ++) {
            if (array[i] < array[pivot]) {
                swap(array, i, index);
                index ++;
            }
        }
        swap(array, pivot, index - 1);
        return index - 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


}
