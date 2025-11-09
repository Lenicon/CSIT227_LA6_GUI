package NumberCounter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Document;

public class NumberFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        // Use the replace method logic, as insertion is a form of replacement
        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        if (text == null) {
            super.replace(fb, offset, length, null, attrs);
            return;
        }

        Document doc = fb.getDocument();
        String currentText = doc.getText(0, doc.getLength());
        String resultingText;

        // Construct the string that would result from this operation
        resultingText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        // Check for invalid characters (non-digits and non-hyphen)
        if (!resultingText.matches("^-?\\d*")) {
            return;
        }

        // If the resulting text is valid, apply the change
        super.replace(fb, offset, length, text, attrs);
    }
}
