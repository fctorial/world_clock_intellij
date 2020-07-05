import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class OA extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent ev) {
        var p = ev.getProject();
        var fl = VF.instance;
        var fem = FileEditorManager.getInstance(p);
        var editors = fem.openEditor(new OpenFileDescriptor(p, fl, 0), true);
//        fem.setSelectedEditor(fl, "FILE_EDITOR");
    }
}
