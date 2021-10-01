package design.sxxov.fuckmysejahtera.receipt;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Item;

public class ReceiptItem implements Item {
    public static final String NAME_KEY = "name";
    public static final String CONTACT_KEY = "contact";
    public static final String USER_STATUS_KEY = "userStatus";
    public static final String VAC_STATUS_KEY = "vacStatus";

    @Nullable
    public String name;
    @Nullable
    public String contact;
    @Nullable
    public String userStatus;
    @Nullable
    public String vacStatus;

    @Override
    public Map<String, String> toMap() {
        return new HashMap<>() {
            {
                put(NAME_KEY, name);
                put(CONTACT_KEY, contact);
                put(USER_STATUS_KEY, userStatus);
                put(VAC_STATUS_KEY, vacStatus);
            }
        };
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public String toJSONString() {
        return null;
    }
}
