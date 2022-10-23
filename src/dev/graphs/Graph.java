package dev.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Graph {
    // vertex n, list of linked vertexes
    List<List<Integer>> vertex = new ArrayList<>();

/*    public static List<List<Integer>> create() {

    }*/

    public boolean action(List<List<Integer>> d, int start, int target) {
        if (start == target) {
            return true;
        }
        boolean[] visited = new boolean[d.size()];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int current = queue.remove();
            if (current == target) {
                return true;
            }
            if (visited[current]) {
                continue;
            }

            visited[current] = true;

            List<Integer> links = d.get(current);
            for (Integer link : links) {
                if (!visited[link]) {
                    queue.add(link);
                }
            }
        }

        return false;
    }

    public static class Pair<T, V> {
        private final T first;
        private final V second;

        public Pair(T first, V second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }
    }
}
