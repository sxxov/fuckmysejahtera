package design.sxxov.fuckmysejahtera.settings;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Item;

public class SettingsItem implements Item {
    @Nullable
    public boolean isNightMode;
    @Nullable
    public String name;
    @Nullable
    public String contact;
    @Nullable
    public boolean isHighRisk;


    public static final String IS_NIGHT_MODE_KEY = "isNightMode";
    public static final String NAME_KEY = "name";
    public static final String CONTACT_KEY = "contact";
    public static final String IS_HIGH_RISK = "isHighRisk";

    public Map<String, String> toMap() {
        return new HashMap<String, String>() {
            {
                put(IS_NIGHT_MODE_KEY, Boolean.toString(isNightMode));
                put(NAME_KEY, name);
                put(CONTACT_KEY, contact);
                put(IS_HIGH_RISK, Boolean.toString(isHighRisk));
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
