// You are given an integer n representing the total number of individuals. Each individual is 
// identified by a unique ID from 0 to n-1. The individuals have a unique secret that they can 
// share with others.
// The secret-sharing process begins with person 0, who initially possesses the secret. Person 0 
// can share the secret with any number of individuals simultaneously during specific time intervals. 
// Each time interval is represented by a tuple (start, end) where start and end are non-negative 
// integers indicating the start and end times of the interval.
// You need to determine the set of individuals who will eventually know the secret after all the 
// possible secret-sharing intervals have occurred.
// Example:
// Input: n = 5, intervals = [(0, 2), (1, 3), (2, 4)], firstPerson = 0
// Output: [0, 1, 2, 3, 4]

// Explanation:
// In this scenario, we have 5 individuals labeled from 0 to 4.
// The secret-sharing process starts with person 0, who has the secret at time 0. At time 0, 
// person 0 can share the secret with any other person. Similarly, at time 1, person 0 can also 
// share the secret. At time 2, person 0 shares the secret again, and so on.
// Given the intervals [(0, 2), (1, 3), (2, 4)], we can observe that during these intervals, person 0 
// shares the secret with every other individual at least once.
// Hence, after all the secret-sharing intervals, individuals 0, 1, 2, 3, and 4 will eventually 
// know the secret.

package Q2;

import java.util.*;

public class SecretSharing {
    public static List<Integer> eventuallyKnowSecret(int n, int[][] intervals, int firstPerson) {
        Set<Integer> knowSecret = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // Add the first person who initially has the secret
        knowSecret.add(firstPerson);
        queue.offer(firstPerson);

        // Iterate through the intervals
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            Set<Integer> currentInterval = new HashSet<>();
            
            // Share the secret to individuals within the current interval
            while (!queue.isEmpty() && queue.peek() <= end) {
                int person = queue.poll();
                for (int i = Math.max(person, start); i <= end; i++) {
                    currentInterval.add(i);
                }
            }
            
            // Add individuals who received the secret during this interval
            knowSecret.addAll(currentInterval);
            queue.addAll(currentInterval);
        }

        return new ArrayList<>(knowSecret);
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}};
        int firstPerson = 0;

        List<Integer> eventuallyKnow = eventuallyKnowSecret(n, intervals, firstPerson);
        System.out.println("Individuals who will eventually know the secret: " + eventuallyKnow);
    }
}