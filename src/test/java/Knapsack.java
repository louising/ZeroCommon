import java.util.ArrayList;
import java.util.List;

public class Knapsack {
    static int num = 0;
    
    public static void main(String[] args) {
        //List<List<Integer>> allCombinations = fitItems(new int[] {13,2,3,4,11, 9, 8, 7, 6, 5}, 20);
        //List<List<Integer>> allCombinations = fitItems(new int[] 13, 12, 6, 8, 1, 2, 3, 7}, 13);
        //List<List<Integer>> allCombinations = fitItems(new int[] {1,2,3,4,11, 9, 8, 7, 6}, 20);
        //List<List<Integer>> allCombinations = fitItems(new int[] {13, 12, 6, 8, 1, 2, 3, 7,4}, 13);
        //List<List<Integer>> allCombinations = fitItems(new int[] {5, 3, 2,15, 12, 10, 8, 7}, 20);
        //List<List<Integer>> allCombinations = fitItems(new int[] {19, 11, 9, 8, 7, 4, 3, 2, 1}, 20);
        List<List<Integer>> allCombinations = fitItems(new int[] {2, 3, 5, 20, 10, 9, 15}, 20);  //20, 15, 10, 9, 5, 3, 2     2, 3, 5, 9, 10, 15, 20
        
        
        for (List<Integer> combination : allCombinations) {
            System.out.println(combination);
        }
    }

    //List<List<Integer>> allCombinations = fitItems(new int[] {20, 15, 10, 9, 5, 3, 2}, 20);
    public static List<List<Integer>> fitItems(int[] weights, int targetWeight) {
        //Arrays.sort(weights);
        List<List<Integer>> allCombinations = new ArrayList<>();
        List<Integer> currentCombination = new ArrayList<>();
        fitItemsRecursive(weights, targetWeight, weights.length - 1, currentCombination, allCombinations);
        return allCombinations;
    }

    public static void fitItemsRecursive(int[] weights, int targetWeight, int currentIndex, List<Integer> currentCombination, List<List<Integer>> allCombinations) {
        if (currentIndex > -1) {
            System.out.println(num++ + " index " + currentIndex + " Curr " + weights[currentIndex] + " Target " + targetWeight + " currList " + currentCombination);
        }
        
        // Base case: if the target weight becomes 0, we have found a valid combination
        if (targetWeight == 0) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }
        
        // Base case: if we have considered all items or the target weight becomes negative, the combination is not valid
        if (currentIndex < 0 ) {
            return;
        }

        // Recursive case 1: include the current item
        if (weights[currentIndex] <= targetWeight) {
            currentCombination.add(weights[currentIndex]);
            //System.out.println("  Container  " + currentCombination);
            fitItemsRecursive(weights, targetWeight - weights[currentIndex], currentIndex - 1, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
            //System.out.println("       After Container " + currentCombination);
        }

        // Recursive case 2: exclude the current item
        fitItemsRecursive(weights, targetWeight, currentIndex - 1, currentCombination, allCombinations);
    } //2, 3, 5, 20, 10, 9, 15
}
 