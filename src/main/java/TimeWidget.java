import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.concurrency.EdtExecutorService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeWidget extends JBTextArea implements CustomStatusBarWidget {
    private ScheduledFuture task;

    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
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
        registerDefaults();
        var _z = PropertiesComponent.getInstance().getValue("selectedZone");
        this.fmt.setTimeZone(TimeZone.getTimeZone(_z));
        this.setToolTipText(_z);
        this.setEditable(false);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() != 1) {
                    return;
                }
                final var chooser = new ZoneChooser(TimeWidget.this::setZone);
                TimeWidget.this.popup = new JBPopupMenu();
                TimeWidget.this.popup.add(chooser);
                TimeWidget.this.popup.show(TimeWidget.this, TimeWidget.this.getX(), TimeWidget.this.getY()-200);
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
        this.task = EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.SECONDS);
    }

    private void registerDefaults() {
        if (PropertiesComponent.getInstance().getValue("selectedZone") == null) {
            PropertiesComponent.getInstance().setValue("selectedZone", "UTC");
        }
        if (PropertiesComponent.getInstance().getValues("pinnedZones") == null) {
            PropertiesComponent.getInstance().setValues("pinnedZones", new String[]{"EST", "UTC"});
        }

    }

    Void setZone(String z) {
        var win = SwingUtilities.windowForComponent(this.popup.getComponent());
        win.dispose();

        this.popup = null;
        if (z != null) {
            PropertiesComponent.getInstance().setValue("selectedZone", z);
            this.fmt.setTimeZone(TimeZone.getTimeZone(z));
            this.setToolTipText(z);
        }
        this.update();
        SwingUtilities.invokeLater(() -> this.dispatchEvent(new MouseEvent(this, 12, 0, 0, 5, 5, 0, true, 1)));
        return null;
    }

    void update() {
        this.setText(this.fmt.format(new Date()));
    }

    @Override
    public void dispose() {
        if (this.task != null) {
            this.task.cancel(false);
            this.task = null;
        }
    }
}
