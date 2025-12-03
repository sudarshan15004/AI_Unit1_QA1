import java.util.*;
public class BFS {

    static class State {
        List<Integer>[] pegs;

        @SuppressWarnings("unchecked")
        State() {
            pegs = new List[3];
            for (int i = 0; i < 3; i++)
                pegs[i] = new ArrayList<>();
        }

        State cloneState() {
            State s = new State();
            for (int i = 0; i < 3; i++)
                s.pegs[i].addAll(pegs[i]);
            return s;
        }

        boolean isGoal() {
            return pegs[0].isEmpty() && pegs[1].isEmpty() && pegs[2].size() == 3;
        }

        @Override
        public String toString() {
            return pegs[0] + " | " + pegs[1] + " | " + pegs[2];
        }
    }

    static class Node {
        State state;
        List<String> path;
    }

    public static void main(String[] args) {
        State start = new State();
        start.pegs[0].addAll(Arrays.asList(3, 2, 1));

        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        Node startNode = new Node();
        startNode.state = start;
        startNode.path = new ArrayList<>();

        queue.add(startNode);

        System.out.println("BFS Search Started...");

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.state.isGoal()) {
                System.out.println("Solution Path:");
                current.path.forEach(System.out::println);
                System.out.println(current.state);
                return;
            }

            visited.add(current.state.toString());

            for (int from = 0; from < 3; from++) {
                if (current.state.pegs[from].isEmpty()) continue;

                for (int to = 0; to < 3; to++) {
                    if (from == to) continue;

                    if (!current.state.pegs[to].isEmpty() &&
                            current.state.pegs[from].get(current.state.pegs[from].size() - 1) >
                            current.state.pegs[to].get(current.state.pegs[to].size() - 1))
                        continue;

                    State next = current.state.cloneState();
                    int disk = next.pegs[from].remove(next.pegs[from].size() - 1);
                    next.pegs[to].add(disk);

                    if (!visited.contains(next.toString())) {
                        Node newNode = new Node();
                        newNode.state = next;
                        newNode.path = new ArrayList<>(current.path);
                        newNode.path.add("Move disk " + disk + " from Peg " + from + " to Peg " + to);
                        queue.add(newNode);
                    }
                }
            }
        }
    }
}