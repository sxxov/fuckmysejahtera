package design.sxxov.fuckmysejahtera.blocks.interfaces.common;

import org.json.JSONObject;

import java.util.Map;

public interface ItemFactory<T> {
    T fromMap(Map<String, String> map);

    T fromJSON(JSONObject jsonObject);

    T fromJSONString(String jsonString);
}
