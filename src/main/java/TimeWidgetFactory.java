import com.intellij.ide.lightEdit.LightEditCompatible;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class TimeWidgetFactory implements StatusBarWidgetFactory, LightEditCompatible {
    @Override
    public @NotNull String getId() {
        return "TimeWidget";
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return "time widget";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new TimeWidget();
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }

}
