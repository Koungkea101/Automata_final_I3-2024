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
import java.util.Stack;

import javax.swing.JOptionPane;


public class FA {
	//init all the variable
	public Set<Integer> allStates;
	public Set<Character> alphabet;
	public Map<Integer,Map<Character, Set<Integer>>> transFunction;
	public int startState;
	public Set<Integer> finalStates;
	
	//constructor
	public FA(Set<Integer> allStates, Set<Character> alphabet, int startState, Set<Integer> finalStates) {
        this.allStates = allStates;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transFunction = new HashMap<>();
        for (int s : allStates) {
        	transFunction.put(s, new HashMap<>());
        }
    }
	
	public void addTransition(int fromState,char ch,int toState) {
		//check if the input is corrected or not
		if (!allStates.contains(fromState) || !allStates.contains(toState) || (ch != 'ε' && !alphabet.contains(ch))) {
			JOptionPane.showMessageDialog(null, "Wrong input in transition!! please try again");
		}else {
			//add a new transistion to the class
			Map<Character, Set<Integer>> stateTrans = transFunction.get(fromState);
	        Set<Integer> trans = stateTrans.computeIfAbsent(ch, k -> new HashSet<>());
	        trans.add(toState);
		}
	}
	
	//function for checking acceptance string
	public boolean testString(String input) {
		Set<Integer> currStates=new HashSet<>();
		//add the first state into the currState to start the transition
		currStates.add(startState);
		//find all state that are reachable from start state via epsilon
		currStates=epsilonClosure(currStates);
		
		//processing input one at a time
		for (char symbol : input.toCharArray()) {
			//create to hold state that are reachable by the selected input
            Set<Integer> nextStates = new HashSet<>();
            //check if there are transitions for the selected input
            for (int state : currStates) {
            	//if the transition exist add it to the nextState set
                if (transFunction.get(state).containsKey(symbol)) {
                    nextStates.addAll(transFunction.get(state).get(symbol));
                }
            }
            //do epsilon Closure to the new nextStates
            currStates = epsilonClosure(nextStates);
        }
		
		//check if the current state is the final state or not
		for (int state : currStates) {
            if (finalStates.contains(state)) {
            	//this mean the string is accepted
                return true;
            }
        }
        return false;
	}
	
	public Set<Integer> epsilonClosure(Set<Integer> states) {
		//init closure set
        Set<Integer> closure = new HashSet<>(states);
        
        //init stack to track state
        Stack<Integer> stack = new Stack<>();
        stack.addAll(states);
        
        //run through the whole stack
        while (!stack.isEmpty()) {
        	//get the first element in the stack
            int currState = stack.pop();
            // Check if there are epsilon from the current state
            if (transFunction.get(currState).containsKey('ε')) {
                for (int nextState : transFunction.get(currState).get('ε')) {
                    // If nextState is not already in the closure set, add it and push to the stack
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }
        return closure;
    }
	
	//function created for testFA function
	public static String[] removeEle(String[] array, String element) {
		//if it is a empty array
        if (array == null || array.length == 0) {
            return array;
        }
        
        //count the wanted remove element in the array
        int count = 0;
        for (String s : array) {
            if (s.equals(element)) {
                count++;
            }
        }
        
        //if they dont find any
        if (count == 0) {
            return array;
        }

        //init a new array minus the wanted remove element
        String[] newArray = new String[array.length - count];
        int index = 0;
        
        //add all the other element into the new array
        for (String s : array) {
            if (!s.equals(element)) {
                newArray[index++] = s;
            }
        }

        return newArray;
    }
    
	//function for testing string
    public boolean testFA(Set<Character> alphabet,int numStates,int numTransitions,String[] eachTrans) {
    	//find total transition if it meet the characteristic of DFA
        boolean isNFA;
    	int DFAnumTrans=alphabet.size()*numStates;
    	
    	boolean flagOneOrMore=false;
    	boolean flagMissingTransition = false;
    	
    	ArrayList<Integer> allFromStateDFA=new ArrayList<>();
        ArrayList<Integer> allFromStateUser=new ArrayList<>();
        
        
        //find zero transition from the fromState
        //add  all states from user input to allFromStateDFA
        for(int j=0;j<numStates;j++) {
        	allFromStateDFA.add(j);
        	
        }
        //now select only the fromState which is the first index from the all entered transitions
        for(int i=0;i<eachTrans.length;i++) {
        	String[] temp = eachTrans[i].split("\\s+");
        	
            String fromState = temp[0];
            
            allFromStateUser.add(Integer.parseInt(fromState));
            
        }
        //compare if there any missing fromState if there is that mean there are states that have no transitions
        Set<Integer> set1=new HashSet<>(allFromStateDFA);
        Set<Integer> set2=new HashSet<>(allFromStateUser);
        System.out.println(allFromStateDFA);
        System.out.println(allFromStateUser);
        
        boolean flagZero=set1.equals(set2);
        
        //find fromState without all the alphabet
        Map<Integer, Set<Character>> stateTransitions = new HashMap<>();
        
        //get only the fromState and the symbol
        for (String trans : eachTrans) {
            String[] temp = trans.split("\\s+");
            int fromState = Integer.parseInt(temp[0]);
            char input = temp[1].charAt(0);

            stateTransitions.computeIfAbsent(fromState, k -> new HashSet<>()).add(input);
        }

        //illerate through to check if there are states that dont have all alphabet to do transition
        for (int state = 0; state < numStates; state++) {
            if (!stateTransitions.containsKey(state) || !stateTransitions.get(state).containsAll(alphabet)) {
                flagMissingTransition = true;
                break;
            }
        }
        

        //find one or more transition per input from same fromState
    	for (int i = 0; i < eachTrans.length; i++) {
            String[] temp = eachTrans[i].split("\\s+");

            //init selected transition info
            String fromState = temp[0];
            String input = temp[1];
            String toState = temp[2];

            //init the already selected transition
            String removeCurTrans = eachTrans[i];
            //init new array of transtion without the already selected
            String[] tempEachTrans = removeEle(eachTrans, removeCurTrans);

            //compare the selected transition to all other transitions to see if they has the both same fromState and input
            for (int j = 0; j < tempEachTrans.length; j++) {
                String[] oneEleTrans = tempEachTrans[j].split("\\s+");
                
                //if they found one that mean there are duplicated transition per input 
                if(fromState.equals(oneEleTrans[0]) && input.equals(oneEleTrans[1]) && !toState.equals(oneEleTrans[2])) {
                	flagOneOrMore=true;
                	break;
                }
            }
        }

    	//result 
    	if(alphabet.contains('ε')) {
    		System.out.println("This is a NFA!! because of epsilon");
                isNFA=true;
        }else if(numTransitions != DFAnumTrans){
    		System.out.println("This is a NFA!! because transition is not enough for DFA");
                isNFA=true;
        }else if(flagOneOrMore ){
    		System.out.println("This is a NFA!! because than one transition ");
                isNFA=true;
        }else if(!flagZero) {
    		System.out.println("This is a NFA because one or more of fromStates don't have a transition");
                isNFA=true;
        }else if(flagMissingTransition) {
        	System.out.println("This is a NFA because of missing transition of one or more symbol");
                isNFA=true;
        }
    	else {
    		System.out.println("This is a DFA");
                isNFA=false;
    	}
        return isNFA;
    }
    

}

