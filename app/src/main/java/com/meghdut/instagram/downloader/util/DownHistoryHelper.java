package com.meghdut.instagram.downloader.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.apps.inslibrary.InsManager;
import com.apps.inslibrary.InstagramRes;
import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.entity.InstagramUser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DownHistoryHelper {
    private static final String DOWN_USER_KEY = "down_user_info";
    private static final String HISTORY_KEY = "down_history";

    public static boolean isUrlDownHistory(String str) {
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory == null || downHistory.size() <= 0) {
            return false;
        }
        for (InstagramData instagramData : downHistory) {
            if (str.equals(instagramData.getShareUrl())) {
                return true;
            }
        }
        return false;
    }

    public static void addDownHistory(InstagramData instagramData) {
        if (!AppManager.getSettingConfig().isNotSaveHistory()) {
            return;
        }
        List<InstagramData> downHistory = getDownHistory();
        downHistory.add(instagramData);
        InsManager.shared.setListEntity(HISTORY_KEY, downHistory);
    }

    public static boolean isDownHistory(String str) {
        if (!AppManager.getSettingConfig().isNotSaveHistory()) {
            return false;
        }
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory.size() <= 0) {
            return false;
        }
        for (InstagramData instagramData : downHistory) {
            if ((!TextUtils.isEmpty(instagramData.getViewUrl()) && instagramData.getViewUrl().contains(str)) || (!TextUtils.isEmpty(instagramData.getShareUrl()) && instagramData.getShareUrl().contains(str))) {
                return true;
            }
        }
        return false;
    }

    public static InstagramData isPk(String str) {
        if (!AppManager.getSettingConfig().isNotSaveHistory()) {
            return null;
        }
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory.size() <= 0) {
            return null;
        }
        for (InstagramData instagramData : downHistory) {
            if (instagramData.getId().equals(str)) {
                return instagramData;
            }
        }
        return null;
    }

    public static boolean isCollect(InstagramData instagramData) {
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory.size() <= 0) {
            return false;
        }
        for (InstagramData instagramData2 : downHistory) {
            if (instagramData.getId().equals(instagramData2.getId())) {
                return instagramData2.isCollection();
            }
        }
        return false;
    }

    public static void setCollect(InstagramData instagramData) {
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory.size() <= 0) {
            return;
        }
        ArrayList<InstagramData> arrayList = new ArrayList<>();
        for (InstagramData instagramData2 : downHistory) {
            if (instagramData.getId().equals(instagramData2.getId())) {
                arrayList.add(instagramData);
            } else {
                arrayList.add(instagramData2);
            }
        }
        InsManager.shared.setListEntity(HISTORY_KEY, arrayList);
    }

    public static List<InstagramData> getDownHistory() {
        List<InstagramData> listEntity = InsManager.shared.getListEntity(HISTORY_KEY, InstagramData.class);
        return listEntity == null ? new ArrayList<>() : listEntity;
    }

    public static List<InstagramData> getReverseDownHistory() {
        List<InstagramData> listEntity = InsManager.shared.getListEntity(HISTORY_KEY, InstagramData.class);
        if (listEntity != null && listEntity.size() > 0) {
            Collections.reverse(listEntity);
        }
        return listEntity == null ? new ArrayList<>() : listEntity;
    }

    public static void removeHistory(InstagramData instagramData) {
        Context context = InsManager.context;
//        FirebaseHelper.onEvent(context, "deleteHistory", instagramData.getViewUrl() + "");
        List<InstagramData> downHistory = getDownHistory();
        if (downHistory == null || downHistory.size() <= 0) {
            removeHistoryAll();
            return;
        }
        ArrayList<InstagramData> arrayList = new ArrayList<>();
        for (InstagramData instagramData2 : downHistory) {
            if (!instagramData2.getId().equals(instagramData.getId())) {
                arrayList.add(instagramData2);
            } else {
                delete(instagramData2);
            }
        }
        if (arrayList.size() <= 0) {
            removeHistoryAll();
        } else {
            InsManager.shared.setListEntity(HISTORY_KEY, arrayList);
        }
    }

    public static void removeHistory(List<InstagramData> list) {
        boolean z;
        if (list != null && list.size() == 1) {
            removeHistory(list.get(0));
            return;
        }
        List<InstagramData> downHistory = getDownHistory();
        ArrayList<InstagramData> arrayList = new ArrayList<>();
        for (InstagramData instagramData : downHistory) {
            String id = instagramData.getId();
            Iterator<InstagramData> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                InstagramData next = it.next();
                if (id.equals(next.getId())) {
                    delete(next);
                    z = true;
                    break;
                }
            }
            if (!z) {
                arrayList.add(instagramData);
            }
        }
        if (arrayList.size() <= 0) {
            removeHistoryAll();
        } else {
            InsManager.shared.setListEntity(HISTORY_KEY, arrayList);
        }
    }

    public static void removeHistoryAll() {
        InsManager.shared.remove(HISTORY_KEY);
    }

    public static void delete(InstagramData instagramData) {
        for (InstagramRes instagramRes : instagramData.getInstagramRes()) {
            String saveFile = instagramRes.getSaveFile();
            try {
                boolean delete = new File(saveFile).delete();
                Log.e("TAG", "删除文件：" + saveFile + ">>" + delete);
            } catch (Exception unused) {
                unused.printStackTrace();
            }
        }
    }

    public static List<InstagramUser> addFollowUser(List<InstagramUser> list) {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (list == null) {
            return downUserInfo;
        }
        if (downUserInfo == null) {
            ArrayList<InstagramUser> arrayList = new ArrayList<>(list);
            InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList);
            Collections.reverse(arrayList);
            return arrayList;
        }
        ArrayList<InstagramUser> arrayList2 = new ArrayList<>(list);
        for (InstagramUser instagramUser : downUserInfo) {
            if (!isExitUser(instagramUser, list)) {
                arrayList2.add(instagramUser);
            }
        }
        InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList2);
        Collections.reverse(arrayList2);
        return arrayList2;
    }

    public static boolean isExitUser(InstagramUser instagramUser, List<InstagramUser> list) {
        Iterator<InstagramUser> it = list.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().getUser_id() == instagramUser.getUser_id()) {
                z = true;
            }
        }
        return z;
    }

    public static List<InstagramUser> addDownUserInfo(InstagramUser instagramUser) {
        boolean isNotSaveHistory = AppManager.getSettingConfig().isNotSaveHistory();
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (isNotSaveHistory && instagramUser != null) {
            if (downUserInfo == null) {
                ArrayList<InstagramUser> arrayList = new ArrayList<>();
                arrayList.add(instagramUser);
                InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList);
                Collections.reverse(arrayList);
                return arrayList;
            }
            ArrayList<InstagramUser> arrayList2 = new ArrayList<>();
            for (InstagramUser instagramUser2 : downUserInfo) {
                if (instagramUser.getUser_id() != instagramUser2.getUser_id()) {
                    arrayList2.add(instagramUser2);
                }
            }
            arrayList2.add(instagramUser);
            InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList2);
            Collections.reverse(arrayList2);
            return arrayList2;
        }
        return downUserInfo;
    }

    public static void moveDownUserInfo(int i, InstagramUser instagramUser) {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (downUserInfo == null) {
            downUserInfo = new ArrayList<>();
            downUserInfo.add(instagramUser);
            InsManager.shared.setListEntity(DOWN_USER_KEY, downUserInfo);
        }
        ArrayList<InstagramUser> arrayList = new ArrayList<>();
        for (InstagramUser instagramUser2 : downUserInfo) {
            if (instagramUser.getUser_id() != instagramUser2.getUser_id()) {
                arrayList.add(instagramUser2);
            }
        }
        arrayList.add(Math.max(i - 1, 0), instagramUser);
        if (arrayList.size() > 0) {
            Collections.reverse(arrayList);
        }
        InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList);
    }

    public static List<InstagramUser> getDownUserInfo() {
        return InsManager.shared.getListEntity(DOWN_USER_KEY, InstagramUser.class);
    }

    public static List<InstagramUser> getReverseDownUserInfo() {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (downUserInfo != null && downUserInfo.size() > 0) {
            Collections.reverse(downUserInfo);
            return downUserInfo;
        }
        return new ArrayList<>();
    }

    public static List<InstagramUser> getAccountReverseUser() {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        ArrayList<InstagramUser> arrayList = new ArrayList<>();
        if (downUserInfo != null && downUserInfo.size() > 0) {
            for (InstagramUser instagramUser : downUserInfo) {
                if (instagramUser.getUser_type() != 1) {
                    arrayList.add(instagramUser);
                }
            }
        }
        return arrayList;
    }

    public static int getUserSize() {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (downUserInfo != null) {
            return downUserInfo.size();
        }
        return 0;
    }

    @Deprecated
    public static List<InstagramUser> getReverseDownUserInfo4() {
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (downUserInfo != null && downUserInfo.size() > 0) {
            Collections.reverse(downUserInfo);
            ArrayList<InstagramUser> arrayList = new ArrayList<>();
            for (InstagramUser instagramUser : downUserInfo) {
                if (arrayList.size() >= 10) {
                    break;
                }
                arrayList.add(instagramUser);
            }
            return arrayList;
        }
        return new ArrayList<>();
    }

    private static InstagramUser getEZMoneyUser() {
        return new InstagramUser(-1, -2L, "EzMoney", "EzMoney", "file:///android_asset/ez/icon_ez_money.gif");
    }

    public static void removeDownUser(InstagramUser instagramUser) {
        if (instagramUser == null) {
            return;
        }
        Context context = InsManager.context;
//        FirebaseHelper.onEvent(context, "deleteUser", instagramUser.getUsername() + "");
        List<InstagramUser> downUserInfo = getDownUserInfo();
        if (downUserInfo != null && downUserInfo.size() > 0) {
            ArrayList<InstagramUser> arrayList = new ArrayList<>();
            for (InstagramUser instagramUser2 : downUserInfo) {
                if (instagramUser2.getUser_id() != instagramUser.getUser_id()) {
                    arrayList.add(instagramUser2);
                }
            }
            if (arrayList.size() > 0) {
                InsManager.shared.setListEntity(DOWN_USER_KEY, arrayList);
                return;
            } else {
                InsManager.shared.remove(DOWN_USER_KEY);
                return;
            }
        }
        InsManager.shared.remove(DOWN_USER_KEY);
    }

    public static void removeAllUser() {
        InsManager.shared.remove(DOWN_USER_KEY);
    }
}