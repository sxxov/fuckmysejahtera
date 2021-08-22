package design.sxxov.fuckmysejahtera.db;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class AppDatabaseMigrationManager {
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("create table `HistoryHTML` (`id` integer not null, `html` text, primary key(`id`))");
            database.execSQL("drop table `HistoryImage`");
        }
    };

    public static Migration[] getMigrations() {
        return new Migration[] {
                MIGRATION_1_2
        };
    }
}
