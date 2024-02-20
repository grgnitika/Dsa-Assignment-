/*
a)
You are a planner working on organizing a series of events in a row of n venues. 
Each venue can be decorated with one of the k available themes. However, adjacent venues 
should not have the same theme. The cost of decorating each venue with a certain theme varies.
The costs of decorating each venue with a specific theme are represented by an n x k cost 
matrix. For example, costs [0][0] represents the cost of decorating venue 0 with theme 0,
and costs[1][2] represents the cost of decorating venue 1 with theme 2. The task is to 
find the minimum cost to decorate all the venues while adhering to the adjacency 
constraint.

For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all 
the venues is 5. One possible arrangement is decorating venue 0 with theme 0 and venue 1 with 
theme 2, resulting in a minimum cost of 1 + 4 = 5. Alternatively, decorating venue 0 with 
theme 2 and venue 1 with theme 0 also yields a minimum cost of 3 + 2 = 5.
Write a function that takes the cost matrix as input and returns the minimum cost to decorate 
all the venues while satisfying the adjacency constraint.
Note: The costs are positive integers.
Example: 
Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] 
Output: 7 
Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. 
Minimum cost: 1 + 6 + 1 = 7.
*/


package Q1;

public class MinVenueDecorationCost {

    public static int minCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        //Initializing a 2D DP array to store the minimum cost
        int[][] dp = new int[n][k];

        // Initializing the first row of DP array with the cost of decorating each venue with the corresponding theme
        dp[0] = costs[0];

        // Iterating through the venues
        for (int i = 1; i < n; i++) {
            // Iterating through the available themes for each venue
            for (int j = 0; j < k; j++) {
                // Calculating the minimum cost for the current venue and theme
                dp[i][j] = costs[i][j] + min(dp[i - 1], j);
            }
        }

        // Returning the minimum cost from the last row of DP array
        return min(dp[n - 1]);
    }

    private static int min(int[] arr, int excludeIndex) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (i != excludeIndex) {
                min = Math.min(min, arr[i]);
            }
        }
        return min;
    }

    private static int min(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int value : arr) {
            min = Math.min(min, value);
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int result = minCost(costMatrix);
        System.out.println("The minimum cost to decorate all the venues while satisfying the adjacency constraint is: " + result);
    }
}
