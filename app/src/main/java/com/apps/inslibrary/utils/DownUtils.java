package com.apps.inslibrary.utils;

import java.io.File;
import java.net.SocketTimeoutException;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.HttpRetryHandler;
import org.xutils.http.request.UriRequest;

public class DownUtils {

    public interface OnDownCallback {
        void onError(String str);

        void onLoading(long j, long j2);

        void onSuccess(File file);
    }

    public static void down(String str, String str2, final OnDownCallback onDownCallback) {
        RequestParams requestParams = new RequestParams(str);
        requestParams.setSaveFilePath(str2);
        requestParams.setMaxRetryCount(3);
        requestParams.setHttpRetryHandler(new HttpRetryHandler() {
            public boolean canRetry(UriRequest uriRequest, Throwable th, int i) {
                blackList.add(SocketTimeoutException.class);
                return i <= this.maxRetryCount;
            }
        });
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            public void onCancelled(CancelledException cancelledException) {
            }

            public void onFinished() {
            }

            public void onStarted() {
            }

            public void onWaiting() {
            }

            public void onLoading(long j, long j2, boolean z) {
                if (onDownCallback != null) {
                    onDownCallback.onLoading(j, j2);
                }
            }

            public void onSuccess(File file) {
                if (onDownCallback != null) {
                    onDownCallback.onSuccess(file);
                }
            }

            public void onError(Throwable th, boolean z) {
                if (onDownCallback != null) {
                    onDownCallback.onError(th.getMessage());
                }
            }
        });
    }
}
