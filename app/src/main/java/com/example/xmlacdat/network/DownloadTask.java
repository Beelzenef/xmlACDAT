package com.example.xmlacdat.network;

import android.os.Environment;

import com.example.xmlacdat.pojo.FailureEvent;
import com.example.xmlacdat.pojo.SuccessEvent;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Download tasks
 */

public class DownloadTask {

    public static void executeDownload(String rss, String tmp) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), tmp);
        RestClient.get(rss, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                EventBus.getDefault().post(new FailureEvent("Algo ha salido mal :(", 1));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                EventBus.getDefault().post(new SuccessEvent("¡Éxito!", file));
            }
        });
    }
}
