import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Automata_Project extends JFrame {

    private JTextField statesField;
    private JTextField alphabetField;
    private JTextArea transitionsArea;
    private JTextField startStateField;
    private JTextField acceptStatesField;
    private DFA dfa;

    public Automata_Project() {
        setTitle("Finite Automaton App");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("AUTOMATION");
        titleLabel.setBounds(200, 10, 100, 25);
        add(titleLabel);

        JLabel statesLabel = new JLabel("States (comma-separated):");
        statesLabel.setBounds(10, 50, 200, 25);
        add(statesLabel);

        statesField = new JTextField();
        statesField.setBounds(220, 50, 250, 25);
        add(statesField);

        JLabel alphabetLabel = new JLabel("Alphabet (comma-separated):");
        alphabetLabel.setBounds(10, 80, 200, 25);
        add(alphabetLabel);

        alphabetField = new JTextField();
        alphabetField.setBounds(220, 80, 250, 25);
        add(alphabetField);

        JLabel transitionsLabel = new JLabel("Transitions (one per line):");
        transitionsLabel.setBounds(10, 110, 200, 25);
        add(transitionsLabel);

        transitionsArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(transitionsArea);
        scrollPane.setBounds(220, 110, 250, 80);
        add(scrollPane);

        JLabel startStateLabel = new JLabel("Initial State:");
        startStateLabel.setBounds(10, 200, 200, 25);
        add(startStateLabel);

        startStateField = new JTextField();
        startStateField.setBounds(220, 200, 250, 25);
        add(startStateField);

        JLabel acceptStatesLabel = new JLabel("Accepting States (comma-separated):");
        acceptStatesLabel.setBounds(10, 230, 250, 25);
        add(acceptStatesLabel);

        acceptStatesField = new JTextField();
        acceptStatesField.setBounds(220, 230, 250, 25);
        add(acceptStatesField);

        JButton designFAButton = new JButton("Design FA");
        designFAButton.setBounds(10, 270, 150, 25);
        add(designFAButton);

        JButton checkDeterministicButton = new JButton("Check Deterministic");
        checkDeterministicButton.setBounds(170, 270, 150, 25);
        add(checkDeterministicButton);

        JButton checkAcceptanceButton = new JButton("Check Acceptance");
        checkAcceptanceButton.setBounds(330, 270, 150, 25);
        add(checkAcceptanceButton);

        JButton convertNFAButton = new JButton("Convert DFA to NFA");
        convertNFAButton.setBounds(10, 300, 150, 25);
        add(convertNFAButton);

        JButton minimizeDFAButton = new JButton("Minimize DFA");
        minimizeDFAButton.setBounds(170, 300, 150, 25);
        add(minimizeDFAButton);

        JButton saveFAButton = new JButton("Save FA");
        saveFAButton.setBounds(330, 300, 150, 25);
        add(saveFAButton);

        designFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                designFA();
            }
        });

        checkDeterministicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkDeterministic();
            }
        });

        checkAcceptanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAcceptance();
            }
        });

        convertNFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertNFA();
            }
        });

        minimizeDFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minimizeDFA();
            }
        });

        saveFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFA();
            }
        });
    }

    private void designFA() {
        String statesText = statesField.getText();
        String startState = startStateField.getText();
        String acceptStatesText = acceptStatesField.getText();
        String transitionsText = transitionsArea.getText();

        Set<String> setOfStates = new HashSet<>(Arrays.asList(statesText.split(",")));
        Set<String> acceptStates = new HashSet<>(Arrays.asList(acceptStatesText.split(",")));

        List<String[]> transitions = new ArrayList<>();
        for (String line : transitionsText.split("\n")) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                transitions.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()});
            }
        }

        dfa = new DFA(setOfStates, startState, acceptStates, transitions);
        JOptionPane.showMessageDialog(this, "DFA designed successfully.");
    }

    private void checkDeterministic() {
        if (dfa == null) {
            JOptionPane.showMessageDialog(this, "Please design the DFA first.");
            return;
        }
        // Implement the logic to check if the DFA is deterministic.
        // For now, show a placeholder message.
        JOptionPane.showMessageDialog(this, "Check Deterministic feature is not implemented yet.");
    }

    private void checkAcceptance() {
        if (dfa == null) {
            JOptionPane.showMessageDialog(this, "Please design the DFA first.");
            return;
        }
        String inputString = JOptionPane.showInputDialog(this, "Enter the string to check:");
        boolean result = dfa.run(inputString);
        JOptionPane.showMessageDialog(this, "String is " + (result ? "accepted" : "not accepted") + " by the DFA.");
    }

    private void convertNFA() {
        if (dfa == null) {
            JOptionPane.showMessageDialog(this, "Please design the DFA first.");
            return;
        }
        // Implement the logic to convert DFA to NFA.
        // For now, show a placeholder message.
        JOptionPane.showMessageDialog(this, "Convert NFA feature is not implemented yet.");
    }

    private void minimizeDFA() {
        if (dfa == null) {
            JOptionPane.showMessageDialog(this, "Please design the DFA first.");
            return;
        }
        // Implement the logic to minimize the DFA.
        // For now, show a placeholder message.
        JOptionPane.showMessageDialog(this, "Minimize DFA feature is not implemented yet.");
    }

    private void saveFA() {
        if (dfa == null) {
            JOptionPane.showMessageDialog(this, "Please design the DFA first.");
            return;
        }
        // Implement the logic to save the DFA.
        // For now, show a placeholder message.
        JOptionPane.showMessageDialog(this, "Save FA feature is not implemented yet.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Automata_Project().setVisible(true);
            }
        });
    }
}
