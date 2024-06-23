package V2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DFA {
    public Set<Integer> states;
    public Set<Character> alphabet;
    public Map<Integer, Map<Character, Integer>> transitionFunction;
    public int startState;
    public Set<Integer> finalStates;

    // Constructor to initialize a DFA
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

    // Method to add a transition to the DFA
    public void addTransition(int fromState, char input, int toState) {
        if (!states.contains(fromState) || !states.contains(toState) || !alphabet.contains(input)) {
            throw new IllegalArgumentException("Invalid states or input symbol.");
        }
        if (transitionFunction.get(fromState).containsKey(input)) {
            throw new IllegalArgumentException("Deterministic transition already exists for this state and input.");
        }
        transitionFunction.get(fromState).put(input, toState);
    }
    
    public boolean testString(String input) {
        // Initialize the current state to the start state
        int currState = startState;

        // Process each character in the input string
        for (char symbol : input.toCharArray()) {
            // Check if there is a transition for the current state and input symbol
            if (transitionFunction.get(currState).containsKey(symbol)) {
                // Move to the next state according to the transition function
                currState = transitionFunction.get(currState).get(symbol);
            } else {
                // If there is no transition for the input symbol, return false
                return false;
            }
        }

        // Check if the final state is one of the accepting states
        return finalStates.contains(currState);
    }
    
    // Getters for NFA components
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