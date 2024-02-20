/* 
a)
A clothing manufacturing factory has a production line of super sewing machines. The 
production line consists of n super sewing machines placed in a line. Initially, each sewing
machine has a certain number of dresses or is empty. For each move, you can select any
m (1 <= m <= n) consecutive sewing machines on the production line and pass one dress 
from each selected sewing machine to its adjacent sewing machine simultaneously.
The objective is to equalize the number of dresses in all the sewing machines on the 
production line. The task is to determine the minimum number of moves required to achieve 
this objective. If it is not possible to equalize the number of dresses, -1 should be returned.
Input: [1,0,5]
Output: 3

Example:
Imagine you have a production line with the following number of dresses in each sewing 
machine: [1,0,5]. The production line has 5 sewing machines.
Here's how the process works:
1.	Initial state: [1,0,5]
2.	Move 1: Pass one dress from the third sewing machine to the first sewing machine, 
resulting in [1,1,4]
3.	Move 2: Pass one dress from the second sewing machine to the first sewing machine, 
and from third to first sewing Machine [2,1,3]
4.	Move 3: Pass one dress from the third sewing machine to the second sewing machine, 
resulting in [2,2,2]
After these 3 moves, the number of dresses in each sewing machine is equalized to 2. Therefore, 
the minimum number of moves required to equalize the number of dresses is 3.
*/

package Q2;

public class MinMovesForEqualizingDresses {
    public static int minMoves(int[] dresses) {
        int n = dresses.length;
        int totalMoves = 0;

        // Calculating the total sum of dresses
        int sum = 0;
        for (int dress : dresses) {
            sum += dress;
        }

        // Calculating the target number of dresses for each machine
        int target = sum / n;

        // If the total sum is not evenly divisible by the number of machines, it's not possible to equalize dresses
        if (sum % n != 0) {
            return -1;
        }

        // Iterating through the machines
        for (int i = 0; i < n; i++) {
            int diff = dresses[i] - target;

            // If the current machine has more dresses than the target
            if (diff > 0) {
                // Distributing the excess dresses to the adjacent machines
                if (i > 0) {
                    dresses[i] -= diff;
                    dresses[i - 1] += diff;
                    totalMoves += diff;
                }
                // Distributing the excess dresses to the machines on the right
                if (i < n - 1) {
                    dresses[i] -= diff;
                    dresses[i + 1] += diff;
                    totalMoves += diff;
                }
            }
        }

        return totalMoves;
    }

    public static void main(String[] args) {
        int[] dresses = {1, 0, 5};
        int minMoves = minMoves(dresses);

        if (minMoves != -1) {
            System.out.println("Minimum number of moves required to equalize the number of dresses: " + minMoves);
        } else {
            System.out.println("It is not possible to equalize the number of dresses.");
        }
    }
}