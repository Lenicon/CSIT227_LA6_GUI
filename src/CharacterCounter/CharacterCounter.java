package CharacterCounter;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class CharacterCounter extends JFrame {
    private JTextArea textArea;
    private JPanel panelMain;

    private JTextField specificField;
    private JLabel specificCountNum;

    private final MutableBoolean isCaseSensitive;
    private JButton btnIsCaseSensitive;

    private JLabel wordCountNum;
    private JLabel lineCountNum;
    private JLabel charCountNum;




    public CharacterCounter(){

//        specificField.setMargin(new Insets(7,2,0,2));
        specificField.setBorder(new EmptyBorder(5, 10, 0, 10));
//        specificField.setBorder(BorderFactory.createEmptyBorder());

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

        // Add case sensitivity feature
        isCaseSensitive = new MutableBoolean(true);
        btnIsCaseSensitive.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (isCaseSensitive.isTrue()){
                    // Toggle to false
                    btnIsCaseSensitive.setBackground(new Color(19,19,24));
                    btnIsCaseSensitive.setForeground(new Color(255,255,255));
                    isCaseSensitive.setValue(false);
                } else {
                    // Toggle to false
                    btnIsCaseSensitive.setBackground(new Color(255,255,255));
                    btnIsCaseSensitive.setForeground(new Color(19,19,24));
                    isCaseSensitive.setValue(true);
                }

                updateCount();
            }
        });
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
            Pattern pattern = Pattern.compile(specificField.getText(), isCaseSensitive.isTrue() ? 0 : Pattern.CASE_INSENSITIVE);
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
