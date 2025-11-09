package CharacterCounter;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.util.regex.Pattern;

public class CharacterCounter extends JFrame {
    private JTextArea textArea;
    private JPanel panelMain;

    private JTextField specificField;
    private JLabel specificCountNum;
    private JButton btnCaseToggle;

    private JLabel wordCountNum;
    private JLabel lineCountNum;
    private JLabel charCountNum;




    public CharacterCounter(){

        if(specificField != null) specificField.setBorder(BorderFactory.createEmptyBorder());

        setContentPane(panelMain);
        setTitle("Character Counter (Len.icon)");
//        pack();
        setSize(600, 400);
        setMinimumSize(getSize());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add a DocumentListener to update the count in real-time
        createUpdateCounters(textArea);
        createUpdateCounters(specificField);
    }

    private void createUpdateCounters(JTextComponent tc){
        tc.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateCount(); }

            @Override
            public void removeUpdate(DocumentEvent e) { updateCount(); }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void updateCount(){
        String text = textArea.getText();

        long sc = 0;
        if (!specificField.getText().isBlank()){
            Pattern pattern = Pattern.compile(specificField.getText());
            sc = pattern.matcher(text).results().count();
        }

        specificCountNum.setText(sc+"");
        charCountNum.setText(text.length()+"");
        wordCountNum.setText(text.split("\\s+").length+"");
        lineCountNum.setText(text.lines().count()+"");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new CharacterCounter());
    }
}
