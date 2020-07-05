import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VF extends VirtualFile {
    public static final VF instance = new VF();
    private VF() {}
    @Override
    public @NotNull String getName() {
        return "VF";
    }

    @Override
    public @NotNull VirtualFileSystem getFileSystem() {
        return VFS.instance;
//        return null;
    }

    @Override
    public @NotNull String getPath() {
        return "VF";
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public VirtualFile getParent() {
        return null;
    }

    @Override
    public VirtualFile[] getChildren() {
        return new VirtualFile[0];
    }

    @Override
    public @NotNull OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
        throw new IOException();
    }

    @Override
    public @NotNull byte[] contentsToByteArray() throws IOException {
        return new byte[0];
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public void refresh(boolean b, boolean b1, @Nullable Runnable runnable) {

    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
