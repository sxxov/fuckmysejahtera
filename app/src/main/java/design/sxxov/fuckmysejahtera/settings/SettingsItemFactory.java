package design.sxxov.fuckmysejahtera.settings;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.ItemFactory;

public class SettingsItemFactory implements ItemFactory<SettingsItem> {
    public SettingsItem fromMap(Map<String, String> map) {
        return new SettingsItem() {
            {
                isNightMode = Boolean.parseBoolean(map.get(SettingsItem.IS_NIGHT_MODE_KEY));
                name = map.get(SettingsItem.NAME_KEY);
                contact = map.get(SettingsItem.CONTACT_KEY);
                isHighRisk = Boolean.parseBoolean(map.get(SettingsItem.IS_HIGH_RISK));
                isVaccinated = Boolean.parseBoolean(map.get(SettingsItem.IS_VACCINATED));

                this.applyDefaults();
            }
        };
    }

    public SettingsItem fromJSON(JSONObject jsonObject) {
        try {
            return new SettingsItem() {
                {
                    isNightMode = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_NIGHT_MODE_KEY)
                    );
                    name = jsonObject.getString(SettingsItem.NAME_KEY);
                    contact = jsonObject.getString(SettingsItem.CONTACT_KEY);
                    isHighRisk = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_HIGH_RISK)
                    );
                    isVaccinated = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_VACCINATED)
                    );

                    this.applyDefaults();
                }
            };
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
