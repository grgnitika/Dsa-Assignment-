/*
b)
Implement Kruskal algorithm and priority queue using minimum heap.
*/

package Q3;

import java.util.*;

class Edge implements Comparable<Edge> {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return this.weight - other.weight;
    }
}

class DisjointSet {
    private int[] parent;
    private int[] rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);
        if (xRoot == yRoot) return;
        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[xRoot] > rank[yRoot]) {
            parent[yRoot] = xRoot;
        } else {
            parent[yRoot] = xRoot;
            rank[xRoot]++;
        }
    }
}

public class KruskalAlgorithm {
    private int V;
    private List<Edge> edges;
    private PriorityQueue<Edge> minHeap;

    public KruskalAlgorithm(int V) {
        this.V = V;
        edges = new ArrayList<>();
        minHeap = new PriorityQueue<>();
    }

    public void addEdge(int src, int dest, int weight) {
        edges.add(new Edge(src, dest, weight));
    }

    public void findMinimumSpanningTree() {
        // Sorting the edges by weight using a priority queue (minimum heap)
        for (Edge edge : edges) {
            minHeap.offer(edge);
        }

        DisjointSet ds = new DisjointSet(V);
        int totalWeight = 0;
        System.out.println("Minimum Spanning Tree:");
        while (!minHeap.isEmpty()) {
            Edge edge = minHeap.poll();
            int x = ds.find(edge.src);
            int y = ds.find(edge.dest);
            if (x != y) {
                System.out.println("Edge: " + edge.src + " - " + edge.dest + ", Weight: " + edge.weight);
                totalWeight += edge.weight;
                ds.union(x, y);
            }
        }
        System.out.println("Total Weight: " + totalWeight);
    }

    public static void main(String[] args) {
        KruskalAlgorithm graph = new KruskalAlgorithm(6);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 3);
        graph.addEdge(2, 4, 1);
        graph.addEdge(2, 5, 5);
        graph.addEdge(3, 4, 5);
        graph.addEdge(4, 5, 7);
        graph.findMinimumSpanningTree();
    }
}