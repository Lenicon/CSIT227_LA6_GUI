package NumberCounter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import java.awt.event.*;
import java.math.BigInteger;


public class NumberCounter extends JFrame {
    private JPanel panelMain;

    private JTextField countField;

    private JButton btnIncrease;
    private JButton btnDecrease;
    private JButton btnHelp;

    private Timer longPressDetectTimer;             // Detects if the hold crosses the threshold
    private Timer rapidRepeatTimer;                 // For continuous increment/decrement during a hold
    private final int LONG_PRESS_THRESHOLD = 600;   // Time to trigger continuous change (ms)
    private final int REPEAT_INTERVAL = 400;        // Interval for continuous change (ms)


    public NumberCounter(){

        createUIComponents();

        setContentPane(panelMain);
        setTitle("Number Counter (Len.icon)");
        pack();
        setMinimumSize(getSize());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // BUTTONS //
        btnHelp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(
                        null,
                        "1. Click - in/decrement by 1.\n2. Hold - in/decrement by 10.\n3. You can change the number directly.\n4. All invalid characters are automatically filtered.\n5. Virtually no integer limit.",
                        "Help / Features",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        // Press: +1; Hold: +10;
        setupCountListeners(btnIncrease, 1, 10);
        setupCountListeners(btnDecrease, -1, -10);

    }


    private void setupCountListeners(JButton button, int shortIncrement, int longIncrement){
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 1. Start the 'long press detection' timer
                longPressDetectTimer = new Timer(LONG_PRESS_THRESHOLD, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        // This fires ONCE after (LONG_PRESS_THRESHOLD) ms
                        longPressDetectTimer.stop();

                        // 2. Start the 'rapid repeat' timer for continuous change
                        rapidRepeatTimer = new Timer(REPEAT_INTERVAL, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                updateCount(longIncrement); // Fire every (REPEAT_INTERVAL) ms
                            }
                        });
                        rapidRepeatTimer.setInitialDelay(0); // Start immediately
                        rapidRepeatTimer.start();
                    }
                });
                longPressDetectTimer.setRepeats(false); // Only fires once
                longPressDetectTimer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean wasLongPress = false;

                // Stop the rapid repeat timer if it was running
                if (rapidRepeatTimer != null && rapidRepeatTimer.isRunning()) {
                    rapidRepeatTimer.stop();
                    wasLongPress = true; // A rapid timer implies a long press
                }

                // If the long press detection timer is still running, it was a short press.
                if (longPressDetectTimer != null && longPressDetectTimer.isRunning()) {
                    longPressDetectTimer.stop();
                    if (!wasLongPress) {
                        updateCount(shortIncrement); // Handle the short press
                    }
                }
            }
        });
    }

    private void updateCount(int n){
        BigInteger currentCount = new BigInteger(countField.getText().isBlank() ? "0":countField.getText());
        countField.setText(currentCount.add(BigInteger.valueOf(n)).toString());
    }

    private void createUIComponents() {
        if (countField != null){
            countField.setBorder(BorderFactory.createEmptyBorder());

            AbstractDocument doc = (AbstractDocument) countField.getDocument();
            doc.setDocumentFilter(new NumberFilter());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new NumberCounter());
    }
}
