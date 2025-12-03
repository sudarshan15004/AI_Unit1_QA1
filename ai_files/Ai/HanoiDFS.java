import java.util.*;

public class HanoiDFS {
    
    static class State {
        private final List<Integer>[] pegs;
        
        @SuppressWarnings("unchecked")
        public State(List<Integer>[] pegs) {
            this.pegs = new List[3];
            for (int i = 0; i < 3; i++) {
                this.pegs[i] = new ArrayList<>(pegs[i]);
            }
        }
        
        public List<Integer>[] getPegs() { return pegs; }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State)) return false;
            return Arrays.deepEquals(pegs, ((State)obj).pegs);
        }
        
        @Override
        public int hashCode() {
            return Arrays.deepHashCode(pegs);
        }
    }
    
    static class Move {
        int from, to;
        Move(int from, int to) { this.from = from; this.to = to; }
        @Override public String toString() {
            return "Move disk from peg " + from + " to peg " + to;
        }
    }
    
    static boolean isGoal(State state, int n) {
        return state.getPegs()[2].size() == n;
    }
    
    static List<Move> getPossibleMoves(State state) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Integer> fromPeg = state.getPegs()[i];
            if (fromPeg.isEmpty()) continue;
            int disk = fromPeg.get(fromPeg.size() - 1);
            for (int j = 0; j < 3; j++) {
                if (i == j) continue;
                List<Integer> toPeg = state.getPegs()[j];
                if (toPeg.isEmpty() || toPeg.get(toPeg.size() - 1) > disk) {
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }
    
    static State applyMove(State state, Move move) {
        List<Integer>[] newPegs = new List[3];
        for (int i = 0; i < 3; i++) {
            newPegs[i] = new ArrayList<>(state.getPegs()[i]);
        }
        int disk = newPegs[move.from].remove(newPegs[move.from].size() - 1);
        newPegs[move.to].add(disk);
        return new State(newPegs);
    }
    
    public static List<Move> solveDFS(int n) {
        List<Integer>[] initialPegs = new List[3];
        initialPegs[0] = new ArrayList<>(Arrays.asList(n, n-1, 1));
        initialPegs[1] = new ArrayList<>();
        initialPegs[2] = new ArrayList<>();
        State start = new State(initialPegs);
        
        Stack<State> stack = new Stack<>();
        Stack<List<Move>> pathStack = new Stack<>();
        Set<State> visited = new HashSet<>();
        
        stack.push(start);
        pathStack.push(new ArrayList<>());
        
        while (!stack.isEmpty()) {
            State current = stack.pop();
            List<Move> path = pathStack.pop();
            
            if (visited.contains(current)) continue;
            visited.add(current);
            
            if (isGoal(current, n)) {
                return path;
            }
            
            for (Move move : getPossibleMoves(current)) {
                State next = applyMove(current, move);
                if (!visited.contains(next)) {
                    List<Move> newPath = new ArrayList<>(path);
                    newPath.add(move);
                    stack.push(next);
                    pathStack.push(newPath);
                }
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        List<Move> solution = solveDFS(3);
        System.out.println("=== DFS Tower of Hanoi Solution ===");
        if (solution != null) {
            for (int i = 0; i < solution.size(); i++) {
                System.out.println((i+1) + ". " + solution.get(i));
            }
            System.out.println("Total moves: " + solution.size());
        }
    }
}
