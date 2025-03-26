package fib;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;

public class FibGUI extends JFrame {
    private FibManager fm = FibManager.getInstance();
    private JComboBox<FibBasedSequence> compareLeft;
    private JComboBox<FibBasedSequence> compareRight;

    private FibBasedSequence leftSeq;
    private FibBasedSequence rightSeq;

    private JTextArea topText;
    private JTextArea bottomText;
    private JTextArea comparisonText;

    private JSlider numValues;

    public FibGUI() {
        // FIBMANAGER CONFIG
        leftSeq = fm.add("Fib", 1, 1);
        rightSeq = fm.add("Lucas", 2, 1);

        setTitle("Fibbionaci Creator");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // TOP CONTROLS
        JPanel topPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        compareLeft = new JComboBox<>(new FibBasedSequence[]{leftSeq, rightSeq});
        compareRight = new JComboBox<>(new FibBasedSequence[]{leftSeq, rightSeq});

        compareLeft.setSelectedItem(leftSeq);
        compareRight.setSelectedItem(rightSeq);

        numValues = new JSlider(1, 100);
        JButton addButton = new JButton("Add Sequence");

        topPanel.add(compareLeft);
        topPanel.add(compareRight);
        topPanel.add(numValues);
        topPanel.add(addButton);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TEXT AREAS
        topText = createStyledTextArea();
        bottomText = createStyledTextArea();
        comparisonText = createStyledTextArea();

        // MIDDLE PANEL (3 rows)
        JPanel midPanel = new JPanel(new GridLayout(3, 1));

        JPanel seq1Panel = new JPanel(new BorderLayout());
        seq1Panel.add(topText, BorderLayout.CENTER);

        JPanel seq2Panel = new JPanel(new BorderLayout());
        seq2Panel.add(bottomText, BorderLayout.CENTER);

        JPanel compPanel = new JPanel(new BorderLayout());
        compPanel.add(comparisonText, BorderLayout.CENTER);

        midPanel.add(seq1Panel);
        midPanel.add(seq2Panel);
        midPanel.add(compPanel);

        // BOTTOM RENDER BUTTONS
        JButton renderLeft = new JButton("Render " + leftSeq);
        JButton renderRight = new JButton("Render " + rightSeq);
        JButton renderComp = new JButton("Render comparison");

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomButtonPanel.add(renderLeft);
        bottomButtonPanel.add(renderRight);
        bottomButtonPanel.add(renderComp);

        // COMPONENT LOGIC
        numValues.addChangeListener(e -> {
            FibManager.setNumElements(numValues.getValue());
            leftSeq.regenerate();
            rightSeq.regenerate();
            updateSliderChoice(topText, leftSeq, renderLeft);
            updateSliderChoice(bottomText, rightSeq, renderRight);
        });

        compareLeft.addActionListener(e -> {
            leftSeq = (FibBasedSequence) compareLeft.getSelectedItem();
            updateSliderChoice(topText, leftSeq, renderLeft);
        });

        compareRight.addActionListener(e -> {
            rightSeq = (FibBasedSequence) compareRight.getSelectedItem();
            updateSliderChoice(bottomText, rightSeq, renderRight);
        });

        addButton.addActionListener(e -> {
            JTextField nameField = new JTextField(10);
            JTextField firstTermField = new JTextField(5);
            JTextField secondTermField = new JTextField(5);

            JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            dialogPanel.add(new JLabel("Name:"));
            dialogPanel.add(nameField);
            dialogPanel.add(new JLabel("First Term:"));
            dialogPanel.add(firstTermField);
            dialogPanel.add(new JLabel("Second Term:"));
            dialogPanel.add(secondTermField);

            int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add New Sequence", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    int first = Integer.parseInt(firstTermField.getText().trim());
                    int second = Integer.parseInt(secondTermField.getText().trim());
                    FibBasedSequence seq = name.isEmpty() ? fm.add(first, second) : fm.add(name, first, second);
                    compareLeft.addItem(seq);
                    compareRight.addItem(seq);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "First/second terms must be integers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // renderLeft.addActionListener(e -> {
        //     SceneRenderer.run(
        //         List.of(leftSeq)
        //     );
        // });
        
        // renderRight.addActionListener(e -> {
        //     SceneRenderer.run(
        //         List.of(rightSeq)
        //     );
        // });

        // renderComp.addActionListener(e -> {
        //     SceneRenderer.run(
        //         List.of(leftSeq, rightSeq)
        //     );
        // });

        renderLeft.addActionListener(e -> runSceneWithSequences(List.of(leftSeq)));
        renderRight.addActionListener(e -> runSceneWithSequences(List.of(rightSeq)));
        renderComp.addActionListener(e -> runSceneWithSequences(List.of(leftSeq, rightSeq)));

        // ADD TO FRAME
        add(topPanel, BorderLayout.PAGE_START);
        add(midPanel, BorderLayout.CENTER);
        add(bottomButtonPanel, BorderLayout.PAGE_END);

        updateSliderChoice(topText, leftSeq, renderLeft);
        updateSliderChoice(bottomText, rightSeq, renderRight);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("SansSerif", Font.PLAIN, 18));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return area;
    }

    private void updateSliderChoice(JTextArea update, FibBasedSequence selected, JButton button) {
        update.setText("Sequence: " + selected + "\n" + selected.getSequence());
        comparisonText.setText("Comparing " + leftSeq + " and " + rightSeq +
                "\n" + FibManager.generateComparison(leftSeq, rightSeq));
        button.setText("Render " + selected);
    }

    private void runSceneWithSequences(List<FibBasedSequence> sequences) {
        try {
            String classpath = System.getProperty("java.class.path");
            String[] args = sequences.stream().map(FibBasedSequence::serialize).toArray(String[]::new);
    
            String[] command = new String[4 + args.length];
            command[0] = "java";
            command[1] = "-cp";
            command[2] = classpath;
            command[3] = "fib.SceneRenderer";
            System.arraycopy(args, 0, command, 4, args.length);
    
            new ProcessBuilder(command)
                .inheritIO()
                .start();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to launch render scene.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
