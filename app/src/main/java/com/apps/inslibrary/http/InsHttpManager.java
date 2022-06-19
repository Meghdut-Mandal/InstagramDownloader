package com.apps.inslibrary.http;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.apps.inslibrary.InstagramRes;
import com.apps.inslibrary.LoginHelper;
import com.apps.inslibrary.entity.FollowResult;
import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.entity.InstagramUser;
import com.apps.inslibrary.entity.PageParams;
import com.apps.inslibrary.entity.login.LoginReel;
import com.apps.inslibrary.entity.login.LoginResult;
import com.apps.inslibrary.entity.login.ReelUser;
import com.apps.inslibrary.entity.login.UserInfoParams;
import com.apps.inslibrary.entity.stories2.ReelsMediaStories2;
import com.apps.inslibrary.entity.stories2.Stories2Entity;
import com.apps.inslibrary.entity.stories2.Stories2Item;
import com.apps.inslibrary.entity.userStories.StoriesUser;
import com.apps.inslibrary.entity.userinfo.GraphqlUser;
import com.apps.inslibrary.entity.userinfo.UserInfoPageResult;
import com.apps.inslibrary.interfaces.HttpListener;
import com.apps.inslibrary.reelentity.ReelsEntity;
import com.apps.inslibrary.utils.JsonObjectUtils;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import org.xutils.x;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InsHttpManager {
    private static final String APP_ID = "936619743392459";
    private static final String FOLLOW_APP_ID = "1217981644879628";
    private static final String KEY = "?__a=1";
    private static final String QUERY_HASH = "8c2a529969ee035a5063f2fc8602a0fd";
    private static final String QUERY_HASH_USER_INFO = "c9100bf9110dd6361671f113dd02e7d6";
    private static final String QUERY_RES = "7d4d42b121a214d23bd43206e5142c8c";
    private static final String QUERY_URL = "https://www.instagram.com/graphql/query/";
    private static final String QUERY_USER_INFO_URL = "https://i.instagram.com/api/v1/users/web_profile_info/";
    public static final String STORIES_S = "https://i.instagram.com/api/v1/highlights/";
    private static final String STORIES_URL = "https://i.instagram.com/api/v1/feed/reels_media/?reel_ids=";
    private static final String STORIES_URL2 = "https://i.instagram.com/api/v1/feed/reels_media/";
    public static final String USER_FOLLOW_URL = "https://i.instagram.com/api/v1/friendships/";

    public interface OnHttpCallback {
        void onError(String str);

        void onSuccess(String str);
    }

    private static String getStoriesTrayUrl(String str) {
        return "https://i.instagram.com/api/v1/highlights/" + str + "/highlights_tray/";
    }

    private static RequestParams getStoriesUrl(List<String> list) {
        RequestParams requestParams = new RequestParams(STORIES_URL2);
        for (String addQueryStringParameter : list) {
            requestParams.addQueryStringParameter("reel_ids", addQueryStringParameter);
        }
        return requestParams;
    }

    public static void getShareData(String str, String str2, HttpListener<InstagramData> httpListener) {
        String[] split = str.split("/");
        queryAllData(str, split[split.length - 1], str2, httpListener);
    }

    public static void queryAllData(final String str, final String str2, String str3, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(QUERY_URL);
        requestParams.addQueryStringParameter("query_hash", QUERY_RES);
        requestParams.addQueryStringParameter("variables", "{\"shortcode\":\"" + str2 + "\"}");
        requestParams.addHeader(HttpHeaders.COOKIE, str3);
        requestParams.setConnectTimeout(10000);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                List<InstagramRes> list;
                try {
                    InstagramData instagramData = new InstagramData();
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("data")) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                        if (!jSONObject2.isNull("shortcode_media")) {
                            JSONObject jSONObject3 = jSONObject2.getJSONObject("shortcode_media");
                            instagramData.setId(JsonObjectUtils.parseInstagramID(jSONObject3));
                            String parseShortCode = JsonObjectUtils.parseShortCode(jSONObject3);
                            instagramData.setShortcode(parseShortCode);
                            boolean parseType = JsonObjectUtils.parseType(jSONObject3);
                            instagramData.setVideo(parseType);
                            instagramData.setInstagramUser(JsonObjectUtils.parseInstagramUser(jSONObject3));
                            instagramData.setDescribe(JsonObjectUtils.parseCaption(jSONObject3));
                            String parseCover = JsonObjectUtils.parseCover(jSONObject3);
                            instagramData.setCoverUrl(parseCover);
                            if (parseType) {
                                list = JsonObjectUtils.parseVideoUrl(jSONObject3, str2, parseCover);
                            } else {
                                list = JsonObjectUtils.parseImageUrl(jSONObject3, parseShortCode);
                            }
                            instagramData.setInstagramRes(new ArrayList<>(list));
                        }
                    }
                    instagramData.setViewUrl(str);
                    instagramData.setShareUrl(str);
                    if (httpListener != null) {
                        httpListener.onSuccess(instagramData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "TAG-Throwable:onNext:" + e.getMessage());
                    if (httpListener != null) {
                        httpListener.onError(e.getMessage());
                    }
                }
            }

            public void onError(String str) {
                Log.e("TAG", "Throwable:" + str);
                if (httpListener != null) {
                    httpListener.onError(str);
                }
            }
        });
    }

  
    /* access modifiers changed from: private */
    public static void queryReelData(final String str, String str2, String str3, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(str2);
        requestParams.addHeader("x-ig-app-id", APP_ID);
        requestParams.addHeader(HttpHeaders.COOKIE, str3);
        requestParams.setConnectTimeout(10000);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                List<InstagramRes> list;
                try {
                    InstagramData instagramData = new InstagramData();
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("reels_media")) {
                        JSONObject jSONObject2 = jSONObject.getJSONArray("reels_media").getJSONObject(0);
                        instagramData.setId(JsonObjectUtils.parseInstagramID(jSONObject2));
                        if (!jSONObject2.isNull("user")) {
                            instagramData.setInstagramUser(JsonObjectUtils.parseInstagramUser(jSONObject2));
                        }
                        if (!jSONObject2.isNull("items")) {
                            JSONArray jSONArray = jSONObject2.getJSONArray("items");
                            if (jSONArray.length() > 0) {
                                for (int i = 0; i < jSONArray.length(); i++) {
                                    JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                                    if (!jSONObject3.isNull("pk")) {
                                        if (str.equals(jSONObject3.getString("pk"))) {
                                            String parseShortCode = JsonObjectUtils.parseShortCode(jSONObject3);
                                            instagramData.setShortcode(parseShortCode);
                                            boolean parseType = JsonObjectUtils.parseType(jSONObject3);
                                            instagramData.setVideo(parseType);
                                            instagramData.setDescribe(JsonObjectUtils.parseCaption(jSONObject3));
                                            String parseCover = JsonObjectUtils.parseCover(jSONObject3);
                                            instagramData.setCoverUrl(parseCover);
                                            if (parseType) {
                                                list = JsonObjectUtils.parseVideoUrl(jSONObject3, parseShortCode, parseCover);
                                            } else {
                                                list = JsonObjectUtils.parseImageUrl(jSONObject3, parseShortCode);
                                            }
                                            instagramData.setInstagramRes(new ArrayList<>(list));
                                            String str2 = "http://instagram.com/" + instagramData.getInstagramUser().getUsername();
                                            instagramData.setViewUrl(str2);
                                            instagramData.setShareUrl(str2);
                                            if (httpListener != null) {
                                                httpListener.onSuccess(instagramData);
                                                return;
                                            }
                                            return;
                                        }
                                    }
                                }
                                return;
                            }
                            if (httpListener != null) {
                                httpListener.onError("The result is empty");
                                return;
                            }
                            return;
                        }
                        if (httpListener!= null) {
                            httpListener.onError("The result is empty");
                            return;
                        }
                        return;
                    }
                    if (httpListener != null) {
                        httpListener.onError("The result is empty");
                    }
                } catch (Exception e) {
                    if (httpListener != null) {
                        httpListener.onError(e.getMessage());
                    }
                }
            }

            public void onError(String str) {
                Log.e("TAG", "Throwable:" + str);
                if (httpListener != null) {
                    httpListener.onError(str);
                }
            }
        });
    }

    public static void queryUserInfoData(String str, final HttpListener<GraphqlUser> httpListener) {
        String cookies = LoginHelper.getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            RequestParams requestParams = new RequestParams(QUERY_USER_INFO_URL);
            requestParams.addQueryStringParameter("username", str);
            requestParams.addHeader(HttpHeaders.COOKIE, cookies);
            requestParams.addHeader("x-ig-app-id", APP_ID);
            requestParams.setConnectTimeout(10000);
            get(requestParams, new OnHttpCallback() {
                public void onSuccess(String str) {
                    try {
                        UserInfoPageResult userInfoPageResult = new Gson().fromJson(str, UserInfoPageResult.class);
                        if (userInfoPageResult != null) {
                            GraphqlUser user = userInfoPageResult.getData().getUser();
                            if (httpListener != null) {
                                httpListener.onSuccess(user);
                                return;
                            }
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("The result is empty");
                        }
                    } catch (Exception e) {
                        if (httpListener != null) {
                            httpListener.onError(e.getMessage());
                        }
                    }
                }

                public void onError(String str) {
                    Log.e("TAG", "Throwable:" + str);
                    if (httpListener != null) {
                        httpListener.onError(str);
                    }
                }
            });
        } else if (httpListener != null) {
            httpListener.onLogin(true);
        }
    }

    public static boolean isNotNullJSONObject(JSONObject jSONObject, String str) {
        return !jSONObject.isNull(str);
    }

    public static void queryUserInfoDataPage(String str, int i, String str2, final HttpListener<GraphqlUser> httpListener) {
        RequestParams requestParams = new RequestParams(QUERY_URL);
        requestParams.addQueryStringParameter("query_hash", QUERY_HASH);
        requestParams.addQueryStringParameter("variables", new PageParams(str, i, str2).toString());
        String cookies = LoginHelper.getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            requestParams.addHeader(HttpHeaders.COOKIE, cookies);
            requestParams.setConnectTimeout(10000);
            get(requestParams, new OnHttpCallback() {
                public void onSuccess(String str) {
                    try {
                        UserInfoPageResult userInfoPageResult = (UserInfoPageResult) new Gson().fromJson(str, UserInfoPageResult.class);
                        if (userInfoPageResult != null) {
                            GraphqlUser user = userInfoPageResult.getData().getUser();
                            if (httpListener != null) {
                                httpListener.onSuccess(user);
                                return;
                            }
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("The result is empty");
                        }
                    } catch (Exception e) {
                        if (httpListener != null) {
                            httpListener.onError(e.getMessage());
                        }
                    }
                }

                public void onError(String str) {
                    Log.e("TAG", "Throwable:" + str);
                    if (httpListener != null) {
                        httpListener.onError(str);
                    }
                }
            });
        } else if (httpListener != null) {
            httpListener.onLogin(true);
        }
    }

    public static void queryUserInfoByUserID(String str, final HttpListener<ReelUser> httpListener) {
        RequestParams requestParams = new RequestParams(QUERY_URL);
        requestParams.addQueryStringParameter("query_hash", QUERY_HASH_USER_INFO);
        requestParams.addQueryStringParameter("variables", new Gson().toJson((Object) new UserInfoParams(str)));
        String cookies = LoginHelper.getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            requestParams.addHeader(HttpHeaders.COOKIE, cookies);
            requestParams.setConnectTimeout(10000);
            get(requestParams, new OnHttpCallback() {
                public void onSuccess(String str) {
                    try {
                        Log.e("TAG", "结果：" + str);
                        LoginResult loginResult = (LoginResult) new Gson().fromJson(str, LoginResult.class);
                        if (loginResult != null) {
                            LoginReel reel = loginResult.getData().getUser().getReel();
                            if (reel.getUser() != null) {
                                if (httpListener != null) {
                                    httpListener.onSuccess(reel.getUser());
                                    return;
                                }
                                return;
                            }
                            if (httpListener != null) {
                                httpListener.onSuccess(reel.getOwner());
                                return;
                            }
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("The result is empty");
                        }
                    } catch (Exception e) {
                        if (httpListener != null) {
                            httpListener.onError(e.getMessage());
                        }
                    }
                }

                public void onError(String str) {
                    Log.e("TAG", "Throwable:" + str);
                    if (httpListener != null) {
                        httpListener.onError(str);
                    }
                }
            });
        } else if (httpListener != null) {
            httpListener.onLogin(true);
        }
    }

    @Deprecated
    public static void queryUserStoriesTray(String str, final String str2, final HttpListener<InstagramData> httpListener) {
        final String queryParameter = Uri.parse(str).getQueryParameter("story_media_id");
        if (!TextUtils.isEmpty(queryParameter)) {
            String pkID = getPkID(queryParameter);
            if (!TextUtils.isEmpty(pkID)) {
                RequestParams requestParams = new RequestParams(getStoriesTrayUrl(pkID));
                requestParams.addHeader("x-ig-app-id", APP_ID);
                requestParams.addHeader(HttpHeaders.COOKIE, str2);
                requestParams.setConnectTimeout(10000);
                get(requestParams, new OnHttpCallback() {
                    public void onSuccess(String str) {
                        try {
                            JSONObject jSONObject = new JSONObject(str);
                            if (!jSONObject.isNull("tray")) {
                                JSONArray jSONArray = jSONObject.getJSONArray("tray");
                                if (jSONArray.length() > 0) {
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    for (int i = 0; i < jSONArray.length(); i++) {
                                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                        if (!jSONObject2.isNull("id")) {
                                            arrayList.add(jSONObject2.getString("id"));
                                        }
                                    }
                                    InsHttpManager.queryUserStories(queryParameter, str2, arrayList, httpListener);
                                    return;
                                }
                                if (httpListener != null) {
                                    httpListener.onError("Please paste a valid Instagram link");
                                    return;
                                }
                                return;
                            }
                            if (httpListener != null) {
                                httpListener.onError("Please paste a valid Instagram link");
                            }
                        } catch (Exception e) {
                            Log.e("TAG", "TAG_ERROR:" + e.getMessage());
                            if (httpListener != null) {
                                httpListener.onError("Please paste a valid Instagram link");
                            }
                        }
                    }

                    public void onError(String str) {
                        if (httpListener != null) {
                            httpListener.onError("Please paste a valid Instagram link");
                        }
                    }
                });
            } else if (httpListener != null) {
                httpListener.onError("Please paste a valid Instagram link");
            }
        } else if (httpListener != null) {
            httpListener.onError("Please paste a valid Instagram link");
        }
    }

    public static void queryUserStories(final String str, String str2, List<String> list, final HttpListener<InstagramData> httpListener) {
        RequestParams storiesUrl = getStoriesUrl(list);
        storiesUrl.addHeader("x-ig-app-id", APP_ID);
        storiesUrl.addHeader(HttpHeaders.COOKIE, str2);
        storiesUrl.setConnectTimeout(10000);
        get(storiesUrl, new OnHttpCallback() {
            public void onSuccess(String str) {
                List<InstagramRes> list;
                try {
                    InstagramData instagramData = new InstagramData();
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("reels_media")) {
                        JSONArray jSONArray = jSONObject.getJSONArray("reels_media");
                        if (jSONArray.length() > 0) {
                            JSONObject jSONObject2 = null;
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                                if (!jSONObject3.isNull("items")) {
                                    JSONArray jSONArray2 = jSONObject3.getJSONArray("items");
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 >= jSONArray2.length()) {
                                            break;
                                        }
                                        JSONObject jSONObject4 = jSONArray2.getJSONObject(i2);
                                        if (str.equals(jSONObject4.getString("id"))) {
                                            instagramData.setInstagramUser(JsonObjectUtils.parseInstagramUser(jSONObject3));
                                            jSONObject2 = jSONObject4;
                                            break;
                                        }
                                        i2++;
                                    }
                                }
                            }
                            if (jSONObject2 != null) {
                                instagramData.setId(JsonObjectUtils.parseInstagramID(jSONObject2));
                                String parseShortCode = JsonObjectUtils.parseShortCode(jSONObject2);
                                instagramData.setShortcode(parseShortCode);
                                boolean parseType = JsonObjectUtils.parseType(jSONObject2);
                                instagramData.setVideo(parseType);
                                instagramData.setDescribe(JsonObjectUtils.parseCaption(jSONObject2));
                                String parseCover = JsonObjectUtils.parseCover(jSONObject2);
                                instagramData.setCoverUrl(parseCover);
                                if (parseType) {
                                    list = JsonObjectUtils.parseVideoUrl(jSONObject2, parseShortCode, parseCover);
                                } else {
                                    list = JsonObjectUtils.parseImageUrl(jSONObject2, parseShortCode);
                                }
                                instagramData.setInstagramRes(new ArrayList<>(list));
                                String str2 = "http://instagram.com/" + instagramData.getInstagramUser().getUsername();
                                instagramData.setViewUrl(str2);
                                instagramData.setShareUrl(str2);
                                if (httpListener != null) {
                                    httpListener.onSuccess(instagramData);
                                    return;
                                }
                                return;
                            }
                            if (httpListener != null) {
                                httpListener.onError("The result is empty");
                                return;
                            }
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("The result is empty");
                            return;
                        }
                        return;
                    }
                    if (httpListener != null) {
                        httpListener.onError("The result is empty");
                    }
                } catch (Exception e) {
                    if (httpListener != null) {
                        httpListener.onError(e.getMessage());
                    }
                }
            }

            public void onError(String str) {
                if (httpListener != null) {
                    httpListener.onError("Please paste a valid Instagram link");
                }
            }
        });
    }

    public static void getUserFollows(String str, String str2, HttpListener<FollowResult> httpListener) {
        getUserFollows(str, 5, "0", str2, httpListener);
    }

    public static void getUserFollows(String str, int i, String str2, String str3, final HttpListener<FollowResult> httpListener) {
        RequestParams requestParams = new RequestParams("https://i.instagram.com/api/v1/friendships/" + str + "/following/");
        requestParams.addQueryStringParameter("count", String.valueOf(i));
        requestParams.addQueryStringParameter("max_id", str2);
        requestParams.addHeader("cookie", str3);
        requestParams.addHeader("x-ig-app-id", FOLLOW_APP_ID);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                try {
                    Log.e("TAG", "result:" + str);
                    ArrayList<InstagramUser> arrayList = new ArrayList<>();
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("users")) {
                        JSONArray jSONArray = jSONObject.getJSONArray("users");
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            if (!jSONObject2.isNull("username")) {
                                InstagramUser parseInstagramUser = JsonObjectUtils.parseInstagramUser(jSONObject2);
                                parseInstagramUser.setUser_type(1);
                                arrayList.add(parseInstagramUser);
                            }
                        }
                    }
                    FollowResult followResult = new FollowResult();
                    followResult.setUsers(arrayList);
                    if (!jSONObject.isNull("next_max_id")) {
                        followResult.setMax_id(jSONObject.getString("next_max_id"));
                    }
                    if (httpListener != null) {
                        httpListener.onSuccess(followResult);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onError(String str) {
                if (httpListener != null) {
                    httpListener.onError("Please paste a valid Instagram link");
                }
            }
        });
    }

    public static void getReelsTrayData(String str, final HttpListener<List<ReelsEntity>> httpListener) {
        RequestParams requestParams = new RequestParams("https://i.instagram.com/api/v1/feed/reels_tray/");
        requestParams.addHeader("cookie", str);
        requestParams.addHeader("x-ig-app-id", APP_ID);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("tray")) {
                        ArrayList<ReelsEntity> arrayList = new ArrayList<>();
                        JSONArray jSONArray = jSONObject.getJSONArray("tray");
                        if (jSONArray.length() > 0) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                ReelsEntity reelsEntity = new ReelsEntity();
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                if (!jSONObject2.isNull("id")) {
                                    reelsEntity.setId(jSONObject2.getLong("id"));
                                }
                                if (!jSONObject2.isNull("latest_reel_media")) {
                                    reelsEntity.setLatestTime(jSONObject2.getLong("latest_reel_media"));
                                }
                                if (!jSONObject2.isNull("user")) {
                                    JSONObject jSONObject3 = jSONObject2.getJSONObject("user");
                                    if (!jSONObject3.isNull("profile_pic_url")) {
                                        reelsEntity.setUserHead(jSONObject3.getString("profile_pic_url"));
                                    }
                                    if (!jSONObject3.isNull("username")) {
                                        reelsEntity.setUserName(jSONObject3.getString("username"));
                                    }
                                }
                                arrayList.add(reelsEntity);
                            }
                            if (httpListener != null) {
                                httpListener.onSuccess(arrayList);
                                return;
                            }
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("");
                            return;
                        }
                        return;
                    }
                    if (httpListener != null) {
                        httpListener.onError("");
                    }
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                    if (httpListener != null) {
                        httpListener.onError(e.toString());
                    }
                }
            }

            public void onError(String str) {
                Log.e("TAG", str);
                if (httpListener != null) {
                    httpListener.onError("");
                }
            }
        });
    }


    public static void getHomeStories(final String str, final String str2, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams("https://i.instagram.com/api/v1/feed/reels_tray/");
        requestParams.addHeader("cookie", str);
        requestParams.addHeader("x-ig-app-id", APP_ID);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("tray")) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        JSONArray jSONArray = jSONObject.getJSONArray("tray");
                        if (jSONArray.length() > 0) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                if (!jSONObject2.isNull("id")) {
                                    arrayList.add(String.valueOf(jSONObject2.getLong("id")));
                                }
                            }
                            InsHttpManager.getHomeStoriesById(str, str2, arrayList, httpListener);
                            return;
                        }
                        if (httpListener != null) {
                            httpListener.onError("");
                            return;
                        }
                        return;
                    }
                    if (httpListener != null) {
                        httpListener.onError("");
                    }
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                    if (httpListener != null) {
                        httpListener.onError(e.toString());
                    }
                }
            }

            public void onError(String str) {
                Log.e("TAG", str);
                if (httpListener != null) {
                    httpListener.onError("");
                }
            }
        });
    }

    public static void getHomeStoriesById(String str, String str2, List<String> list, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(STORIES_URL2);
        for (String addQueryStringParameter : list) {
            requestParams.addQueryStringParameter("reel_ids", addQueryStringParameter);
        }
        final String storiesId = getStoriesId(str2);
        requestParams.addHeader("cookie", str);
        requestParams.addHeader("x-ig-app-id", APP_ID);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                boolean z;
                boolean z2;
                try {
                    Iterator<ReelsMediaStories2> it = ((Stories2Entity) new Gson().fromJson(str, Stories2Entity.class)).getReels_media().iterator();
                    ReelsMediaStories2 reelsMediaStories2 = null;
                    Stories2Item stories2Item = null;
                    while (true) {
                        z = true;
                        if (!it.hasNext()) {
                            break;
                        }
                        ReelsMediaStories2 next = it.next();
                        Iterator<Stories2Item> it2 = next.getItems().iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                z2 = false;
                                continue;
                            }
                            Stories2Item next2 = it2.next();
                            if (storiesId.equals(next2.getPk())) {
                                stories2Item = next2;
                                z2 = true;
                                continue;
                            }
                        }
                    }
                    if (stories2Item != null) {
                        InstagramData instagramData = new InstagramData();
                        StoriesUser user = reelsMediaStories2.getUser();
                        String username = user.getUsername();
                        String full_name = user.getFull_name();
                        String profile_pic_url = user.getProfile_pic_url();
                        long pk = user.getPk();
                        InstagramUser instagramUser = new InstagramUser();
                        instagramUser.setUsername(username);
                        instagramUser.setFull_name(full_name);
                        instagramUser.setProfile_pic_url(profile_pic_url);
                        instagramUser.setUser_type(0);
                        instagramUser.setUser_id(pk);
                        instagramData.setInstagramUser(instagramUser);
                        String code = stories2Item.getCode();
                        ArrayList<InstagramRes> arrayList = new ArrayList<>();
                        InstagramRes instagramRes = new InstagramRes();
                        String id = stories2Item.getId();
                        String url = stories2Item.getImage_versions2().getCandidates().get(0).getUrl();
                        int media_type = stories2Item.getMedia_type();
                        String url2 = stories2Item.getVideo_versions().get(0).getUrl();
                        instagramRes.setShortcode(code);
                        instagramRes.setId(id);
                        instagramRes.setIs_video(media_type == 2);
                        instagramRes.setViewUrl("http://instagram.com/" + username);
                        instagramRes.setDisplay_url(url);
                        if (media_type == 2) {
                            instagramRes.setVideo_url(url2);
                        }
                        instagramData.setCoverUrl(url);
                        if (media_type != 2) {
                            z = false;
                        }
                        instagramData.setVideo(z);
                        instagramData.setShortcode(code);
                        instagramData.setId(String.valueOf(reelsMediaStories2.getId()));
                        instagramData.setShareUrl("http://instagram.com/" + username);
                        instagramData.setViewUrl("http://instagram.com/" + username);
                        arrayList.add(instagramRes);
                        instagramData.setInstagramRes(arrayList);
                        if (httpListener != null) {
                            httpListener.onSuccess(instagramData);
                        }
                    }
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                    if (httpListener != null) {
                        httpListener.onError("Service is temporarily unavailable");
                    }
                }
            }

            public void onError(String str) {
                Log.e("TAG", str);
                if (httpListener != null) {
                    httpListener.onError("");
                }
            }
        });
    }

    public static String getStoriesId(String str) {
        String str2 = str.split("\\?")[0];
        if (!str2.endsWith("/")) {
            str2 = str2 + "/";
        }
        String[] split = str2.split("/");
        return split[split.length - 1];
    }

    private static String getPkID(String str) {
        try {
            return str.split("_")[1];
        } catch (Exception unused) {
            return "";
        }
    }

    private static void get(RequestParams requestParams, final OnHttpCallback onHttpCallback) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            public void onCancelled(CancelledException cancelledException) {
            }

            public void onFinished() {
            }

            public void onSuccess(String str) {
                if (onHttpCallback != null) {
                    onHttpCallback.onSuccess(str);
                }
            }

            public void onError(Throwable th, boolean z) {
                Log.e("TAG", th.toString());
                if (onHttpCallback != null) {
                    onHttpCallback.onError(th.getMessage());
                }
            }
        });
    }
}
