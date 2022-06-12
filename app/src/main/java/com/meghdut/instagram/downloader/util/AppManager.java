package com.meghdut.instagram.downloader.util;

import android.content.Context;
import androidx.core.app.NotificationManagerCompat;
import com.apps.inslibrary.InsManager;
import com.meghdut.instagram.downloader.entity.SettingParams;
import java.util.Calendar;

/* loaded from: classes.dex */
public class AppManager {
    private static final String DOT_EZ_KEY = "red_ez_dot";
    private static final String DOT_KEY = "red_dot";
    private static final String FILTER_TYPE = "filter_type";
    private static final String IS_FIRST = "isFirst";
    private static final String RATE_APP_KEY = "rate_app_key";
    private static final String SETTING_CONFIG = "setting_config";
    private static final String SHOW_TYPE = "show_type";

    public static boolean isShowRateApp() {
        int intValue = (Integer) InsManager.shared.getValue(RATE_APP_KEY, 0);
        if (intValue == -1) {
            return false;
        }
        if (intValue == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(5, 5);
            InsManager.shared.setValue(RATE_APP_KEY, calendar.get(5));
            return false;
        }
        int i = Calendar.getInstance().get(5);
        return i == intValue || i > intValue;
    }

    public static void closeRateApp() {
        InsManager.shared.setValue(RATE_APP_KEY, -1);
    }

    public static void setRedDot(boolean z) {
        InsManager.shared.setValue(DOT_KEY, z);
    }

    public static boolean getRedDot() {
        return (Boolean) InsManager.shared.getValue(DOT_KEY, false);
    }

    public static void setEZRedDot(boolean z) {
        InsManager.shared.setValue(DOT_EZ_KEY, z);
    }

    public static boolean getEZRedDot() {
        return (Boolean) InsManager.shared.getValue(DOT_EZ_KEY, true);
    }

    public static void setShowDownType(int i) {
        InsManager.shared.setValue(SHOW_TYPE, i);
    }

    public static int getShowDownType() {
        return (Integer) InsManager.shared.getValue(SHOW_TYPE, 0);
    }

    public static void setFilterType(int i) {
        InsManager.shared.setValue(FILTER_TYPE, i);
    }

    public static int getFilterType() {
        return (Integer) InsManager.shared.getValue(FILTER_TYPE, 0);
    }

    public static SettingParams getSettingConfig() {
        SettingParams settingParams = (SettingParams) InsManager.shared.getEntity(SETTING_CONFIG);
        return settingParams == null ? new SettingParams() : settingParams;
    }

    public static void setSettingConfig(SettingParams settingParams) {
        InsManager.shared.setEntity(SETTING_CONFIG, settingParams);
    }

    public static boolean getFirstStart() {
        return (Boolean) InsManager.shared.getValue(IS_FIRST, true);
    }

    public static void setFirstStart(boolean z) {
        InsManager.shared.setValue(IS_FIRST, z);
    }

    public static boolean canShowNotification(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
