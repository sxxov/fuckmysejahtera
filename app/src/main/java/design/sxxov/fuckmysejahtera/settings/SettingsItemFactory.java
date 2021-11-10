package design.sxxov.fuckmysejahtera.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.ItemFactory;

public class SettingsItemFactory implements ItemFactory<SettingsItem> {
    @Override
    public SettingsItem fromMap(Map<String, String> map) {
        return this.fromMap(map, null);
    }


    public SettingsItem fromMap(Map<String, String> map, @Nullable Context ctx) {
        return new SettingsItem() {
            {
                final boolean isSystemInNightMode = ctx != null
                        && (
                        ctx.getResources().getConfiguration().uiMode
                                & Configuration.UI_MODE_NIGHT_MASK
                )
                        == Configuration.UI_MODE_NIGHT_YES;
                final String isNightMode = map.get(SettingsItem.IS_NIGHT_MODE_KEY);
                final String isVaccinated = map.get(SettingsItem.IS_VACCINATED_KEY);
                final String isFirstRun = map.get(SettingsItem.IS_FIRST_RUN_KEY);

                this.isNightMode = isNightMode == null
                        ? isSystemInNightMode
                        : Boolean.parseBoolean(isNightMode);
                this.isVaccinated = isVaccinated == null || Boolean.parseBoolean(isVaccinated);
                this.isFirstRun = isFirstRun == null || Boolean.parseBoolean(isFirstRun);

                name = map.get(SettingsItem.NAME_KEY);
                contact = map.get(SettingsItem.CONTACT_KEY);
                isHighRisk = Boolean.parseBoolean(map.get(SettingsItem.IS_HIGH_RISK_KEY));
            }
        };
    }

    @Override
    public SettingsItem fromJSON(JSONObject jsonObject) {
        try {
            return new SettingsItem() {
                {
                    isFirstRun = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_FIRST_RUN_KEY)
                    );
                    isNightMode = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_NIGHT_MODE_KEY)
                    );
                    name = jsonObject.getString(SettingsItem.NAME_KEY);
                    contact = jsonObject.getString(SettingsItem.CONTACT_KEY);
                    isHighRisk = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_HIGH_RISK_KEY)
                    );
                    isVaccinated = Boolean.parseBoolean(
                            jsonObject.getString(SettingsItem.IS_VACCINATED_KEY)
                    );
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

    @Override
    public SettingsItem fromJSONString(String jsonString) throws JSONException {
        return this.fromJSON(new JSONObject(jsonString));
    }
}
