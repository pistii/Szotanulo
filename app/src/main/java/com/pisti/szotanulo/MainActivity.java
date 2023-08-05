package com.pisti.szotanulo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Animator front_animation;
    Animator back_animation;

    boolean isFront = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;


        View front = findViewById(R.id.card_font);
        View back = findViewById(R.id.card_back);

        //front.setCameraDistance(301000 * scale);
        front.setCameraDistance(8000 * scale);

        back.setCameraDistance(10000 * scale);

        front_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        back_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);

        front.setOnClickListener(view -> {

            if (isFront) {
                front_animation.setTarget(front);
                back_animation.setTarget(back);
                front_animation.start();
                back_animation.start();
                isFront = false;
            } else {
                front_animation.setTarget(back);
                back_animation.setTarget(front);
                back_animation.start();
                front_animation.start();
                isFront = true;
            }
        });

        back.setOnClickListener(view -> {

            if (isFront) {
                front_animation.setTarget(front);
                back_animation.setTarget(back);
                front_animation.start();
                back_animation.start();
                isFront = false;
            } else {
                front_animation.setTarget(back);
                back_animation.setTarget(front);
                back_animation.start();
                front_animation.start();
                isFront = true;
            }
        });
    }
}