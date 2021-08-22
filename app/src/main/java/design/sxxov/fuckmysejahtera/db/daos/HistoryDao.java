package design.sxxov.fuckmysejahtera.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import design.sxxov.fuckmysejahtera.history.HistoryHTML;
import design.sxxov.fuckmysejahtera.history.HistoryItem;

@Dao
public interface HistoryDao {
    @Query("select * from HistoryItem order by id desc")
    List<HistoryItem> getItems();

    @Query("select * from HistoryHTML order by id desc")
    List<HistoryHTML> getImages();

    @Query("select * from HistoryItem where id = :id")
    List<HistoryItem> getItemById(long id);

    @Query("select * from HistoryHTML where id = :id")
    List<HistoryHTML> getHTMLById(long id);

    @Update
    void update(HistoryItem historyItem);

    @Insert
    void insertItems(HistoryItem... historyItems);

    @Insert
    void insertHTMLs(HistoryHTML... historyHTMLs);

    @Delete
    void deleteItem(HistoryItem historyItem);

    @Delete
    void deleteHTML(HistoryHTML historyHTML);

    @Query("delete from HistoryItem where id = :id")
    void deleteItemById(long id);

    @Query("delete from HistoryHTML where id = :id")
    void deleteHTMLById(long id);
}
