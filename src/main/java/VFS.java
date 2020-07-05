import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class VFS extends VirtualFileSystem {
    public static VFS instance = new VFS();

    @Override
    public @NotNull String getProtocol() {
        return "VFS";
    }

    @Override
    public @Nullable VirtualFile findFileByPath(@NotNull String path) {
        return VF.instance;
    }

    @Override
    public void refresh(boolean asynchronous) {}

    @Override
    public @Nullable VirtualFile refreshAndFindFileByPath(@NotNull String path) {
        return VF.instance;
    }

    @Override
    public void addVirtualFileListener(@NotNull VirtualFileListener listener) {}

    @Override
    public void removeVirtualFileListener(@NotNull VirtualFileListener listener) {}

    @Override
    protected void deleteFile(Object requestor, @NotNull VirtualFile vFile) throws IOException {}

    @Override
    protected void moveFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent) throws IOException {}

    @Override
    protected void renameFile(Object requestor, @NotNull VirtualFile vFile, @NotNull String newName) throws IOException {}

    @Override
    protected @NotNull VirtualFile createChildFile(Object requestor, @NotNull VirtualFile vDir, @NotNull String fileName) throws IOException {
        throw new IOException();
    }

    @Override
    protected @NotNull VirtualFile createChildDirectory(Object requestor, @NotNull VirtualFile vDir, @NotNull String dirName) throws IOException {
        throw new IOException();
    }

    @Override
    protected @NotNull VirtualFile copyFile(Object requestor, @NotNull VirtualFile virtualFile, @NotNull VirtualFile newParent, @NotNull String copyName) throws IOException {
        throw new IOException();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
