package com.apps.inslibrary;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.utils.InsShared;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.xutils.x;

public class InsManager {
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Insta");
    public static Context context;
    public static InsShared shared;

    public static void init(Application application) {
        x.Ext.init(application);
        context = application;
        shared = InsShared.init(application);
    }

    public static List<File> getListFile(InstagramData instagramData) {
        List<InstagramRes> instagramRes = instagramData.getInstagramRes();
        ArrayList arrayList = new ArrayList();
        try {
            for (InstagramRes saveFile : instagramRes) {
                File file = new File(saveFile.getSaveFile());
                if (file.exists()) {
                    arrayList.add(file);
                }
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public static String getSaveFile(String str, boolean z) {
        String filenameFromURL = z ? getFilenameFromURL(str) : getImageFilenameFromURL(str);
        return RootDirectoryInstaShow.getAbsolutePath() + "/" + filenameFromURL;
    }

    public static String getStoriesId(String str) {
        String str2 = str.split("\\?")[0];
        if (!str2.endsWith("/")) {
            str2 = str2 + "/";
        }
        String[] split = str2.split("/");
        return split[split.length - 1];
    }

    public static String getHostUrl(String str) {
        return str.split("\\?")[0];
    }

    public static String getUrlWithoutParameters(String str) {
        try {
            URI uri = new URI(str);
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), (String) null, uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String str2 = str.split("\\?")[0];
                return TextUtils.isEmpty(str2) ? str : str2;
            } catch (Exception e2) {
                e2.printStackTrace();
                return "";
            }
        }
    }

    private static String getFilenameFromURL(String str) {
        try {
            return new File(str + ".mp4").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    private static String getImageFilenameFromURL(String str) {
        try {
            return new File(str + ".png").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }
}
