package com.pisti.szotanulo;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/animation/cardflip.html
    Animator front_animation;
    Animator back_animation;
    boolean isFront = true;
    WordsService service;
    List<Repository> words = new ArrayList<>();
    View front;
    View back;
    TextView back_tv;
    TextView front_tv;
    int currentWordId = 0;
    ImageView successBtn;
    ImageView failBtn;
    List<Repository>  failedWords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnimationHandler();
        QueryData();
        successBtn = findViewById(R.id.icon_success);
        successBtn.setOnClickListener(ImageView -> {
            if (currentWordId+1<words.size())
                currentWordId++;
            SetCardText();
            FlipToFront();
        });

        failBtn = findViewById(R.id.icon_fail);
        failBtn.setOnClickListener(ImageView -> {
            failedWords.add(words.get(currentWordId)); // olyan szavak kigyűjtése amelyeknek a megfelelőjét nem sikerült eltalálni
            if (currentWordId<words.size())
                currentWordId++;
        });
    }

    public void AnimationHandler() {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        front = findViewById(R.id.card_font);
        back = findViewById(R.id.card_back);
        //front.setCameraDistance(301000 * scale);
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);

        front_tv = findViewById(R.id.card_font);
        back_tv = findViewById(R.id.card_back);
        front_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        back_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);
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
            SetCardText();
        });
    }

    public void SetCardText() {
        front_tv.setText(words.get(currentWordId).getHungarianMeaning());
        back_tv.setText(words.get(currentWordId).getEnglishMeaning());
        Log.d("WORDS", words.get(0).getEnglishMeaning() + ", " +  words.get(1).getEnglishMeaning() + ", " + words.get(2).getHungarianMeaning());
    }

    public void FlipToFront() {
        front_animation.setTarget(front);
        back_animation.setTarget(back);
        front_animation.start();
        back_animation.start();
        isFront = false;
    }

    public void QueryData() {
        Toast.makeText(MainActivity.this, "query data from server... Please wait", Toast.LENGTH_LONG);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://szotanuloapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(setTimeOut())
                .build();

        service = retrofit.create(WordsService.class);

        final Call<List<Repository>>[] repo = new Call[]{service.GetAll(4)};
        repo[0].enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Response<List<Repository>> response) {
                if(response.isSuccess()) {
                    if(!response.body().isEmpty())
                    {
                        try {
                            List<Repository> res = response.body();
                            for (int i=0;i<res.size();i++) {
                                Repository word = new Repository();
                                word.setWordId(res.get(i).getWordId());
                                word.setHungarianMeaning(res.get(i).getHungarianMeaning());
                                word.setEnglishMeaning(res.get(i).getEnglishMeaning());
                                word.setRememberanceLevel(res.get(i).getRememberanceLevel());
                                words.add(word);
                            }
                            front_tv.setText(words.get(currentWordId).getHungarianMeaning());
                        }catch (Exception exception)
                        {
                            Log.d("error", exception.getMessage());
                        }
                    }
                else {
                    Log.d("No successful connection", "no connection");
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("connection failure", "failed");
                Toast.makeText(MainActivity.this, "Connection failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private OkHttpClient setTimeOut() {
        OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
                return okHttpClient;
    }

    //For localhost testing
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient builder = new OkHttpClient();
            builder.setSslSocketFactory(sslSocketFactory);
            builder.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

