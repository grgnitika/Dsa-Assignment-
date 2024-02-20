/*
a)
Implement ant colony algorithm solving travelling a salesman problem.
*/

package Q5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyTSP {
    private int numCities;
    private int[][] distanceMatrix;
    private double[][] pheromoneMatrix;
    private double alpha; // Pheromone factor
    private double beta; // Distance factor
    private double evaporationRate;
    private int numAnts;
    private int maxIterations;

    public AntColonyTSP(int numCities, int[][] distanceMatrix, double alpha, double beta, double evaporationRate, int numAnts, int maxIterations) {
        this.numCities = numCities;
        this.distanceMatrix = distanceMatrix;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.numAnts = numAnts;
        this.maxIterations = maxIterations;
        initializePheromones();
    }

    private void initializePheromones() {
        pheromoneMatrix = new double[numCities][numCities];
        double initialPheromone = 1.0 / numCities;
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] = initialPheromone;
            }
        }
    }

    public List<Integer> solveTSP() {
        List<Integer> bestTour = null;
        double bestTourLength = Double.POSITIVE_INFINITY;

        for (int iter = 0; iter < maxIterations; iter++) {
            List<List<Integer>> antTours = new ArrayList<>();
            double[] tourLengths = new double[numAnts];

            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = generateAntTour();
                antTours.add(tour);
                tourLengths[ant] = evaluateTour(tour);
            }

            for (int ant = 0; ant < numAnts; ant++) {
                if (tourLengths[ant] < bestTourLength) {
                    bestTourLength = tourLengths[ant];
                    bestTour = new ArrayList<>(antTours.get(ant));
                }
            }

            updatePheromones(antTours, tourLengths);
        }

        return bestTour;
    }

    private List<Integer> generateAntTour() {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[numCities];
        Random random = new Random();
        int startCity = random.nextInt(numCities);

        tour.add(startCity);
        visited[startCity] = true;

        for (int i = 1; i < numCities; i++) {
            int nextCity = selectNextCity(tour, visited);
            tour.add(nextCity);
            visited[nextCity] = true;
        }

        return tour;
    }

    private int selectNextCity(List<Integer> tour, boolean[] visited) {
        int currentCity = tour.get(tour.size() - 1);
        double[] probabilities = new double[numCities];
        double total = 0.0;

        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromoneMatrix[currentCity][i], alpha)
                                    * Math.pow(1.0 / distanceMatrix[currentCity][i], beta);
                total += probabilities[i];
            }
        }

        double rand = Math.random() * total;
        double sum = 0.0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                sum += probabilities[i];
                if (sum >= rand) {
                    return i;
                }
            }
        }

        return -1; // Should not happen
    }

    private double evaluateTour(List<Integer> tour) {
        double totalDistance = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            totalDistance += distanceMatrix[city1][city2];
        }
        int lastCity = tour.get(tour.size() - 1);
        int firstCity = tour.get(0);
        totalDistance += distanceMatrix[lastCity][firstCity]; // Close the loop
        return totalDistance;
    }

    private void updatePheromones(List<List<Integer>> antTours, double[] tourLengths) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] *= (1 - evaporationRate);
            }
        }

        for (int ant = 0; ant < numAnts; ant++) {
            List<Integer> tour = antTours.get(ant);
            double tourLength = tourLengths[ant];
            for (int i = 0; i < numCities - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromoneMatrix[city1][city2] += 1.0 / tourLength;
                pheromoneMatrix[city2][city1] += 1.0 / tourLength; // Symmetric matrix
            }
        }
    }

    public static void main(String[] args) {
        int numCities = 5;
        int[][] distanceMatrix = {
                {0, 10, 15, 20, 25},
                {10, 0, 35, 25, 30},
                {15, 35, 0, 30, 10},
                {20, 25, 30, 0, 15},
                {25, 30, 10, 15, 0}
        };
        double alpha = 1.0;
        double beta = 2.0;
        double evaporationRate = 0.1;
        int numAnts = 10;
        int maxIterations = 100;

        AntColonyTSP antColony = new AntColonyTSP(numCities, distanceMatrix, alpha, beta, evaporationRate, numAnts, maxIterations);
        List<Integer> bestTour = antColony.solveTSP();
        System.out.println("Best tour: " + bestTour);
    }
}
