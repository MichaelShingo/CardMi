package com.michaelshingo.flashcards;

import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

public class FlipInYCustom extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotationY", 90, -15, 15, 0),
                ObjectAnimator.ofFloat(target, "alpha", 0.25f, 0.5f, 0.75f, 1));
    }
}
