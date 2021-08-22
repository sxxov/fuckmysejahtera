package design.sxxov.fuckmysejahtera.settings;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.ItemFactory;

public class SettingsItemFactory implements ItemFactory<SettingsItem> {
    public SettingsItem create(
            boolean nightMode,
            String name,
            String contact,
            boolean isHighRisk
    ) {
        SettingsItem settingsItem = new SettingsItem();

        settingsItem.isNightMode = nightMode;
        settingsItem.name = name;
        settingsItem.contact = contact;
        settingsItem.isHighRisk = isHighRisk;

        return settingsItem;
    }

    public SettingsItem fromMap(Map<String, String> map) {
        return this.create(
                Boolean.parseBoolean(map.get(SettingsItem.IS_NIGHT_MODE_KEY)),
                map.get(SettingsItem.NAME_KEY),
                map.get(SettingsItem.CONTACT_KEY),
                Boolean.parseBoolean(map.get(SettingsItem.IS_HIGH_RISK))
        );
    }

    public SettingsItem fromJSON(JSONObject jsonObject) {
        try {
            return this.create(
                    Boolean.parseBoolean(jsonObject.getString(SettingsItem.IS_NIGHT_MODE_KEY)),
                    jsonObject.getString(SettingsItem.NAME_KEY),
                    jsonObject.getString(SettingsItem.CONTACT_KEY),
                    Boolean.parseBoolean(jsonObject.getString(SettingsItem.IS_HIGH_RISK))
            );
        } catch (JSONException jsonException) {
            Log.e(
                    this.getClass().getSimpleName(),
                    jsonException.getLocalizedMessage(),
                    jsonException
            );

            throw new IllegalArgumentException(jsonException.getLocalizedMessage());
        }
    }

    public SettingsItem fromJSONString(String jsonString) {
        JSONObject jsonSettingsItem;

        try {
            jsonSettingsItem = new JSONObject(jsonString);
        } catch (JSONException jsonException) {
            // this will cause 'fromJSON' to throw an error, so this doesn't handle
            jsonSettingsItem = new JSONObject();
        }

        return this.fromJSON(jsonSettingsItem);
    }
}
