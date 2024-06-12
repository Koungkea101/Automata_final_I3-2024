import java.util.*;

public class DFA {
    private Set<String> states;
    private String startState;
    private Set<String> acceptStates;
    private Map<String, Map<String, String>> transitionFunction;

    public DFA(Set<String> states, String startState, Set<String> acceptStates, List<String[]> transitions) {
        this.states = states;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitionFunction = new HashMap<>();

        for (String state : states) {
            transitionFunction.put(state, new HashMap<>());
        }

        for (String[] transition : transitions) {
            String fromState = transition[0];
            String input = transition[1];
            String toState = transition[2];
            transitionFunction.get(fromState).put(input, toState);
        }
    }

    public boolean run(String input) {
        String currentState = startState;

        for (char symbol : input.toCharArray()) {
            currentState = transitionFunction.get(currentState).get(String.valueOf(symbol));
            if (currentState == null) {
                return false;
            }
        }

        return acceptStates.contains(currentState);
    }

    // Implement other necessary methods here (e.g., isDeterministic, minimize, etc.)
}
