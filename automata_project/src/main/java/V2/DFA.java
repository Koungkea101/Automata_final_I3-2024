package V2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    
}