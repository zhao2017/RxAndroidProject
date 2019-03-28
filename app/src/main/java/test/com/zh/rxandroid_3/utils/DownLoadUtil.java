package test.com.zh.rxandroid_3.utils;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * 创建日期：2019/3/28
 * 描述: 实现一个图片下载的工具类
 *
 * @author: zhaoh
 */
public class DownLoadUtil {
    private OkHttpClient okHttpClient;
    public DownLoadUtil(){
        okHttpClient = new OkHttpClient().newBuilder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS).build();
    }




    public  Observable<byte[]> downLoadImage(String path){
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                 if(!subscriber.isUnsubscribed()){
                     Log.i("MainActivity","name=="+Thread.currentThread().getName()+";pid=="+Thread.currentThread().getId());
                     //创建网络请求 默认为get请求
                     Request request = new Request.Builder().url(path).build();
                     okHttpClient.newCall(request).enqueue(new Callback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             subscriber.onError(e);

                         }
                         @Override
                         public void onResponse(Call call, Response response) throws IOException {
                             if(response.isSuccessful()){
                                 byte[] data = response.body().bytes();
                                 if(data!=null){
                                     subscriber.onNext(data);
                                 }
                                 subscriber.onCompleted();
                             }
                         }
                     });

                 }

            }
        });

    }



}
