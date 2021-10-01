package design.sxxov.fuckmysejahtera.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import design.sxxov.fuckmysejahtera.db.daos.HistoryDao;
import design.sxxov.fuckmysejahtera.history.HistoryHTML;
import design.sxxov.fuckmysejahtera.history.HistoryItem;

@Database(entities = {HistoryItem.class, HistoryHTML.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();
}
