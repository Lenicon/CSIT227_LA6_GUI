package SurveyProgressIndicator;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SurveyProgressIndicator extends JFrame{
    private JPanel panelMain;

    private JProgressBar progressBar;

    private JTextField emailField;
    private JTextField nameField;

    private final MutableBoolean q1Bool;
    private final ButtonGroup q1group;
    private JRadioButton q1r1;
    private JRadioButton q1r2;

    private JTextArea q2;

    private JButton clearButton;
    private JButton submitButton;


    private final int totalQuestions = 4;
    private final ArrayList<MutableBoolean> bools;
    
    
    public SurveyProgressIndicator(){
        createUIComponents();

        setContentPane(panelMain);
        setTitle("Character Counter (Len.icon)");
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                emailField.setText("");
                nameField.setText("");
                q1group.clearSelection();
                updateProgress(q1Bool, false);
                q2.setText("");
            }
        });


        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (progressBar.getValue() == progressBar.getMaximum()){
                    JOptionPane.showMessageDialog(null, "Thank you for filling up my survey!", "Submitted!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "The survey is incomplete. Please try again after you're done filling it up!", "Uh oh...", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // Questionnaire
        bools = new ArrayList<>();

        final MutableBoolean nameBool = new MutableBoolean();
        final MutableBoolean emailBool = new MutableBoolean();
        q1Bool = new MutableBoolean();
        final MutableBoolean q2Bool = new MutableBoolean();

        setupTextComponentListener(nameField, nameBool);
        setupTextComponentListener(emailField, emailBool);
        setupTextComponentListener(q2, q2Bool);


        q1group = setupRadioButtons(new JRadioButton[] {q1r1, q1r2}, q1Bool);

    }

    private ButtonGroup setupRadioButtons(JRadioButton[] radioButtons, MutableBoolean bool){
        ButtonGroup bgroup = new ButtonGroup();
        bools.add(bool);

        for (JRadioButton radioButton : radioButtons){
            bgroup.add(radioButton);
        }

        ActionListener groupListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check the logical status of the *entire* group
                ButtonModel selectedModel = bgroup.getSelection();

                updateProgress(bool, selectedModel != null);
            }
        };

        for (JRadioButton radioButton : radioButtons){
            radioButton.addActionListener(groupListener);
        }

        return bgroup;
    }



    private void setupTextComponentListener(JTextComponent textComponent, MutableBoolean bool){
        bools.add(bool);
        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!bool.isTrue()) updateProgress(bool, true);

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(textComponent.getText().isBlank() && bool.isTrue()) updateProgress(bool, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    private void createUIComponents() {
        if (progressBar != null){
            progressBar.setMaximum(totalQuestions);
        }
    }

    private void updateProgress(MutableBoolean bool, boolean boolValue){
        bool.setValue(boolValue);
        int totalValue = 0;
        for (MutableBoolean b: bools){
            totalValue += b.toInt();
        }
        progressBar.setValue(totalValue);
        progressBar.setString("Progress: "+((double)totalValue / (double)totalQuestions * 100.0)+"%");
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new SurveyProgressIndicator());
    }

}
