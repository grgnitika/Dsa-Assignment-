/* 
b)
A spaceship is equipped with a set of engines, where each engine is represented by 
a block. Each engine requires a specific amount of time to be built and can only be built by 
one engineer. The task is to determine the minimum time needed to build all the engines 
using the available engineers. The engineers can either work on building an engine or split 
into two engineers, with each engineer sharing the workload equally. Both decisions incur a 
time cost.The time cost of splitting one engineer into two engineers is given as an integer
split. 
Note that if two engineers split at the same time, they split in parallel so the cost would 
be split.
The objective of the given scenario is to calculate the minimum time needed to build all 
the engines, considering the time cost of splitting engineers.
Input: engines= [1,2,3]
Split cost (k)=1
Output: 4
Explanation:
Imagine you need to build engine represented by an array [1,2,3] where its element of an array 
i.e a[i] represents unit time to build its engine and the split cost is 1. Initially, there is 
only one engineer available.
Here, the optimal strategy is as follows:
1.	The engineer splits into two engineers, increasing the total count to two. (Split Time: 1) 
and assign first engineer to build third engine i.e. which will take 3 unit of time.
2.	Again, the second engineer split into two (split time :1) and assign them to build first 
and second engine respectively.
Therefore, the minimum time needed to build all the engines using optimal decisions on 
splitting engineers and assigning them to engines. =1+ max (3, 1 + max (1, 2)) = 4.
Note: The splitting process occurs in parallel, and the goal is to minimize the total time 
required to build all the engines using the available engineers while considering the time 
cost of splitting.
*/

package Q1;

public class MinTimeToBuildEngines {
    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        if (engines.length == 1) {
            return engines[0];
        }

        int numEngines = engines.length;
        int minTime = Integer.MAX_VALUE;

        // Iterating through all possible splits
        for (int i = 1; i < numEngines; i++) {
            int timeToBuildFirstHalf = maxTime(engines, 0, i);
            int timeToBuildSecondHalf = maxTime(engines, i, numEngines);
            int totalTime = Math.max(timeToBuildFirstHalf, timeToBuildSecondHalf) + splitCost;

            minTime = Math.min(minTime, totalTime);
        }

        return minTime;
    }

    // Calculating the maximum time needed to build engines from start to end index
    private static int maxTime(int[] engines, int start, int end) {
        int max = engines[start];
        for (int i = start + 1; i < end; i++) {
            max = Math.max(max, engines[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;

        int minTime = minTimeToBuildEngines(engines, splitCost);
        System.out.println("The minimum time needed to build all engines considering the time cost of splitting engineers is: " + minTime);
    }
}
