import com.intellij.util.concurrency.EdtExecutorService;
//import mock.EdtExecutorService;
//import mock.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ZoneChooser extends JPanel {
    final JTextField tv;
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
        this.filteredOpts = new JList<>();
        this.filteredOpts.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent ev) {
                var idx = filteredOpts.locationToIndex(ev.getPoint());
                var zone = filteredOpts.getModel().getElementAt(idx);
                if (zone != null) {
                    if (ev.getButton() == 1) {
                        ZoneChooser.this.cb.apply(zone);
                    } else {
                        if (! aContains(Settings.get().pinnedZones, zone)) {
                            Settings.get().pinnedZones.add(zone);
                            pinned.setListData(listToArray(Settings.get().pinnedZones));
                            doLayout2();
                        }
                    }
                }
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

        this.tv = new JTextField();
        this.tv.setPreferredSize(new Dimension(200, 30));
        this.tv.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                f();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                f();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) { }

            void f() {
                if (ZoneChooser.this.searchTask != null) {
                    ZoneChooser.this.searchTask.cancel(true);
//                    ZoneChooser.this.searchTask.interrupt();

                    ZoneChooser.this.searchTask = null;

                }
                ZoneChooser.this.searchTask = EdtExecutorService.getScheduledExecutorInstance().schedule(() -> {
                    var qs = tv.getText().toLowerCase().split(" ");
                    var res = new ArrayList<String>();
                    for (String zone : timezones) {
                        var matches = true;
                        for (var q : qs) {
                            if (!zone.toLowerCase().contains(q)) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            res.add(zone);
                        }
                    }

                    filteredOpts.setListData(listToArray(res));
                    doLayout2();
                    ZoneChooser.this.searchTask = null;
                }, 300, TimeUnit.MILLISECONDS);
            }
        });

        this.pinned = new JList<>();
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
        pinned.setListData(listToArray(Settings.get().pinnedZones));

        this.setLayout(null);
//        this.foC = new JBScrollPane(filteredOpts);
        this.foC = new JScrollPane(filteredOpts);
        this.foC.setPreferredSize(new Dimension(200, 250));
        add(this.foC);
        add(this.tv);
        add(new JLabel("Pinned: "));
//        this.pC = new JBScrollPane(this.pinned);
        this.pC = new JScrollPane(this.pinned);
        this.pC.setPreferredSize(new Dimension(200, 100));
        add(this.pC);

        doLayout2();
    }

    private static boolean aContains(List<String> arr, String zone) {
        for (var e : arr) {
            if (e.equals(zone)) {
                return true;
            }
        }
        return false;
    }

    static String[] listToArray(List<String> l) {
        var res = new String[l.size()];
        for (int i=0; i<l.size(); i++) {
            res[i] = l.get(i);
        }
        return res;
    }

    static int PADDING = 5;
    void doLayout2() {
        var cs = getComponents();
        var height = 2*PADDING;
        var width = -1;
        for (var c : cs) {
            var sz = c.getPreferredSize();
            height += sz.height;
            width = Math.max(sz.width, width);
        }
        width += 2*PADDING;
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

