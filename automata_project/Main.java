package automation;

import automation.NFA_demo1;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create NFA as before...
        System.out.println("Enter number of states: ");
        int numStates = scanner.nextInt();
        Set<Integer> states = new HashSet<>();
        for (int i = 0; i < numStates; i++) {
            states.add(i);
        }

        System.out.println("Enter alphabet (characters separated by space, use 'e' for epsilon): ");
        scanner.nextLine(); 
        String alphabetString = scanner.nextLine();
        Set<Character> alphabet = new HashSet<>();
        for (String s : alphabetString.split(" ")) {
            char c = s.charAt(0);
            alphabet.add(c == 'e' ? 'ε' : c);
        }

        System.out.println("Enter start state: ");
        int startState = scanner.nextInt();

        System.out.println("Enter number of final states: ");
        int numFinalStates = scanner.nextInt();
        Set<Integer> finalStates = new HashSet<>();
        System.out.println("Enter final states: ");
        for (int i = 0; i < numFinalStates; i++) {
            finalStates.add(scanner.nextInt());
        }

        NFA_demo1 nfa = new NFA_demo1(states, alphabet, startState, finalStates);

        System.out.println("Enter transitions (fromState input toState): ");
        scanner.nextLine();
        String transInput = scanner.nextLine();
        String[] eachTrans = transInput.split(",");

        for (String transition : eachTrans) {
            String[] eachChar = transition.trim().split("\\s+");
            if (eachChar.length == 3) {
                try {
                    int fromState = Integer.parseInt(eachChar[0]);
                    char input = eachChar[1].charAt(0);
                    int toState = Integer.parseInt(eachChar[2]);
                    nfa.addTransition(fromState, input == 'e' ? 'ε' : input, toState);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in transition: " + transition);
                }
            } else {
                System.err.println("Invalid transition format: " + transition);
            }
        }

        // Convert NFA to DFA
        DFA dfa = Convertor.convertNFAtoDFA(nfa);

        // Test DFA
        String testInput;
        while (true) {
            System.out.println("Enter input string to test (or 'exit' to quit): ");
            testInput = scanner.next();
            if (testInput.equalsIgnoreCase("exit")) {
                break;
            }
            boolean isAccepted = dfa.isAccepting(testInput);
            if (isAccepted) {
                System.out.println("Input is accepted by the DFA.");
            } else {
                System.out.println("Input is rejected by the DFA.");
            }
        }

        scanner.close();
    }
}
