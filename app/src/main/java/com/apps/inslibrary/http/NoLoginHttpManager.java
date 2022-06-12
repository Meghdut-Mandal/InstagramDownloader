package com.apps.inslibrary.http;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.apps.inslibrary.InstagramRes;
import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.entity2.NoLoginEntity;
import com.apps.inslibrary.entity2.NoLoginEntity2;
import com.apps.inslibrary.entity2.NoLoginRes2;
import com.apps.inslibrary.entity2.NoLoginResult;
import com.apps.inslibrary.entity2.NoLoginResult2;
import com.apps.inslibrary.interfaces.HttpListener;
import com.apps.inslibrary.utils.JsonObjectUtils;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NoLoginHttpManager {
    private static final String NO_LOGIN_URL_0 = "https://instaer.app/api/instagram/mediaDownload";
    private static final String NO_LOGIN_URL_1 = "https://api.anonshacker.com/api/instagram";
    private static final String NO_LOGIN_URL_2 = "https://fbion.com/extract";
    private static final String NO_LOGIN_URL_3 = "https://api.withno.xyz/api/ins_link";
    private static final String NO_LOGIN_URL_TOKEN = "OWZiYzQzYmExMzE1N2M1ZjJjZjM1NzcwOGUwNTRjN2M=";
    private static final String TMP_COOKIES_URL = "https://api.withno.xyz/api/get_cookie";

    public interface HttpCallBack {
        void onFailed(String str);

        void onSuccess(String str);
    }

    public static void getTmpCookies(final HttpCallBack httpCallBack) {
        post(new RequestParams(TMP_COOKIES_URL), new HttpCallBack() {
            public void onSuccess(String str) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull("data")) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                        if (!jSONObject2.isNull("cookies")) {
                            String string = jSONObject2.getString("cookies");
                            if (httpCallBack != null) {
                                httpCallBack.onSuccess(string);
                                return;
                            }
                            return;
                        }
                        if (httpCallBack != null) {
                            httpCallBack.onFailed("");
                            return;
                        }
                        return;
                    }
                    if (httpCallBack != null) {
                        httpCallBack.onFailed("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (httpCallBack != null) {
                        httpCallBack.onFailed("");
                    }
                }
            }

            public void onFailed(String str) {
                if (httpCallBack != null) {
                    httpCallBack.onFailed(str);
                }
            }
        });
    }

    public static void getShareData(final String str, final HttpListener<InstagramData> httpListener) {
        final String hostUrl = getHostUrl(str);
        final String shortcode = getShortcode(str);
        RequestParams requestParams = new RequestParams(NO_LOGIN_URL_0);
        requestParams.addHeader("visitor-token", NO_LOGIN_URL_TOKEN);
        requestParams.addBodyParameter("mediaUrl", hostUrl);
        post(requestParams, new HttpCallBack() {
            public void onSuccess(String str) {
                try {
                    List<NoLoginEntity> result = ((NoLoginResult) new Gson().fromJson(str, NoLoginResult.class)).getResult();
                    if (result == null || result.size() <= 0) {
                        NoLoginHttpManager.getShareData2(str, httpListener);
                        return;
                    }
                    InstagramData instagramData = new InstagramData();
                    instagramData.setId(shortcode);
                    instagramData.setDescribe("");
                    instagramData.setViewUrl(hostUrl);
                    instagramData.setShareUrl(hostUrl);
                    instagramData.setShortcode(shortcode);
                    ArrayList<InstagramRes> arrayList = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        NoLoginEntity noLoginEntity = result.get(i);
                        boolean equals = ExifInterface.GPS_MEASUREMENT_2D.equals(noLoginEntity.getType());
                        if (TextUtils.isEmpty(instagramData.getCoverUrl())) {
                            instagramData.setCoverUrl(noLoginEntity.getName());
                            instagramData.setVideo(equals);
                        }
                        arrayList.add(new InstagramRes(shortcode + i, equals, shortcode, noLoginEntity.getName()));
                    }
                    instagramData.setInstagramRes(arrayList);
                    if (httpListener != null) {
                        httpListener.onSuccess(instagramData);
                    }
                } catch (Exception unused) {
                    NoLoginHttpManager.getShareData2(str, httpListener);
                }
            }

            public void onFailed(String str) {
                NoLoginHttpManager.getShareData2(str, httpListener);
            }
        });
    }

    public static void getShareData2(String str, final HttpListener<InstagramData> httpListener) {
        final String hostUrl = getHostUrl(str);
        final String shortcode = getShortcode(str);
        RequestParams requestParams = new RequestParams(NO_LOGIN_URL_1);
        requestParams.addBodyParameter("mediaUrl", hostUrl);
        post(requestParams, new HttpCallBack() {
            public void onSuccess(String str) {
                try {
                    NoLoginEntity2 data = ((NoLoginResult2) new Gson().fromJson(str, NoLoginResult2.class)).getData();
                    if (data != null) {
                        List<NoLoginRes2> medias = data.getMedias();
                        InstagramData instagramData = new InstagramData();
                        instagramData.setId(shortcode);
                        instagramData.setViewUrl(hostUrl);
                        instagramData.setDescribe("");
                        instagramData.setShareUrl(hostUrl);
                        instagramData.setShortcode(shortcode);
                        ArrayList<InstagramRes> arrayList = new ArrayList<>();
                        for (int i = 0; i < medias.size(); i++) {
                            NoLoginRes2 noLoginRes2 = medias.get(i);
                            boolean isIs_video = noLoginRes2.isIs_video();
                            if (TextUtils.isEmpty(instagramData.getCoverUrl())) {
                                instagramData.setCoverUrl(noLoginRes2.getSrc());
                                instagramData.setVideo(isIs_video);
                            }
                            arrayList.add(new InstagramRes(shortcode + i, isIs_video, shortcode, noLoginRes2.getSrc()));
                        }
                        instagramData.setInstagramRes(arrayList);
                        if (httpListener != null) {
                            httpListener.onSuccess(instagramData);
                            return;
                        }
                        return;
                    }
                    if (httpListener != null) {
                        httpListener.onLogin(true);
                    }
                } catch (Exception unused) {
                    if (httpListener != null) {
                        httpListener.onLogin(true);
                    }
                }
            }

            public void onFailed(String str) {
                if (httpListener != null) {
                    httpListener.onError(str);
                }
            }
        });
    }

    public static void getShareData(int i, final String str, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(NO_LOGIN_URL_3);
        requestParams.addBodyParameter(ImagesContract.URL, str);
        requestParams.addBodyParameter("type", String.valueOf(i));
        post(requestParams, new HttpCallBack() {
            public void onSuccess(String str) {
                List<InstagramRes> list;
                try {
                    InstagramData instagramData = new InstagramData();
                    JSONObject jSONObject = new JSONObject(str);
                    if (!jSONObject.isNull(FirebaseAnalytics.Param.ITEMS)) {
                        JSONObject jSONObject2 = jSONObject.getJSONArray(FirebaseAnalytics.Param.ITEMS).getJSONObject(0);
                        instagramData.setId(JsonObjectUtils.parseInstagramID(jSONObject2));
                        String parseShortCode = JsonObjectUtils.parseShortCode(jSONObject2);
                        instagramData.setShortcode(parseShortCode);
                        boolean parseType = JsonObjectUtils.parseType(jSONObject2);
                        instagramData.setVideo(parseType);
                        instagramData.setInstagramUser(JsonObjectUtils.parseInstagramUser(jSONObject2));
                        instagramData.setDescribe(JsonObjectUtils.parseCaption(jSONObject2));
                        String parseCover = JsonObjectUtils.parseCover(jSONObject2);
                        instagramData.setCoverUrl(parseCover);
                        if (parseType) {
                            list = JsonObjectUtils.parseVideoUrl(jSONObject2, parseShortCode, parseCover);
                        } else {
                            list = JsonObjectUtils.parseImageUrl(jSONObject2, parseShortCode);
                        }
                        instagramData.setInstagramRes(new ArrayList<>(list));
                    }
                    instagramData.setShareUrl(str);
                    instagramData.setViewUrl(str);
                    if (httpListener != null) {
                        httpListener.onSuccess(instagramData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "TAG-Throwable:onNext:" + e.toString());
                    if (httpListener != null) {
                        httpListener.onError("Please paste a valid Instagram link");
                    }
                }
            }

            public void onFailed(String str) {
                Log.e("TAG", "onError:" + str);
                if (httpListener != null) {
                    httpListener.onError(str);
                }
            }
        });
    }

    public static void getUserStoriesData(int i, final String str, final HttpListener<InstagramData> httpListener) {
        final String queryParameter = Uri.parse(str).getQueryParameter("story_media_id");
        RequestParams requestParams = new RequestParams(NO_LOGIN_URL_3);
        requestParams.addBodyParameter(ImagesContract.URL, str);
        requestParams.addBodyParameter("type", String.valueOf(i));
        post(requestParams, new HttpCallBack() {
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
                                if (!jSONObject3.isNull(FirebaseAnalytics.Param.ITEMS)) {
                                    JSONArray jSONArray2 = jSONObject3.getJSONArray(FirebaseAnalytics.Param.ITEMS);
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 >= jSONArray2.length()) {
                                            break;
                                        }
                                        JSONObject jSONObject4 = jSONArray2.getJSONObject(i2);
                                        if (queryParameter.equals(jSONObject4.getString("id"))) {
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
                                instagramData.setInstagramRes(new ArrayList(list));
                                instagramData.setViewUrl(str);
                                instagramData.setShareUrl(str);
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

            public void onFailed(String str) {
                Log.e("TAG", "onError:" + str);
                if (httpListener != null) {
                    httpListener.onError(str);
                }
            }
        });
    }

    private static String getHostUrl(String str) {
        try {
            String str2 = str.split("\\?")[0];
            return !TextUtils.isEmpty(str2) ? str2 : str;
        } catch (Exception unused) {
            return str;
        }
    }

    private static String getShortcode(String str) {
        try {
            String[] split = str.split("\\?")[0].split("/");
            String str2 = split[split.length - 1];
            return TextUtils.isEmpty(str2) ? "" : str2;
        } catch (Exception unused) {
            return str;
        }
    }

    private static void post(RequestParams requestParams, final HttpCallBack httpCallBack) {
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            public void onCancelled(CancelledException cancelledException) {
            }

            public void onFinished() {
            }

            public void onSuccess(String str) {
                if (httpCallBack != null) {
                    httpCallBack.onSuccess(str);
                }
            }

            public void onError(Throwable th, boolean z) {
                if (httpCallBack != null) {
                    httpCallBack.onFailed(th.getMessage());
                }
            }
        });
    }

    private static void get(RequestParams requestParams, final HttpCallBack httpCallBack) {
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            public void onCancelled(CancelledException cancelledException) {
            }

            public void onFinished() {
            }

            public void onSuccess(String str) {
                if (httpCallBack != null) {
                    httpCallBack.onSuccess(str);
                }
            }

            public void onError(Throwable th, boolean z) {
                if (httpCallBack != null) {
                    httpCallBack.onFailed(th.getMessage());
                }
            }
        });
    }
}
