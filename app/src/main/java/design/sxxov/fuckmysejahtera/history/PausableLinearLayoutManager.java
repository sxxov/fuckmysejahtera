package design.sxxov.fuckmysejahtera.history;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import design.sxxov.fuckmysejahtera.blocks.interfaces.common.Pausable;

public class PausableLinearLayoutManager extends LinearLayoutManager implements Pausable {
    public boolean isPaused = false;

    public PausableLinearLayoutManager(Context ctx) {
        super(ctx);
    }

    @Override
    public void pause() {
        this.isPaused = true;
    }

    @Override
    public void unpause() {
        this.isPaused = false;
    }

    @Override
    public boolean canScrollVertically() {
        return !this.isPaused && super.canScrollVertically();
    }
}
