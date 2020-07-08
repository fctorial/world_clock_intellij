import org.jetbrains.io.JsonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class T extends JPanel {

    private final JTextField tf;
    private final JScrollPane sv;

    T() {
        this.tf = new JTextField();
        this.tf.setPreferredSize(new Dimension(100, 30));
        var lv = new JList<String>();
        lv.setListData(new String[] {"A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D"});
        this.sv = new JScrollPane(lv);
        add(this.sv);
        add(this.tf);
        setFocusable(true);
        addMouseWheelListener(new MouseAdapter() { // this will react to the scrolling
            public void mouseWheelMoved(MouseWheelEvent e) {
                sv.dispatchEvent(e);
            }
        });
        addKeyListener(new KeyAdapter() { // this reacts to the key strokes
            public void keyTyped(KeyEvent e) {
                System.out.println("KEY");
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    popup.setVisible(false);
                } else {
                    tf.dispatchEvent(e);
                }
            }
        });
    }

    static JPopupMenu popup;

    static void showPopup(JFrame frame) {
        popup = new JPopupMenu();
        popup.add(new T());
        popup.show(frame, 0 ,0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.GRAY);

        var b = new JButton("CLICK");
        b.setPreferredSize(new Dimension(100, 100));
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                showPopup(frame);
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {}
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}
            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        });
        frame.add(b);

        frame.pack();
        frame.setVisible(true);
    }
}
