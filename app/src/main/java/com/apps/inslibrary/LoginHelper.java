package com.apps.inslibrary;

import android.text.TextUtils;
import com.apps.inslibrary.entity.login.ReelUser;

public class LoginHelper {
    public static String COOKIES = "Cookies";
    public static String CSRF = "csrf_token";
    public static String IS_LOGIN = "isLogin";
    public static String SESSION_ID = "sessionid";
    public static String TMP_COOKIES = "TMP_Cookies";
    public static String USERID = "ds_user_id";
    public static String USER_INFO = "user_info";

    public static void addUserInfo(ReelUser reelUser) {
        InsManager.shared.setEntity(USER_INFO, reelUser);
    }

    public static ReelUser getUserInfo() {
        return (ReelUser) InsManager.shared.getEntity(USER_INFO);
    }

    public static void addUserID(String str) {
        if (!TextUtils.isEmpty(str)) {
            InsManager.shared.setValue(USERID, str);
        }
    }

    public static String getUserID() {
        return (String) InsManager.shared.getValue(USERID, "");
    }

    public static void addSessionID(String str) {
        if (!TextUtils.isEmpty(str)) {
            InsManager.shared.setValue(SESSION_ID, str);
        }
    }

    public static String getSessionID() {
        return (String) InsManager.shared.getValue(SESSION_ID, "");
    }

    public static void addCSRF(String str) {
        if (!TextUtils.isEmpty(str)) {
            InsManager.shared.setValue(CSRF, str);
        }
    }

    public static String getCSRF() {
        return (String) InsManager.shared.getValue(CSRF, "");
    }

    public static void setIsLogin(boolean z) {
        InsManager.shared.setValue(IS_LOGIN, z);
    }

    public static boolean getIsLogin() {
        return (Boolean) InsManager.shared.getValue(IS_LOGIN, false);
    }

    public static void setCookies(String str) {
        if (!TextUtils.isEmpty(str)) {
            InsManager.shared.setValue(COOKIES, str);
        }
    }

    public static String getCookies() {
        return (String) InsManager.shared.getValue(COOKIES, "");
    }

    public static String getTmpCookies() {
        return (String) InsManager.shared.getValue(TMP_COOKIES, "");
    }

    public static void setTmpCookies(String str) {
        InsManager.shared.setValue(TMP_COOKIES, str);
    }

    public static void outLogin() {
        InsManager.shared.remove(COOKIES);
        InsManager.shared.remove(IS_LOGIN);
        InsManager.shared.remove(CSRF);
        InsManager.shared.remove(SESSION_ID);
        InsManager.shared.remove(USERID);
        InsManager.shared.remove(USER_INFO);
    }
}
