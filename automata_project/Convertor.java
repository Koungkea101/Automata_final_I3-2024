package automation;

import automation.NFA_demo1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Convertor {

    public static DFA convertNFAtoDFA(NFA_demo1 nfa) {
        Set<Integer> nfaStates = nfa.getStates();
        Set<Character> nfaAlphabet = nfa.getAlphabet();
        Map<Integer, Map<Character, Set<Integer>>> nfaTransitionFunction = nfa.getTransitionFunction();
        int nfaStartState = nfa.getStartState();
        Set<Integer> nfaFinalStates = nfa.getFinalStates();

        Set<Set<Integer>> dfaStates = new HashSet<>();
        Set<Character> dfaAlphabet = new HashSet<>(nfaAlphabet);
        Map<Set<Integer>, Map<Character, Set<Integer>>> dfaTransitionFunction = new HashMap<>();
        Set<Set<Integer>> dfaFinalStates = new HashSet<>();

        // Initial state of DFA is the epsilon-closure of the NFA's start state
        Set<Integer> dfaStartState = nfa.epsilonClosure(Set.of(nfaStartState));
        dfaStates.add(dfaStartState);

        // Process states in DFA
        Set<Set<Integer>> unmarkedStates = new HashSet<>();
        unmarkedStates.add(dfaStartState);

        while (!unmarkedStates.isEmpty()) {
            Set<Integer> currentState = unmarkedStates.iterator().next();
            unmarkedStates.remove(currentState);

            Map<Character, Set<Integer>> currentTransitions = new HashMap<>();

            for (char symbol : dfaAlphabet) {
                if (symbol == 'ε') continue;

                Set<Integer> nextState = new HashSet<>();
                for (int state : currentState) {
                    if (nfaTransitionFunction.containsKey(state) && nfaTransitionFunction.get(state).containsKey(symbol)) {
                        nextState.addAll(nfaTransitionFunction.get(state).get(symbol));
                    }
                }

                nextState = nfa.epsilonClosure(nextState);

                if (!dfaStates.contains(nextState)) {
                    dfaStates.add(nextState);
                    unmarkedStates.add(nextState);
                }

                currentTransitions.put(symbol, nextState);
            }

            dfaTransitionFunction.put(currentState, currentTransitions);
        }

        // Identify final states in the DFA
        for (Set<Integer> dfaState : dfaStates) {
            for (int nfaState : dfaState) {
                if (nfaFinalStates.contains(nfaState)) {
                    dfaFinalStates.add(dfaState);
                    break;
                }
            }
        }

        // Convert Set<Set<Integer>> to Set<Integer> for DFA constructor
        Map<Set<Integer>, Integer> stateMapping = new HashMap<>();
        int stateCounter = 0;
        for (Set<Integer> dfaState : dfaStates) {
            stateMapping.put(dfaState, stateCounter++);
        }

        Set<Integer> dfaStateSet = new HashSet<>(stateMapping.values());
        int dfaStart = stateMapping.get(dfaStartState);
        Set<Integer> dfaFinalStateSet = new HashSet<>();
        for (Set<Integer> dfaState : dfaFinalStates) {
            dfaFinalStateSet.add(stateMapping.get(dfaState));
        }

        DFA dfa = new DFA(dfaStateSet, dfaAlphabet, dfaStart, dfaFinalStateSet);

        for (Map.Entry<Set<Integer>, Map<Character, Set<Integer>>> entry : dfaTransitionFunction.entrySet()) {
            int fromState = stateMapping.get(entry.getKey());
            for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                int toState = stateMapping.get(transition.getValue());
                dfa.addTransition(fromState, symbol, toState);
            }
        }

        return dfa;
    }
}
