package com.pisti.szotanulo;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

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


public class dbConnection extends MainActivity {
    List<Repository> words;
    WordsService service;
    Context mainActivity;
    CountDownTimer timer;
    public dbConnection (MainActivity mainActivity) {
        this.mainActivity = mainActivity.getApplicationContext();
        words = new ArrayList<>();
        QueryData();
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                if (words.size() <= 3) {
                    QueryData();
                    timer.start();
                }
            }
        }.start();
    }

    public List<Repository> QueryData() {

        Toast.makeText(mainActivity.getApplicationContext(), "query data from server... Please wait", Toast.LENGTH_LONG).show();
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
                            SetCardText();
                        }catch (Exception exception)
                        {
                            Toast.makeText(mainActivity.getApplicationContext(), "error: " + exception, Toast.LENGTH_LONG).show();
                            Log.d("error", exception.getMessage());
                        }
                    }
                else {
                    Toast.makeText(mainActivity.getApplicationContext(), "error while connectiong to server", Toast.LENGTH_LONG).show();
                    Log.d("No successful connection", "no connection");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("connection failure", "failed");
                Toast.makeText(mainActivity.getApplicationContext(), "Connection failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (words.size() == 0) {
            Repository word1 = new Repository();
            word1.setWordId(0);
            word1.setEnglishMeaning("Cat");
            word1.setHungarianMeaning("Macska");
            word1.setRememberanceLevel(3);

            Repository word2 = new Repository();
            word1.setWordId(1);
            word1.setEnglishMeaning("Dog");
            word1.setHungarianMeaning("Kutya");
            word1.setRememberanceLevel(3);

            Repository word3 = new Repository();
            word3.setWordId(2);
            word3.setEnglishMeaning("Cat");
            word3.setHungarianMeaning("Macska");
            word3.setRememberanceLevel(3);

            words.add(word1);
            words.add(word2);
            words.add(word3);
        }
        return words;
    }

    private OkHttpClient setTimeOut() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        return okHttpClient;
    }

    //It's not needed anymore but can be used in retrofit as a localhost client
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
