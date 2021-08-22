package design.sxxov.fuckmysejahtera.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStorage {
    private Context ctx;
    private SharedPreferences sharedPreferences;

    public SettingsStorage(Context ctx) {
        this.ctx = ctx;
        this.sharedPreferences = this.ctx
                .getSharedPreferences(
                        "settings",
                        Context.MODE_PRIVATE
                );
    }

    public SettingsItem get() {
        SettingsItemFactory settingsItemFactory = new SettingsItemFactory();

        return settingsItemFactory.create(
                Boolean.parseBoolean(
                        this.get(SettingsItem.IS_NIGHT_MODE_KEY)
                ),
                this.get(SettingsItem.NAME_KEY),
                this.get(SettingsItem.CONTACT_KEY),
                Boolean.parseBoolean(
                        this.get(SettingsItem.IS_HIGH_RISK)
                )
        );
    }

    public String get(String key) {
        return this.sharedPreferences
                .getString(
                        key,
                        null
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
    }
}
