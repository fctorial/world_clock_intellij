import com.intellij.ui.JBColor;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.concurrency.EdtExecutorService;
//import mock.EdtExecutorService;
//import mock.Settings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ZoneChooser extends JPanel {
    final JList<String> pinned;
    final JList<String> filteredOpts;
    final Function<String, Void> cb;

    static final String[] timezones = TimeZone.getAvailableIDs();
    private final JScrollPane foC;
    private final JScrollPane pC;

    private ScheduledFuture<?> searchTask;
//    private Thread searchTask;

    ZoneChooser(Function<String, Void> cb) {
        this.cb = cb;
        this.filteredOpts = new JBList<>();
        this.filteredOpts.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ev) {
                if (ev.getKeyChar() == '\n') {
                    cb.apply(filteredOpts.getSelectedValue());
                }
            }
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });
        this.filteredOpts.setListData(timezones);
        this.filteredOpts.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent ev) {
                var idx = filteredOpts.locationToIndex(ev.getPoint());
                var zone = filteredOpts.getModel().getElementAt(idx);
                if (zone != null) {
                    if (ev.getButton() == 1) {
                        ZoneChooser.this.cb.apply(zone);
                    } else {
                        if (!aContains(Settings.get().pinnedZones, zone)) {
                            Settings.get().pinnedZones.add(zone);
                            pinned.setListData(listToArray(Settings.get().pinnedZones));
                            doLayout2();
                        }
                    }
                }
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
        filteredOpts.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {}

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != filteredOpts) {
                    filteredOpts.requestFocusInWindow();
                    filteredOpts.setBorder(BorderFactory.createLineBorder(JBColor.border()));
                    pinned.clearSelection();
                    pinned.setBorder(null);
                }
            }
        });
        new ListSpeedSearch<>(this.filteredOpts);

        this.pinned = new JBList<>();
        this.pinned.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                var idx = pinned.locationToIndex(ev.getPoint());
                var zone = pinned.getModel().getElementAt(idx);
                if (zone != null)
                    if (ev.getButton() == 1) {
                        ZoneChooser.this.cb.apply(zone);
                    } else {
                        if (aContains(Settings.get().pinnedZones, zone)) {
                            Settings.get().pinnedZones.remove(zone);
                            pinned.setListData(listToArray(Settings.get().pinnedZones));
                            doLayout2();
                        }
                    }
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
        pinned.setListData(listToArray(Settings.get().pinnedZones));
        pinned.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {}

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != pinned) {
                    pinned.requestFocusInWindow();
                    pinned.setBorder(BorderFactory.createLineBorder(JBColor.border()));
                    filteredOpts.clearSelection();
                    filteredOpts.setBorder(null);
                }
            }
        });
        pinned.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ev) {
                if (ev.getKeyChar() == '\n') {
                    cb.apply(pinned.getSelectedValue());
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {}

            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });
        new ListSpeedSearch<>(this.pinned);

        this.setLayout(null);
        this.foC = new JBScrollPane(filteredOpts);
//        this.foC = new JScrollPane(filteredOpts);
        this.foC.setPreferredSize(new Dimension(200, 250));
        add(this.foC);
        add(new JBLabel("Pinned: "));
        this.pC = new JBScrollPane(this.pinned);
//        this.pC = new JScrollPane(this.pinned);
        this.pC.setPreferredSize(new Dimension(200, 100));
        add(this.pC);

        this.filteredOpts.addMouseWheelListener((e) -> {
            this.foC.dispatchEvent(e);
        });
        doLayout2();
    }

    static boolean aContains(List<String> arr, String zone) {
        for (var e : arr) {
            if (e.equals(zone)) {
                return true;
            }
        }
        return false;
    }

    static boolean inOrder(int... nums) {
        var idx = 0;
        while (idx < nums.length - 1) {
            if (nums[idx] > nums[idx + 1]) {
                return false;
            }
            idx++;
        }
        return true;
    }

    static String[] listToArray(List<String> l) {
        var res = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            res[i] = l.get(i);
        }
        return res;
    }

    static int PADDING = 5;

    void doLayout2() {
        var cs = getComponents();
        var height = 2 * PADDING;
        var width = -1;
        for (var c : cs) {
            var sz = c.getPreferredSize();
            height += sz.height;
            width = Math.max(sz.width, width);
        }
        width += 2 * PADDING;
        setPreferredSize(new Dimension(width, height));
        if (this.getWin() != null) {
            this.getWin().setSize(new Dimension(width, height));
        }
        var y = PADDING;
        for (var c : cs) {
            var sz = c.getPreferredSize();
            c.setBounds(PADDING, y, sz.width, sz.height);
            y += sz.height;
        }
    }

    Component getWin() {
        Component p = this;
        while (p != null) {
            if (p.getClass().getName().equals("javax.swing.Popup$HeavyWeightWindow")) {
                return p;
            }
            p = p.getParent();
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setBackground(Color.GRAY);

        var popup = new JPopupMenu();

        popup.add(new ZoneChooser((s) -> {
            System.out.println(s);
            return null;
        }));

        frame.pack();
        frame.setVisible(true);
        popup.show(frame, 300, 400);
        popup.getParent().setSize(popup.getPreferredSize());
    }

}

