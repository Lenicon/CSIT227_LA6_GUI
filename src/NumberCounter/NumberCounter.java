package NumberCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class NumberCounter extends JFrame {
    private JPanel panelMain;
    private JTextField countField;
    private JButton increase;
    private JButton decrease;

    private Timer longPressDetectTimer;             // Detects if the hold crosses the threshold
    private Timer rapidRepeatTimer;                 // For continuous increment/decrement during a hold
    private final int LONG_PRESS_THRESHOLD = 800;   // Time to trigger continuous change (ms)
    private final int REPEAT_INTERVAL = 400;        // Interval for continuous change (ms)

    private BigInteger currentCount;

    public NumberCounter(){

        createUIComponents();

        setContentPane(panelMain);
        setTitle("Number Counter (Len.icon)");
        pack();
        setMinimumSize(getSize());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Press: +1; Hold: +10;
        setupCountListeners(increase, 1, 10);
        setupCountListeners(decrease, -1, -10);

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
                                addCount(longIncrement); // Fire every (REPEAT_INTERVAL) ms
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
                        addCount(shortIncrement); // Handle the short press
                    }
                }
            }
        });
    }

    private void addCount(int n){
        currentCount = new BigInteger(countField.getText().isBlank() ? "0":countField.getText().replaceAll("[^\\d-]|-(?!\\d)", ""));
        countField.setText(currentCount.add(BigInteger.valueOf(n)).toString());
    }

    private void createUIComponents() {
        if (countField != null){
            countField.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater ( () -> new NumberCounter());
    }
}
