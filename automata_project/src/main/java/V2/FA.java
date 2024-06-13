/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package V2;

/**
 *
 * @author Nann Koungkea
 */
import java.util.ArrayList;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JOptionPane;

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
    public static String[] removeEle(String[] array, String element) {
        if (array == null || array.length == 0) {
            return array;
        }

        int count = 0;
        for (String s : array) {
            if (s.equals(element)) {
                count++;
            }
        }

        if (count == 0) {
            return array;
        }

        String[] newArray = new String[array.length - count];
        int index = 0;
        
        for (String s : array) {
            if (!s.equals(element)) {
                newArray[index++] = s;
            }
        }

        return newArray;
    }
    
    public boolean testFA(Set<Character> alphabet,int numStates,int numTransitions,String[] eachTrans) {
        boolean isNFA;
    	int DFAnumTrans=alphabet.size()*numStates;
    	boolean flagOneOrMore=false;
    	boolean flagMissingTransition = false;
    	
    	ArrayList<Integer> allFromStateDFA=new ArrayList<>();
        ArrayList<Integer> allFromStateUser=new ArrayList<>();
        
        
        //find zero transition from the fromState
        for(int j=0;j<numStates;j++) {
        	allFromStateDFA.add(j);
        	
        }

        for(int i=0;i<eachTrans.length;i++) {
        	String[] temp = eachTrans[i].split("\\s+");
        	
            String fromState = temp[0];
            
            allFromStateUser.add(Integer.valueOf(fromState));
            
        }
        
        Set<Integer> set1=new HashSet<>(allFromStateDFA);
        Set<Integer> set2=new HashSet<>(allFromStateUser);
        System.out.println(allFromStateDFA);
        System.out.println(allFromStateUser);
        
        boolean flagZero=set1.equals(set2);
        
        //find fromState without all the alphabet
        Map<Integer, Set<Character>> stateTransitions = new HashMap<>();

        for (String trans : eachTrans) {
            String[] temp = trans.split("\\s+");
            int fromState = Integer.parseInt(temp[0]);
            char input = temp[1].charAt(0);

            stateTransitions.computeIfAbsent(fromState, k -> new HashSet<>()).add(input);
        }

        for (int state = 0; state < numStates; state++) {
            if (!stateTransitions.containsKey(state) || !stateTransitions.get(state).containsAll(alphabet)) {
                flagMissingTransition = true;
                break;
            }
        }
        

        //find one or more transition per input from same fromState
    	for (int i = 0; i < eachTrans.length; i++) {
            String[] temp = eachTrans[i].split("\\s+");

            String fromState = temp[0];
            String input = temp[1];
            String toState = temp[2];

            String removeCurTrans = eachTrans[i];
            String[] tempEachTrans = removeEle(eachTrans, removeCurTrans);

            for (int j = 0; j < tempEachTrans.length; j++) {
                String[] oneEleTrans = tempEachTrans[j].split("\\s+");
                // Further processing can be done here
                if(fromState.equals(oneEleTrans[0]) && input.equals(oneEleTrans[1]) && !toState.equals(oneEleTrans[2])) {
                	flagOneOrMore=true;
                	break;
                }
            }
        }

    	
    	if(alphabet.contains('e')) {
                JOptionPane.showMessageDialog(null, "This is a NFA!! because of epsilon");
    		System.out.println("This is a NFA!! because of epsilon");
                return isNFA=true;
    	}else if(numTransitions != DFAnumTrans){
                JOptionPane.showMessageDialog(null,"This is a NFA!! because transition is not enough for DFA");
    		System.out.println("This is a NFA!! because transition is not enough for DFA");
                return isNFA=true;
    	}else if(flagOneOrMore ){
            JOptionPane.showMessageDialog(null,"This is a NFA!! because more than one transition ");
            System.out.println("This is a NFA!! because than one transition ");
            return isNFA=true;
    	}else if(!flagZero) {
            JOptionPane.showMessageDialog(null,"This is a NFA because one or more of fromStates don't have a transition");
            System.out.println("This is a NFA because one or more of fromStates don't have a transition");
            return isNFA=true;
    	}else if(flagMissingTransition) {
            JOptionPane.showMessageDialog(null,"This is a NFA because of missing transition of one or more symbol");
            System.out.println("This is a NFA because of missing transition of one or more symbol");
            return isNFA=true;
    	}
    	else {
            JOptionPane.showMessageDialog(null,"This is a DFA");
            System.out.println("This is a DFA");
            return isNFA=false;
    	}
    }
}
