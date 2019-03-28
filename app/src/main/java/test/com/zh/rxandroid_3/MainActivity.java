package test.com.zh.rxandroid_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import test.com.zh.rxandroid_3.utils.DownLoadUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PATH = "http://a3.topitme.com/4/a4/53/1128058568ec753a44l.jpg";
    private static final String PATH1 = "http://pic69.nipic.com/file/20150608/9252150_134415115986_2.jpg";
    private static final String PATH2 = "http://img02.tooopen.com/images/20150512/tooopen_sy_123903738291.jpg";
    @BindView(R.id.iv_src)
    ImageView ivSrc;
    @BindView(R.id.bt)
    Button bt;

    private DownLoadUtil downLoadUtil;
    int i = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        downLoadUtil = new DownLoadUtil();
    }


    public void onDownClick(View view) {
        boolean isFirstCross = true;
        i++;
        Log.i(TAG, "name==" + Thread.currentThread().getName() + ";pid==" + Thread.currentThread().getId() + ";i==" + i);
        long time1 = System.currentTimeMillis();
        if (isFirstCross&&i<3) {
            if(i==1){
                MyAscyTask myAscyTask= new MyAscyTask();
                myAscyTask.execute();
            }else{
                loadImageHttp();
            }
        } else {
            if (i % 2 == 0) {
                downLoadUtil.downLoadImage(PATH)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<byte[]>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ivSrc.setImageBitmap(bitmap);
                                long time2 = System.currentTimeMillis();
                                Log.i(TAG, "所用的时间为=" + (time2 - time1));
                            }
                        });

            } else {
                downLoadUtil.downLoadImage(PATH1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<byte[]>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, e.toString());
                            }

                            @Override
                            public void onNext(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ivSrc.setImageBitmap(bitmap);
                                long time2 = System.currentTimeMillis();
                                Log.i(TAG, "所用的时间为=" + (time2 - time1));
                            }
                        });

            }
        }

    }


    private class MyAscyTask extends AsyncTask<String, Integer, byte[]> {
        byte[] needBytes = null;
        @Override
        protected byte[] doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(PATH1).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        byte[] data = response.body().bytes();
                        if (data != null) {
                            needBytes = data;
                            runMainThread(needBytes);
                        }

                    }
                }
            });
            return needBytes;
        }
    }

    private void runMainThread(byte[] bytes){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivSrc.setImageBitmap(bitmap);
            }
        });
    }


    private void loadImageHttp(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(PATH2).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] data = response.body().bytes();
                    if (data != null) {
                        runMainThread(data);
                    }
                }
            }
        });
    }

}
