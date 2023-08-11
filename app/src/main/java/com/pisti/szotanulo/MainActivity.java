package com.pisti.szotanulo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        View front = findViewById(R.id.card_font);
        View back = findViewById(R.id.card_back);

        //front.setCameraDistance(301000 * scale);
        front.setCameraDistance(8000 * scale);

        back.setCameraDistance(8000 * scale);

        front_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        back_animation = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);
        //TODO: SETUP THE CONNECTION


        front.setOnClickListener(view -> {
            if (isFront) {
                get();

                front_animation.setTarget(front);
                back_animation.setTarget(back);
                front_animation.start();
                back_animation.start();
                isFront = false;
            } else {
                get();

                front_animation.setTarget(back);
                back_animation.setTarget(front);
                back_animation.start();
                front_animation.start();
                isFront = true;
            }
        });

        back.setOnClickListener(view -> {
            if (isFront) {
                get();

                front_animation.setTarget(front);
                back_animation.setTarget(back);
                front_animation.start();
                back_animation.start();
                isFront = false;
            } else {
                get();

                front_animation.setTarget(back);
                back_animation.setTarget(front);
                back_animation.start();
                front_animation.start();
                isFront = true;
            }

        });
    }
    private final MutableLiveData<Repository> _user = new MutableLiveData<>();

    public void get() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5158")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
                .build();

        service = retrofit.create(WordsService.class);
        String responseMessage = "";
        LiveData<Repository> user = (LiveData<Repository>) _user;
        try {
            Call<List<Repository>> repo = service.listRepos(4);

            repo.enqueue(new Callback<List<Repository>>() {
                @Override
                public void onResponse(Response<List<Repository>> response) {
                    if(response.isSuccess()) {

                        if(!response.body().isEmpty())
                        {
                            try {
                                List<Repository> repo = response.body();
                                for (Repository repository : repo) {
                                    Log.d("resp", repository.HungarianMeaning);
                                    Toast.makeText(MainActivity.this, "siker: " + repository.HungarianMeaning, Toast.LENGTH_SHORT).show();

                                }


                            }catch (Exception exception)
                            {

                            }
                        }
                    else {

                        }
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MainActivity.this, "error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "error" + e, Toast.LENGTH_SHORT).show();
        }
    }

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

