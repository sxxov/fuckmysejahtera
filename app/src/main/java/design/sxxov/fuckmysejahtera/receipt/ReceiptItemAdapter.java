package design.sxxov.fuckmysejahtera.receipt;

import design.sxxov.fuckmysejahtera.settings.SettingsItem;

public class ReceiptItemAdapter {
    public static ReceiptItem adaptSettingsItem(SettingsItem settingsItem) {
        return new ReceiptItem() {
            {
                name = settingsItem.name;
                contact = settingsItem.contact;
                userStatus = settingsItem.isHighRisk ? "HIGH" : "LOW";
                vacStatus = settingsItem.isVaccinated ? "Fully Vaccinated" : "NA";
            }
        };
    }
}
