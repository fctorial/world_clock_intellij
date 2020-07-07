import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;

public class MJScrollPane extends JBScrollPane {
    public MJScrollPane(JList<String> filteredOpts) {
        super(filteredOpts);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        if (!isWheelScrollingEnabled()) {
            if (getParent() != null)
                getParent().dispatchEvent(
                        SwingUtilities.convertMouseEvent(this, e, getParent()));
            return;
        }
        super.processMouseWheelEvent(e);
    }

}
