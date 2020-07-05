package sidebar;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class TWF implements ToolWindowFactory {
    public static final NotificationGroup GROUP_DISPLAY_ID_INFO =
            new NotificationGroup("My notification group",
                    NotificationDisplayType.BALLOON, true);

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return true;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow win) {
        var c = win.getComponent();
        var not = GROUP_DISPLAY_ID_INFO.createNotification(c.getClass().getName(), NotificationType.ERROR);
        Notifications.Bus.notify(not, project);
    }
}
