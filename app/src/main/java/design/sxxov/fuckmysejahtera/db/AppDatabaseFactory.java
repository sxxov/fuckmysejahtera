package design.sxxov.fuckmysejahtera.db;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseFactory {
    private Context ctx;

    public AppDatabaseFactory(Context ctx) {
        this.ctx = ctx;
    }

    public AppDatabase create() {
        return Room
                .databaseBuilder(
                        this.ctx,
                        AppDatabase.class,
                        "db"
                )
                .addMigrations(AppDatabaseMigrationManager.getMigrations())
                .build();
    }
}
