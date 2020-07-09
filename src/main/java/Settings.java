import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

@State(name = "ActivityManager", storages = @Storage(value = "Clock.xml"))
public class Settings implements PersistentStateComponent<Settings> {
    public String selectedZone = "UTC";

    public ArrayList<String> pinnedZones = new ArrayList<>();
    {
        Collections.addAll(pinnedZones, "EST", "IST");
    }

    @Nullable
    @Override
    public Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings o) {
        this.selectedZone = o.selectedZone;
        this.pinnedZones = o.pinnedZones;
    }

    static Settings get() {
        return ServiceManager.getService(Settings.class);
    }
}
