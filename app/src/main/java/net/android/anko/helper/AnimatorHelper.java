package net.android.anko.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class AnimatorHelper {

    public static void zoomIn(View target){
        zoom(target, 0);
    }

    public static void zoomOut(View target){
        zoom(target, 1);
    }

    public static void zoom(View target, float value){
        AnimatorSet animatorSet = getZoomAnimatorSet(target, value);
        animatorSet.start();
    }

    public static AnimatorSet getZoomAnimatorSet(View target, float value){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(target, View.SCALE_X, value);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, View.SCALE_Y, value);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        return animatorSet;
    }

}
