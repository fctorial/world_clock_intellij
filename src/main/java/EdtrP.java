import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class EdtrP implements FileEditorProvider {
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return virtualFile instanceof VF;//true; //virtualFile.getName().equals("FILE");
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return new Edtr();
    }

    @Override
    public @NotNull String getEditorTypeId() {
        return "FILE_EDITOR";
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
