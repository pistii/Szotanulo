package com.pisti.szotanulo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    List<Repository> words;
    TextView back_tv;
    TextView front_tv;
    int currentWordId = 0;
    ImageView successBtn;
    ImageView failBtn;
    List<Repository>  failedWords = new ArrayList<>();
    Boolean fromEnglish = true;
    AnimationHandler FlipCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = new ArrayList<>();
        FlipCard = new AnimationHandler(MainActivity.this);

        front_tv = findViewById(R.id.card_font);
        back_tv = findViewById(R.id.card_back);

        successBtn = findViewById(R.id.icon_success);
        successBtn.setOnClickListener(ImageView -> {
            if (currentWordId<words.size()) {
                Random random = new Random();
                currentWordId = random.nextInt(words.size());
            }
            SetCardText();
        });


        failBtn = findViewById(R.id.icon_fail);
        failBtn.setOnClickListener(ImageView -> {
            failedWords.add(words.get(currentWordId)); // olyan szavak kigyűjtése amelyeknek a megfelelőjét nem sikerült eltalálni
            if (currentWordId<words.size()) {
                Random random = new Random();
                currentWordId = random.nextInt(words.size());
            }
            SetCardText();
        });

        ImageView arrow = findViewById(R.id.arrow);
        arrow.setOnClickListener(ImageView -> {
            TextView from = findViewById(R.id.fromWord);
            TextView to = findViewById(R.id.toWord);
            if (!from.getText().equals("Angol")) {
                from.setText(R.string.fromWord);
                to.setText(R.string.toWord);
                fromEnglish = true;
            } else {
                from.setText(R.string.toWord);
                to.setText(R.string.fromWord);
                fromEnglish = false;
            }
        });

    }

    @Override
    protected void onStart() {
        try {
            dbConnection db = new dbConnection(MainActivity.this);
            this.words = db.words;
            SetCardText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    public void SetCardText() {
        if (words.size() > 0) {
            if (fromEnglish) {
                if (FlipCard.isFront) {
                    back_tv.setText(words.get(currentWordId).getHungarianMeaning());
                    front_tv.setText(words.get(currentWordId).getEnglishMeaning());
                } else {
                    front_tv.setText(words.get(currentWordId).getHungarianMeaning());
                    back_tv.setText(words.get(currentWordId).getEnglishMeaning());
                }
            } else {
                if (FlipCard.isFront) {
                    back_tv.setText(words.get(currentWordId).getEnglishMeaning());
                    front_tv.setText(words.get(currentWordId).getHungarianMeaning());
                } else {
                    front_tv.setText(words.get(currentWordId).getEnglishMeaning());
                    back_tv.setText(words.get(currentWordId).getHungarianMeaning());
                }
            }
        }
    }
}