package conclave.gui.components;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ScrollingText extends JTextPane {

    public ScrollingText(String text) {
        setText(text);
        setDefault();
        alignText();
    }

    public void place(int parentWidth, int parentHeight) {
        setLocation(
            (parentWidth - getWidth()) / 2 - 10,
            parentHeight
        );
    }

    public void scroll() {
        Timer timer = new Timer(30, e -> setLocation(getX(), getY() - 2));

        timer.start();
        repaint();
    }

    private void setDefault() {
        setSize(getPreferredSize());
        setBackground(null);
        setBorder(null);
        setEditable(false);
        setFocusable(false);
    }

    private void alignText() {
        StyledDocument doc = getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
}
