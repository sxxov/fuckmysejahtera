package design.sxxov.fuckmysejahtera.utilities;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncUtility {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
        void onError(Exception e);
    }

    public <R> void executeAsync(Callable<R> callable) {
        executeAsync(callable, null);
    }

    public <R> void executeAsync(Callable<R> callable, @Nullable Callback<R> callback) {
        executor.execute(() -> {
            final R result;

            try {
                result = callable.call();

                if (callback == null) {
                    return;
                }

                handler.post(() -> {
                    callback.onComplete(result);
                });
            } catch (Exception e) {
                if (callback == null) {
                    return;
                }

                handler.post(() -> {
                    callback.onError(e);
                });
            }
        });
    }
}
