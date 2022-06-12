package com.apps.inslibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.apps.inslibrary.entity.InstagramUser;
import com.meghdut.instagram.downloader.util.AppManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.HttpRetryHandler;
import org.xutils.http.request.UriRequest;

public class DownUtils {

    /* loaded from: classes.dex */
    public interface OnDownCallback {
        void onError(String str);

        void onLoading(long j, long j2);

        void onStart();

        void onSuccess(File file);
    }

    public static void createFileFolder() {
        String saveFilePath = AppManager.getSettingConfig().getSaveFilePath();
        File file = new File(saveFilePath);
        if (!new File(saveFilePath).exists()) {
            file.mkdirs();
        }
    }

    public static String getSaveFile(String str, boolean z) {
        String filenameFromURL = z ? getFilenameFromURL(str) : getImageFilenameFromURL(str);
        String saveFilePath = AppManager.getSettingConfig().getSaveFilePath();
        File file = new File(saveFilePath + "/" + filenameFromURL);
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    public static String getSaveFile(InstagramUser instagramUser, String url, boolean isVideo) {
        File file;
        String filenameFromURL = isVideo ? getFilenameFromURL(url) : getImageFilenameFromURL(url);
        String saveFilePath = AppManager.getSettingConfig().getSaveFilePath();
        if (instagramUser != null && !TextUtils.isEmpty(instagramUser.getFull_name())) {
            file = new File(saveFilePath + "/" + instagramUser.getUsername() + "_" + filenameFromURL);
        } else {
            file = new File(saveFilePath + "/" + filenameFromURL);
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static String getFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public static String getImageFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public static void down(String str, String str2, final OnDownCallback onDownCallback) {
        RequestParams requestParams = new RequestParams(str);
        new File(str2).exists();
        requestParams.setSaveFilePath(str2);
        requestParams.setMaxRetryCount(10);
        requestParams.setHttpRetryHandler(new HttpRetryHandler() { // from class: com.apps.instagram.downloader.utils.DownUtils.1
            @Override // org.xutils.http.app.HttpRetryHandler
            public boolean canRetry(UriRequest uriRequest, Throwable th, int i) {
                blackList.add(SocketTimeoutException.class);
                blackList.add(SocketException.class);
                int i2 = this.maxRetryCount;
                Log.e("TAG", "：" + i);
                return i <= this.maxRetryCount;
            }
        });
        x.http().get(requestParams, new Callback.ProgressCallback<File>() { // from class: com.apps.instagram.downloader.utils.DownUtils.2
            @Override // org.xutils.common.Callback.CommonCallback
            public void onCancelled(Callback.CancelledException cancelledException) {
            }

            @Override // org.xutils.common.Callback.CommonCallback
            public void onFinished() {
            }

            @Override // org.xutils.common.Callback.ProgressCallback
            public void onWaiting() {
            }

            @Override // org.xutils.common.Callback.ProgressCallback
            public void onStarted() {
                if (onDownCallback != null) {
                    onDownCallback.onStart();
                }
            }

            @Override // org.xutils.common.Callback.ProgressCallback
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

            @Override // org.xutils.common.Callback.CommonCallback
            public void onError(Throwable th, boolean z) {
                if (onDownCallback != null) {
                    onDownCallback.onError(th.toString());
                }
            }
        });
    }
}
