package design.sxxov.fuckmysejahtera.settings;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Item;

public class SettingsItem implements Item {
    public static final String IS_NIGHT_MODE_KEY = "isNightMode";
    public static final String NAME_KEY = "name";
    public static final String CONTACT_KEY = "contact";
    public static final String IS_HIGH_RISK_KEY = "isHighRisk";
    public static final String IS_VACCINATED_KEY = "isVaccinated";

    public boolean isNightMode;
    @Nullable
    public String name;
    @Nullable
    public String contact;
    public boolean isHighRisk;
    public boolean isVaccinated;

    public Map<String, String> toMap() {
        return new HashMap<>() {
            {
                put(IS_NIGHT_MODE_KEY, Boolean.toString(isNightMode));
                put(NAME_KEY, name);
                put(CONTACT_KEY, contact);
                put(IS_HIGH_RISK_KEY, Boolean.toString(isHighRisk));
                put(IS_VACCINATED_KEY, Boolean.toString((isVaccinated)));
            }
        };
    }

    public JSONObject toJSON() {
        return new JSONObject(this.toMap());
    }

    public String toJSONString() {
        return this.toJSON().toString();
    }
}
