import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class T extends JPanel {

    T(Runnable r) {
        var b = new JButton(String.valueOf(new Random().nextInt()));
        b.setPreferredSize(new Dimension(100, 30));
        add(b);
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                r.run();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalized");
        super.finalize();
    }

    static JPopupMenu popup;

    static void showPopup(JFrame frame) {
        popup = new JPopupMenu();
        popup.add(new T(() -> {}));
        popup.show(frame, 0 ,0);
    }

    static void hidePopup() {
        popup.setVisible(false);
        popup = null;
        System.gc();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.gc();
            }
        }).start();
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.GRAY);

        var b = new JButton("O");
        b.setPreferredSize(new Dimension(100, 100));
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                showPopup(frame);
                EdtExecutorService2.getScheduledExecutorInstance().schedule(T::hidePopup, 3000, null);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        frame.add(b);

        frame.pack();
        frame.setVisible(true);
    }
}
