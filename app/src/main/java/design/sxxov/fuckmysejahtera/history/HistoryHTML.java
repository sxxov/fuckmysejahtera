package design.sxxov.fuckmysejahtera.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Item;

@Entity
public class HistoryHTML implements Item {
    public static final String HTML_KEY = "html";

    @PrimaryKey
    public long id;
    @ColumnInfo(name = HTML_KEY)
    public String html;


    public Map<String, String> toMap() {
        return new HashMap<>() {
            {
                put(HTML_KEY, html);
            }
        };
    }

    public JSONObject toJSON() {
        return new JSONObject(this.toMap());
    }

    public String toJSONString() {
        return this.toJSON().toString();
    }
}
