package automation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFA {
    private Set<Integer> states;
    private Set<Character> alphabet;
    private Map<Integer, Map<Character, Integer>> transitionFunction;
    private int startState;
    private Set<Integer> finalStates;

    public DFA(Set<Integer> states, Set<Character> alphabet, int startState, Set<Integer> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitionFunction = new HashMap<>();
        for (int state : states) {
            transitionFunction.put(state, new HashMap<>());
        }
    }

    public void addTransition(int fromState, char input, int toState) {
        if (!states.contains(fromState) || !states.contains(toState) || !alphabet.contains(input)) {
            throw new IllegalArgumentException("Invalid states or input symbol.");
        }
        if (transitionFunction.get(fromState).containsKey(input)) {
            throw new IllegalArgumentException("Deterministic transition already exists for this state and input.");
        }
        transitionFunction.get(fromState).put(input, toState);
    }

    public boolean isAccepting(String input) {
        int currentState = startState;
        for (char symbol : input.toCharArray()) {
            if (!transitionFunction.get(currentState).containsKey(symbol)) {
                return false; // No valid transition for the input symbol
            }
            currentState = transitionFunction.get(currentState).get(symbol);
        }
        return finalStates.contains(currentState);
    }

    public Set<Integer> getStates() {
        return states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public Map<Integer, Map<Character, Integer>> getTransitionFunction() {
        return transitionFunction;
    }

    public int getStartState() {
        return startState;
    }

    public Set<Integer> getFinalStates() {
        return finalStates;
    }
}
