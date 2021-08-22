package design.sxxov.fuckmysejahtera.history;

public class HistoryItemFactory {
    public HistoryItem create(
            String location,
            String time
    ) {
        HistoryItem historyItem = new HistoryItem();

        historyItem.id = System.currentTimeMillis();
        historyItem.location = location;
        historyItem.time = time;

        return historyItem;
    }
}
