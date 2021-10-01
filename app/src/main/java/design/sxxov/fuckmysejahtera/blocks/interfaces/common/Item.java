package design.sxxov.fuckmysejahtera.blocks.interfaces.common;

import org.json.JSONObject;

import java.util.Map;

public interface Item {
    Map<String, String> toMap();

    default JSONObject toJSON() {
        return new JSONObject(this.toMap());
    }

    default String toJSONString() {
        return this.toJSON().toString();
    }
}
