/* 
a)
A 2D grid representing a maze in a virtual game world is given. The grid is of size m x n 
and consists of different types of cells: 'P' represents an empty path where you can move 
freely. 'W' represents a wall that you cannot pass through. 'S' represents the starting 
point. Lowercase letters represent hidden keys. Uppercase letters represent locked doors.
You start at the starting point 'S' and can move in any of the four cardinal directions 
(up, down, left, right) to adjacent cells. However, you cannot walk through walls ('W').
As you explore the maze, you may come across hidden keys represented by lowercase letters. 
To unlock a door represented by an uppercase letter, you need to collect the corresponding key 
first. Once you have a key, you can pass through the corresponding locked door.
For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k 
letters of the English alphabet in the maze. This means that there is exactly one key for each 
door, and one door for each key. The letters used to represent the keys and doors follow the 
English alphabet order.
The task is to find the minimum number of moves required to collect all the keys. If it is 
impossible to collect all the keys and reach the exit, return -1.

Example:
Input: grid = [ ["S","P","q","P","P"], ["W","W","W","P","W"], ["r","P","Q","P","R"]] 
Output: 8
Note: The goal is to Collect all key.
*/

package Q4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class MinMovesForCollectingKeys {
    static class State {
        int x, y, keys, steps;

        State(int x, int y, int keys, int steps) {
            this.x = x;
            this.y = y;
            this.keys = keys;
            this.steps = steps;
        }
    }

    public static int collectAllKeys(String[] grid) {
        int m = grid.length, n = grid[0].length();
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int allKeys = 0;

        // Find starting point and total number of keys
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i].charAt(j);
                if (cell == 'S') {
                    queue.offer(new State(i, j, 0, 0));
                    visited.add(i + "," + j + ",0");
                } else if (cell >= 'a' && cell <= 'f') {
                    allKeys |= (1 << (cell - 'a'));
                }
            }
        }

        // Directions: up, down, left, right
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Checking if all keys collected
            if (current.keys == allKeys)
                return current.steps;

            for (int[] dir : directions) {
                int newX = current.x + dir[0], newY = current.y + dir[1];
                int newKeys = current.keys;
                if (newX >= 0 && newX < m && newY >= 0 && newY < n) {
                    char cell = grid[newX].charAt(newY);
                    // Checks if it's a wall
                    if (cell == 'W')
                        continue;
                    // Checks if it's a door and we have the key
                    if (cell >= 'A' && cell <= 'F' && (newKeys & (1 << (cell - 'A'))) == 0)
                        continue;
                    // Collects key if found
                    if (cell >= 'a' && cell <= 'f')
                        newKeys |= (1 << (cell - 'a'));

                    String newState = newX + "," + newY + "," + newKeys;
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.offer(new State(newX, newY, newKeys, current.steps + 1));
                    }
                }
            }
        }

        // exits the loop,if no solution found
        return -1;
    }

    public static void main(String[] args) {
        String[] grid1 = { "SPaPP", "WWWPW", "bPAPB" };
        System.out.println("Minimum moves required to collect all the keys: " + collectAllKeys(grid1));
    }
}