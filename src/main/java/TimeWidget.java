import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.util.concurrency.EdtExecutorService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeWidget extends JLabel implements CustomStatusBarWidget {
    private ScheduledFuture task;

    static String TIME_FORMAT = "HH:mm";
    SimpleDateFormat fmtM = new SimpleDateFormat(TIME_FORMAT);
    ArrayList<SimpleDateFormat> fmtP = new ArrayList<>();
    private JBPopupMenu popup;

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public @NotNull String ID() {
        return "TimeWidget";
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        initFmts();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() != 1) {
                    return;
                }
                final var chooser = new ZoneChooser(TimeWidget.this::setZone);
                TimeWidget.this.popup = new JBPopupMenu();
                TimeWidget.this.popup.add(chooser);
                TimeWidget.this.popup.show(TimeWidget.this, TimeWidget.this.getX(), TimeWidget.this.getY());
                TimeWidget.this.popup.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
                        TimeWidget.this.setZone(null);
                    }
                });
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
        this.task = EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(this::update, 0, 20, TimeUnit.SECONDS);
    }

    private void initFmts() {
        this.fmtM.setTimeZone(TimeZone.getTimeZone(Settings.get().selectedZone));
        fmtP.clear();
        for (var z : Settings.get().pinnedZones) {
            var fmt = new SimpleDateFormat(TIME_FORMAT);
            fmt.setTimeZone(TimeZone.getTimeZone(z));
            this.fmtP.add(fmt);
        }
    }

    Void setZone(String z) {
        this.popup.setVisible(false);
        this.popup = null;
        if (z != null) {
            Settings.get().selectedZone = z;
        }
        for (var w : TimeWidgetFactory.activeWidgets) {
            w.initFmts();
            w.update();
        }
        return null;
    }

    void update() {
        this.setText(" " + this.fmtM.getTimeZone().getID() + ": " + this.fmtM.format(new Date()) + " ");
        StringBuilder tooltip = new StringBuilder();
        for (var fmt : this.fmtP) {
            tooltip.append(fmt.getTimeZone().getID()).append(": ").append(fmt.format(new Date())).append("<br>");
        }
        this.setToolTipText(tooltip.toString());
    }

    @Override
    public void dispose() {
        if (this.task != null) {
            this.task.cancel(false);
            this.task = null;
        }
    }
}
