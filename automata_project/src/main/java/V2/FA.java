/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package V2;

/**
 *
 * @author Nann Koungkea
 */
import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FA {
    private Set<Integer> states;
    private Set<Character> alphabet;
    private Map<Integer, Map<Character, Set<Integer>>> transitionFunction;
    private int startState;
    private Set<Integer> finalStates;

    public FA(Set<Integer> states, Set<Character> alphabet, int startState, Set<Integer> finalStates) {
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

    private Set<Integer> epsilonClosure(Set<Integer> states) {
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
    
    private void testFA(Set<Character> alphabet,int numStates,int numTransitions) {
    	int DFAnumTrans=alphabet.size()*numStates;
    	
    	if(alphabet.contains('e')) {
    		System.out.println("This is a NFA!!");
    	}else if(numTransitions != DFAnumTrans){
    		System.out.println("This is a NFA!!");
    	}else {
    		System.out.println("This is a DFA");
    	}
    }
}
