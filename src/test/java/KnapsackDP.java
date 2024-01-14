import java.util.ArrayList;
import java.util.List;

public class KnapsackDP {
    public static void main(String[] args) {
        //System.out.println(fitItems(new int[] {11,8,7,6,5}, 20));
        //System.out.println(fitItems(new int[] {13,10,18, 2, 1, 3, 7}, 13));
        System.out.println(fitItems(new int[] {1,2,3,4,11, 9, 8, 7, 6, 181}, 20));
    }

    //Just find the first one combination
    public static List<Integer> fitItems(int[] weights, int targetWeight) {
        int n = weights.length;
        boolean[][] dp = new boolean[n + 1][targetWeight + 1];

        // Base case: when the target weight is 0, the knapsack can be filled with no items
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= targetWeight; j++) {
                // If the current item's weight is less than or equal to the target weight,
                // we have two choices: include the item or exclude it
                if (weights[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j - weights[i - 1]] || dp[i - 1][j];
                } else {
                    // If the current item's weight is greater than the target weight,
                    // we can only exclude it
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // Backtrack to find the selected items
        List<Integer> selectedItems = new ArrayList<>();
        int i = n, j = targetWeight;
        while (i > 0 && j > 0) {
            if (dp[i][j] && !dp[i - 1][j]) {
                selectedItems.add(weights[i - 1]);
                j -= weights[i - 1];
            }
            i--;
        }

        return selectedItems;
    }
}
