package V2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Minimizer {
	
	// to eliminate all unreachable state
    private static Set<Integer> allReachableStates(DFA dfa) {
        //init reachState
    	Set<Integer> reachState=new HashSet<>();
    	//init set for search
    	Stack<Integer> search=new Stack<>();
    	
    	//init start state
    	int startS=dfa.getStartState();
    	search.push(startS);
    	reachState.add(startS);
    	
    	//run through the whole stack
    	while(!search.isEmpty()) {
    		int currState=search.pop();
    		
    		//run through all alphabet to check for unreachable
    		for(char ch: dfa.getAlphabet()) {
    			//find all next state of the alphabet we current run through 
    			int nextS=dfa.getTransitionFunction().get(currState).get(ch);
    			//find all next state of the currState to see if there are no state that can be reach
    			if(!reachState.contains(nextS)) {
    				//add to reachState
    				reachState.add(nextS);
    				//to ensures All States are Explored
    				search.push(nextS);
    			}
    		}
    		
    	}
    	
    	return reachState;
    }
    
    public DFA minimizeDFA(DFA dfa) {
    	//get reachable state
    	Set<Integer>reachableS= allReachableStates(dfa);
    	//newDfa state ,Maps old states to new states
    	Map<Integer, Integer> stateMapping = new HashMap<>();
    	
    	boolean partChanged;
    	
    	//init all final and normal statee
    	Set<Set<Integer>> partition = new HashSet<>();
        Set<Integer> finalS = dfa.getFinalStates();
        Set<Integer> nonFinalS = new HashSet<>(dfa.getStates());
        
        nonFinalS.removeAll(finalS);
        
        //add all state into partition
        if (!nonFinalS.isEmpty()) {
            partition.add(nonFinalS);
        }
        if (!finalS.isEmpty()) {
            partition.add(finalS);
        }
        
        //minimize partition
        do {
        	
        	partChanged = false;
        	//init new partition
            List<Set<Integer>> newPartition = new ArrayList<>();

            //run though all the element in partition
            for (Set<Integer> block : partition) {
            	
                // Create a list to hold new blocks
                List<Set<Integer>> transitionGroups = new ArrayList<>();
                List<Map<Character, Integer>> transitionKeys = new ArrayList<>();

                
                for (int state : block) {
                    Map<Character, Integer> transitionKey = new HashMap<>();
                    
                    //run through all alphabet
                    for (char symbol : dfa.getAlphabet()) {
                    	//init the toState of each alphabet
                        int toState = dfa.getTransitionFunction().get(state).get(symbol);
                        //check for all toState in the partiton
                        for (Set<Integer> p : partition) {
                        	//if true toStart belongs to the current blocks
                            if (p.contains(toState)) {
                            	
                                transitionKey.put(symbol, p.hashCode());  // Use partition hash as a unique identifier
                                break;
                            }
                        }
                    }
                    //init foundGroup to see if matching group has been found in transition key
                    boolean foundGroup = false;
                    for (int i = 0; i < transitionKeys.size(); i++) {
                        if (transitionKeys.get(i).equals(transitionKey)) {
                        	//add the state to the group of transition
                            transitionGroups.get(i).add(state);
                            foundGroup = true;
                            break;
                        }
                    }
                    
                    //this block is for if it doesnt find it
                    if (!foundGroup) {
                    	//init a new group then add the current state to the new group
                        Set<Integer> newGroup = new HashSet<>();
                        newGroup.add(state);
                        transitionGroups.add(newGroup);
                        transitionKeys.add(transitionKey);
                    }
                }
                //to see if there are still changes or not to let the loop keep running or stop
                if (transitionGroups.size() > 1) {
                	partChanged = true;
                	//add all the groups to the new partition set
                    newPartition.addAll(transitionGroups);
                } else {
                	//if there are only 1 group or new group were form add the current block to the newPartiton
                    newPartition.add(block);
                }
            }

            partition = new HashSet<>(newPartition);

        } while (partChanged);
        
        //creat all new states for minimized DFA
        int countState = 0;
        

        // Create state mappings for each block in the partition
        for (Set<Integer> block : partition) {
            for (int state : block) {
                stateMapping.put(state, countState);
            }
            countState++;
        }

        // Determine all state for the minimized DFA
        Set<Integer> newStates = new HashSet<>(stateMapping.values());
        Set<Integer> newFinalS = new HashSet<>();
        int newStartState = stateMapping.get(dfa.getStartState());

        for (int state : dfa.getFinalStates()) {
            newFinalS.add(stateMapping.get(state));
        }

        // Create the minimized DFA object
        DFA minimizedDFA = new DFA(newStates, dfa.getAlphabet(), newStartState, newFinalS);

        // Add all transitions to the new DFA
        for (Set<Integer> block : partition) {
            int fromState = stateMapping.get(block.iterator().next()); // Get the representative state of the block

            for (char symbol : dfa.getAlphabet()) {
                int toState = stateMapping.get(dfa.getTransitionFunction().get(block.iterator().next()).get(symbol));
                minimizedDFA.addTransition(fromState, symbol, toState);
            }
        }

        return minimizedDFA;

    	
    }
    
	
}
