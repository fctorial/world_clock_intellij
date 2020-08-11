import mock.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;

public class ZoneChooserStandalone extends JPanel {
    final JList<String> pinned;
    final JList<String> zonesList;
    final JTextField stf;
    final Function<String, Void> cb;

    static final String[] timezones = TimeZone.getAvailableIDs();
    private final JScrollPane foC;
    private final JScrollPane pC;
    
    static int BORDER_WIDTH = 2;

    ZoneChooserStandalone(Function<String, Void> cb) {
        this.cb = cb;
        this.zonesList = new JList<>();
        this.zonesList.setListData(timezones);
        this.zonesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                var idx = zonesList.locationToIndex(ev.getPoint());
                var zone = zonesList.getModel().getElementAt(idx);
                if (zone != null) {
                    if (ev.getButton() == 1) {
                        ZoneChooserStandalone.this.cb.apply(zone);
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

        this.stf = new JTextField();
        this.stf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                filter();
            }

            void filter() {
                var qf = stf.getText().toLowerCase();
                if (qf.equals("")) {
                    zonesList.setListData(timezones);
                } else {
                    var qa = qf.trim().split(" ");
                    var results = new ArrayList<String>();
                    for (var tz : timezones) {
                        var accept = true;
                        for (var q : qa) {
                            accept = accept && tz.toLowerCase().contains(q);
                        }
                        if (accept) {
                            results.add(tz);
                        }
                    }
                    zonesList.setListData(listToArray(results));
                }
            }
        });
        stf.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent ev) {
                var to_select = 0;
                var curr = zonesList.getSelectedIndex();
                var key = ev.getKeyCode();
                if (key == KeyEvent.VK_UP) {
                    to_select = curr-1;
                } else if (key == KeyEvent.VK_DOWN) {
                    to_select = curr+1;
                } else if (key == KeyEvent.VK_ENTER) {
                    if (curr != -1) {
                        cb.apply(zonesList.getSelectedValue());
                    }
                    return;
                } else {
                    return;
                }
                zonesList.setSelectedIndex(to_select);
            }
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });
        stf.setPreferredSize(new Dimension(200, 30));

        this.pinned = new JList<>();
        this.pinned.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                var idx = pinned.locationToIndex(ev.getPoint());
                var zone = pinned.getModel().getElementAt(idx);
                if (zone != null)
                    if (ev.getButton() == 1) {
                        ZoneChooserStandalone.this.cb.apply(zone);
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

        this.setLayout(null);
        this.foC = new JScrollPane(zonesList);
        this.foC.setPreferredSize(new Dimension(200, 250));
        add(this.foC);

        add(this.stf);

        add(new JLabel("Pinned: "));

        this.pC = new JScrollPane(this.pinned);
        this.pC.setPreferredSize(new Dimension(200, 100));
        add(this.pC);

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
        final var w = new JFrame("demo");
        final var b = new JButton("Click");
        b.setPreferredSize(new Dimension(100, 30));
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                final var chooser = new ZoneChooserStandalone((s) -> {
                    System.out.println(s);
                    return null;
                });
                var popup = new JPopupMenu();
                popup.add(chooser);
                popup.show(w, 200, 100);
                SwingUtilities.invokeLater(chooser.stf::requestFocusInWindow);
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
        w.add(b);
        w.pack();
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setVisible(true);
    }
}

