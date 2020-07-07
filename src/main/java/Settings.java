import com.intellij.ide.util.PropertiesComponent;

public class Settings {
    String get(String k) {
        return PropertiesComponent.getInstance().getValue(k);
    }
}
