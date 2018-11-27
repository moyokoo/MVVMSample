package net.android.anko.widgets;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import net.android.anko.helper.AnimatorHelper;


public class ZoomAbleFloatingActionButton extends FloatingActionButton {

    private AnimatorSet zoomInAnimatorSet;
    private AnimatorSet zoomOutAnimatorSet;

    private boolean isNormalSize = true;

    public ZoomAbleFloatingActionButton(Context context) {
        super(context);
    }

    public ZoomAbleFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomAbleFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void zoomIn(){
        if(zoomInAnimatorSet == null){
            zoomInAnimatorSet = AnimatorHelper.getZoomAnimatorSet(this, 0);
        }
        if(zoomInAnimatorSet.isRunning() || !isNormalSize){
            return;
        }
        zoomInAnimatorSet.start();
        isNormalSize = false;
    }

    public void zoomOut(){
        if(zoomOutAnimatorSet == null){
            zoomOutAnimatorSet = AnimatorHelper.getZoomAnimatorSet(this, 1);
        }
        if(zoomOutAnimatorSet.isRunning() || isNormalSize){
            return;
        }
        zoomOutAnimatorSet.start();
        isNormalSize = true;
    }

}
