package design.sxxov.fuckmysejahtera.history;

public class HistoryHTMLFactory {
    public HistoryHTML create(
            long id,
            String html
    ) {
        HistoryHTML historyHTML = new HistoryHTML();

        historyHTML.id = id;
        historyHTML.html = html;

        return historyHTML;
    }
}
