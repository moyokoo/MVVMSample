package net.android.anko.utils.banner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class RecyclerGallery extends RecyclerView {

    private static final int VELOCITY = 8000; //滑动速度

    public RecyclerGallery(Context context) {
        super(context);
    }

    public RecyclerGallery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerGallery(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = calcVelocity(velocityX);
        velocityY = calcVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    private int calcVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, VELOCITY);
        } else {
            return Math.max(velocity, -VELOCITY);
        }
    }
}
