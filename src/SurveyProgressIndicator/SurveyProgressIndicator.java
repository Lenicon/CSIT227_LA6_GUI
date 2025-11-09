package SurveyProgressIndicator;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.ArrayList;

public class SurveyProgressIndicator extends JFrame{
    private JPanel panelMain;

    private JProgressBar progressBar;

    private JTextField nameField;
    private JTextField dobField;
    private JTextField emailField;
    private JTextField contactField;

    private final MutableBoolean genderBool;
    private final ButtonGroup genderGroup;
    private JRadioButton genderR1;
    private JRadioButton genderR2;
    private JRadioButton genderR3;

    private JButton clearButton;
    private JButton submitButton;

    private int TOTAL_FIELDS = 0;
    private final ArrayList<MutableBoolean> bools;
    
    
    public SurveyProgressIndicator(){

        setContentPane(panelMain);
        setTitle("Survey Progress Indicator (Len.icon)");
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                nameField.setText("");
                dobField.setText("");
                contactField.setText("");
                emailField.setText("");
                genderGroup.clearSelection();
                updateProgress(genderBool, false);
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
        final MutableBoolean dobBool = new MutableBoolean();
        final MutableBoolean contactBool = new MutableBoolean();
        final MutableBoolean emailBool = new MutableBoolean();
        genderBool = new MutableBoolean();

        setupTextComponentListener(nameField, nameBool);
        setupTextComponentListener(dobField, dobBool);
        setupTextComponentListener(contactField, contactBool);
        setupTextComponentListener(emailField, emailBool);

        genderGroup = setupRadioButtons(new JRadioButton[] {genderR1, genderR2, genderR3}, genderBool);


        progressBar.setMaximum(TOTAL_FIELDS);
    }

    private ButtonGroup setupRadioButtons(JRadioButton[] radioButtons, MutableBoolean bool){
        ButtonGroup bgroup = new ButtonGroup();
        bools.add(bool);
        TOTAL_FIELDS += 1;

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
        TOTAL_FIELDS += 1;

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
            public void changedUpdate(DocumentEvent e) {}
        });
    }


    private void updateProgress(MutableBoolean bool, boolean boolValue){
        bool.setValue(boolValue);
        int totalValue = 0;
        for (MutableBoolean b: bools){
            totalValue += b.toInt();
        }
        progressBar.setValue(totalValue);
        progressBar.setString("Progress: "+((double)totalValue / (double)TOTAL_FIELDS * 100.0)+"%");
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new SurveyProgressIndicator());
    }

}
