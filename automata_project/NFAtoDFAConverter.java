package automation;
import java.util.*;

public class NFAtoDFAConverter {

    public static DFA convert(NFA_demo1 nfa) {
        // Initialize DFA components
        Set<Set<Integer>> dfaStates = new HashSet<>();
        Map<Set<Integer>, Map<Character, Set<Integer>>> dfaTransitionFunction = new HashMap<>();
        Set<Set<Integer>> dfaAcceptStates = new HashSet<>();
        Set<Integer> nfaFinalStates = nfa.getFinalStates();
        
        // Queue for processing DFA states
        Queue<Set<Integer>> queue = new LinkedList<>();
        
        // Start state for DFA is the epsilon closure of the NFA start state
        Set<Integer> startState = nfa.epsilonClosure(Set.of(nfa.getStartState()));
        queue.add(startState);
        dfaStates.add(startState);
        
        while (!queue.isEmpty()) {
            Set<Integer> current = queue.poll();
            dfaTransitionFunction.put(current, new HashMap<>());

            for (char symbol : nfa.getAlphabet()) {
                if (symbol == 'ε') continue; // Skip epsilon transitions

                Set<Integer> nextState = new HashSet<>();
                for (int state : current) {
                    Set<Integer> nfaNextState = nfa.getTransition(state, symbol);
                    if (nfaNextState != null) {
                        nextState.addAll(nfaNextState);
                    }
                }

                nextState = nfa.epsilonClosure(nextState);
                dfaTransitionFunction.get(current).put(symbol, nextState);

                if (!dfaStates.contains(nextState)) {
                    dfaStates.add(nextState);
                    queue.add(nextState);
                }
            }

            // Check if the current DFA state is an accept state
            for (int state : current) {
                if (nfaFinalStates.contains(state)) {
                    dfaAcceptStates.add(current);
                    break;
                }
            }
        }
        
        // Convert DFA state sets to string representations
        Set<String> dfaStateNames = new HashSet<>();
        Map<Set<Integer>, String> stateNameMap = new HashMap<>();
        int stateCounter = 0;
        
        for (Set<Integer> state : dfaStates) {
            String stateName = "Q" + stateCounter++;
            dfaStateNames.add(stateName);
            stateNameMap.put(state, stateName);
        }

        String dfaStartState = stateNameMap.get(startState);
        Set<String> dfaAcceptStateNames = new HashSet<>();
        
        for (Set<Integer> state : dfaAcceptStates) {
            dfaAcceptStateNames.add(stateNameMap.get(state));
        }

        List<String[]> dfaTransitions = new ArrayList<>();
        for (Map.Entry<Set<Integer>, Map<Character, Set<Integer>>> entry : dfaTransitionFunction.entrySet()) {
            Set<Integer> fromState = entry.getKey();
            for (Map.Entry<Character, Set<Integer>> trans : entry.getValue().entrySet()) {
                char symbol = trans.getKey();
                Set<Integer> toState = trans.getValue();
                dfaTransitions.add(new String[]{stateNameMap.get(fromState), String.valueOf(symbol), stateNameMap.get(toState)});
            }
        }

        // Create and return the DFA
        return new DFA(dfaStateNames, dfaStartState, dfaAcceptStateNames, dfaTransitions);
    }
}

class NFA_demo1 {
    private Set<Integer> states;
    private Set<Character> alphabet;
    private Map<Integer, Map<Character, Set<Integer>>> transitionFunction;
    private int startState;
    private Set<Integer> finalStates;

    public NFA_demo1(Set<Integer> states, Set<Character> alphabet, int startState, Set<Integer> finalStates) {
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
        if (!states.contains(fromState) || !states.contains(toState) || (input != 'ε' && !alphabet.contains(input))) {
            throw new IllegalArgumentException("Invalid states or input symbol.");
        }
        transitionFunction.computeIfAbsent(fromState, k -> new HashMap<>());
        transitionFunction.get(fromState).computeIfAbsent(input, k -> new HashSet<>()).add(toState);
    }

    public boolean isAccepting(String input) {
        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(startState);
        currentStates = epsilonClosure(currentStates);

        for (char symbol : input.toCharArray()) {
            Set<Integer> nextStates = new HashSet<>();
            for (int state : currentStates) {
                if (transitionFunction.get(state).containsKey(symbol)) {
                    nextStates.addAll(transitionFunction.get(state).get(symbol));
                }
            }
            currentStates = epsilonClosure(nextStates);
        }

        for (int state : currentStates) {
            if (finalStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    public Set<Integer> epsilonClosure(Set<Integer> states) {
        Set<Integer> closure = new HashSet<>(states);
        Set<Integer> stack = new HashSet<>(states);

        while (!stack.isEmpty()) {
            int state = stack.iterator().next();
            stack.remove(state);
            if (transitionFunction.get(state).containsKey('ε')) {
                for (int nextState : transitionFunction.get(state).get('ε')) {
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.add(nextState);
                    }
                }
            }
        }
        return closure;
    }

    // Getter methods for accessing NFA components
    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public int getStartState() {
        return startState;
    }

    public Set<Integer> getFinalStates() {
        return finalStates;
    }

    public Set<Integer> getTransition(int state, char input) {
        return transitionFunction.getOrDefault(state, new HashMap<>()).get(input);
    }
}

class DFA {
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

    // Rest of your DFA class as provided
}

public class Main {
    public static void main(String[] args) {
        // Sample NFA to test conversion
        Set<Integer> states = new HashSet<>(Arrays.asList(0, 1, 2));
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b', 'ε'));
        int startState = 0;
        Set<Integer> finalStates = new HashSet<>(Collections.singletonList(2));
        
        NFA_demo1 nfa = new NFA_demo1(states, alphabet, startState, finalStates);
        nfa.addTransition(0, 'a', 0);
        nfa.addTransition(0, 'a', 1);
        nfa.addTransition(1, 'b', 2);
        nfa.addTransition(0, 'ε', 1);

        DFA dfa = NFAtoDFAConverter.convert(nfa);

        // Test the resulting DFA
        String testInput = "aab";
        boolean result = dfa.run(testInput);
        System.out.println("Input " + testInput + " is " + (result ? "accepted" : "rejected") + " by the DFA.");
    }
}
