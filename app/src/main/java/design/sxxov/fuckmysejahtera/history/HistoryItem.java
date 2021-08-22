package design.sxxov.fuckmysejahtera.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Item;

@Entity
public class HistoryItem implements Item {
    @PrimaryKey
    public long id;

    public static final String LOCATION_KEY = "location";
    public static final String TIME_KEY = "time";


    @ColumnInfo(name = LOCATION_KEY)
    public String location;
    @ColumnInfo(name = TIME_KEY)
    public String time;

    public Map<String, String> toMap() {
        return new HashMap<String, String>() {
            {
                put(LOCATION_KEY, location);
                put(TIME_KEY, time);
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
