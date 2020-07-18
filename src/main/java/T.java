import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class T extends JPanel {
    T() {
        setPreferredSize(new Dimension(100, 100));
        addMouseWheelListener((e) -> {
            System.out.println(e);
        });
        setBackground(Color.GRAY);
    }
    public static void main(String[] args) {
        var f = new JFrame("T");
        f.setSize(100, 100);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(new T());
        f.setVisible(true);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        System.out.println(e);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        System.out.println(e);
    }
}
