// From: http://www.java2novice.com/java-sorting-algorithms/quick-sort/

import java.util.Collection;

public class QuickSort {
    private int array[];
    private int length;

    public void init() {
        System.exit(1);
    }

    public void init2() {
        System.exit(1);
    }

    public void init3() {
        int i = 0;
        i = i++;
        Double d = new Double(10);
        d.longBitsToDouble(i);
    }

    public int init4() {
        int b = -128;
        int y = 10;
        b = b >> 34;
        b = (b << 8 + y);
        return b++;
    }

    public String[] getAsArray(Collection<String> c) {
        return (String[]) c.toArray();
    }

    public void sort(int[] inputArr) {
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }

    public void arrayTest() {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {5, 4, 3, 2, 1};
        String[] c = {"string1", "string2"};
        a.equals(b);
        a.equals(c);
        a.equals(null);
    }

    public void testString() {
        System.out.printf("%i\n", "12");
        String.format("%d", "1");
        double x = -1234;
        if (x == Double.NaN) {
            int a = 1;
        }
        if (x < 0) new IllegalArgumentException("x must be nonnegative");
    }

    public void infiniteLoop() {
        while (true) {
            int a = 1;
        }
    }

    private void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex; // calculate pivot number, I am taking pivot as middle index number int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2]; // Divide into two arrays while (i <= j) { /** * In each iteration, we will identify a number from left side which * is greater then the pivot value, and also we will identify a number * from right side which is less then the pivot value. Once the search * is done, then we exchange both numbers. */ while (array[i] < pivot) { i++; } while (array[j] > pivot) { j--; } if (i <= j) { exchangeNumbers(i, j); //move index to next position on both sides i++; j--; } } // call quickSort() method recursively if (lowerIndex < j) quickSort(lowerIndex, j); if (i < higherIndex) quickSort(i, higherIndex); } private void exchangeNumbers(int i, int j) { int temp = array[i]; array[i] = array[j]; array[j] = temp; } }