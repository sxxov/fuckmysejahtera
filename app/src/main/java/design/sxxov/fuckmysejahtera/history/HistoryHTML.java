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
    @PrimaryKey
    public long id;

    public static final String HTML_KEY = "html";

    @ColumnInfo(name = HTML_KEY)
    public String html;


    public Map<String, String> toMap() {
        return new HashMap<String, String>() {
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
