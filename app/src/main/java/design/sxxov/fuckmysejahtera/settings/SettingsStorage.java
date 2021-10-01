package design.sxxov.fuckmysejahtera.settings;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SettingsStorage {
    private final SharedPreferences sharedPreferences;

    public SettingsStorage(Context ctx) {
        this.sharedPreferences = ctx
                .getSharedPreferences(
                        "settings",
                        Context.MODE_PRIVATE
                );
    }

    public SettingsItem get() {
        Map<String, ?> sharedPreferences = this.sharedPreferences.getAll();
        Map<String, String> stringSharedPreferences = new HashMap<>();

        for (Map.Entry<String, ?> entry : sharedPreferences.entrySet()) {
            if (entry.getValue() instanceof String) {
                stringSharedPreferences.put(entry.getKey(), (String) entry.getValue());
            }
        }

        return new SettingsItemFactory().fromMap(stringSharedPreferences);
    }

    public String get(String key) {
        return this.get(key, null);
    }

    public String get(String key, String defValue) {
        return this.sharedPreferences
                .getString(
                        key,
                        defValue
                );
    }

    public void set(String key, boolean value) {
        this.set(
                key,
                Boolean.toString(value)
        );
    }

    public void set(String key, int value) {
        this.set(
                key,
                Integer.toString(value)
        );
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(
                key,
                value
        );

        editor.apply();
    }

    public void set(SettingsItem settingsItem) {
        this.set(SettingsItem.IS_NIGHT_MODE_KEY, settingsItem.isNightMode);
        this.set(SettingsItem.NAME_KEY, settingsItem.name);
        this.set(SettingsItem.CONTACT_KEY, settingsItem.contact);
        this.set(SettingsItem.IS_HIGH_RISK, settingsItem.isHighRisk);
        this.set(SettingsItem.IS_VACCINATED, settingsItem.isVaccinated);
    }
}
