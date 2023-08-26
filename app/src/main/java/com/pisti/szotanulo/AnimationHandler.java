package com.pisti.szotanulo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;

public class AnimationHandler {
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/animation/cardflip.html
    Animator front_animation;
    Animator back_animation;
    boolean isFront = true;
    View front;
    View back;

    public AnimationHandler(MainActivity mainActivity) {
        float scale = mainActivity.getResources().getDisplayMetrics().density;
        front = mainActivity.findViewById(R.id.card_font);
        back = mainActivity.findViewById(R.id.card_back);
        //front.setCameraDistance(301000 * scale);
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);

        front_animation = AnimatorInflater.loadAnimator(mainActivity.getApplicationContext(), R.animator.front_animator);
        back_animation = AnimatorInflater.loadAnimator(mainActivity.getApplicationContext(), R.animator.back_animator);
        front.setOnClickListener(view -> {
            if (isFront) {
                FlipToFront();
            } else {
                front_animation.setTarget(back);
                back_animation.setTarget(front);
                back_animation.start();
                front_animation.start();
                isFront = true;
            }
        });
    }

    public void FlipToFront() {
        front_animation.setTarget(front);
        back_animation.setTarget(back);
        front_animation.start();
        back_animation.start();
        isFront = false;
    }
}
