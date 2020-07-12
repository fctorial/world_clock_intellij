package mock;

import java.util.ArrayList;
import java.util.Collections;

public class Settings {
    static Settings mockSettings = new Settings();
    public String selectedZone = "UTC";

    public ArrayList<String> pinnedZones = new ArrayList<>();
    {
        Collections.addAll(pinnedZones, "EST", "IST");
    }

    public static Settings get() {
        return mockSettings;
    }
}
