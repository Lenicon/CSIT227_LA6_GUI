package CharacterCounter;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;


public class CharacterCounter extends JFrame {
    private JTextArea textArea;
    private JPanel panelMain;

    private JLabel wordCountNum;

    private JLabel lineCountNum;

    private JLabel charCountNum;

    public CharacterCounter(){

        setContentPane(panelMain);
        setTitle("Character Counter (Len.icon)");
        pack();
        setMinimumSize(getSize());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add a DocumentListener to update the count in real-time
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });
    }

    private void updateCount(){
        String text = textArea.getText();

        charCountNum.setText(text.length()+"");
        wordCountNum.setText(text.split("\\s+").length+"");
        lineCountNum.setText(text.lines().count()+"");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new CharacterCounter());
    }
}
