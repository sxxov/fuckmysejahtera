package design.sxxov.fuckmysejahtera.blocks.interfaces.common;

import org.json.JSONObject;

import java.util.Map;

public interface Item {
    Map<String, String> toMap();
    JSONObject toJSON();
    String toJSONString();
}
