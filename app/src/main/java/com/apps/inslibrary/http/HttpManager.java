package com.apps.inslibrary.http;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.apps.inslibrary.InstagramRes;
import com.apps.inslibrary.LoginHelper;
import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.entity.InstagramUser;
import com.apps.inslibrary.entity.PageParams;
import com.apps.inslibrary.entity.login.LoginReel;
import com.apps.inslibrary.entity.login.LoginResult;
import com.apps.inslibrary.entity.login.ReelUser;
import com.apps.inslibrary.entity.login.UserInfoParams;
import com.apps.inslibrary.entity.shareEntity.CaptionNode;
import com.apps.inslibrary.entity.shareEntity.CarouselMedia;
import com.apps.inslibrary.entity.shareEntity.EdgeSidecarToChildren;
import com.apps.inslibrary.entity.shareEntity.EdgeSidecarToChildrenEdge;
import com.apps.inslibrary.entity.shareEntity.EdgeSidecarToChildrenEdgeNode;
import com.apps.inslibrary.entity.shareEntity.Items;
import com.apps.inslibrary.entity.shareEntity.ResponseModel;
import com.apps.inslibrary.entity.shareEntity.ResponseModel2;
import com.apps.inslibrary.entity.shareEntity.ShortcodeMedia;
import com.apps.inslibrary.entity.shareEntity.ShortcodeMediaOwner;
import com.apps.inslibrary.entity.shareEntity.VideoVersions;
import com.apps.inslibrary.entity.stroiesEntity.ImageVersions2;
import com.apps.inslibrary.entity.stroiesEntity.ReelsMedia;
import com.apps.inslibrary.entity.stroiesEntity.ReelsMediaItems;
import com.apps.inslibrary.entity.stroiesEntity.ReelsMediaResult;
import com.apps.inslibrary.entity.stroiesEntity.StoriesUserResult;
import com.apps.inslibrary.entity.userStories.CoverMedia;
import com.apps.inslibrary.entity.userStories.ReelsStoriesMedia;
import com.apps.inslibrary.entity.userStories.StoriesItems;
import com.apps.inslibrary.entity.userStories.StoriesReelsMediaResult;
import com.apps.inslibrary.entity.userStories.StoriesUser;
import com.apps.inslibrary.entity.userStories.Tray;
import com.apps.inslibrary.entity.userStories.UserStoriesResult;
import com.apps.inslibrary.entity.userinfo.GraphqlUser;
import com.apps.inslibrary.entity.userinfo.UserInfoPageResult;
import com.apps.inslibrary.entity.userinfo.UserInfoResult;
import com.apps.inslibrary.interfaces.HttpListener;
import com.apps.inslibrary.utils.JsonObjectUtils;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class HttpManager {
    private static final String APP_ID = "936619743392459";
    private static final String FOLLOW_APP_ID = "1217981644879628";
    private static final String KEY = "?__a=1";
    private static final String QUERY_HASH = "8c2a529969ee035a5063f2fc8602a0fd";
    private static final String QUERY_HASH_USER_INFO = "c9100bf9110dd6361671f113dd02e7d6";
    private static final String QUERY_RES = "7d4d42b121a214d23bd43206e5142c8c";
    private static final String QUERY_URL = "https://www.instagram.com/graphql/query/";
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

    public static void getShareData(final String str, final String str2, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(str + KEY);
        requestParams.addHeader(HttpHeaders.COOKIE, str2);
        requestParams.setConnectTimeout(10000);
        get(requestParams, new OnHttpCallback() { // from class: com.apps.inslibrary.http.HttpManager.1
            @Override // com.apps.inslibrary.http.HttpManager.OnHttpCallback
            public void onSuccess(String str3) {
                try {
                    InstagramData instagramData = new InstagramData();
                    ResponseModel responseModel = (ResponseModel) new Gson().fromJson(str3,ResponseModel.class);
                    if (responseModel != null && responseModel.getGraphql() != null) {
                        ShortcodeMedia shortcode_media = responseModel.getGraphql().getShortcode_media();
                        EdgeSidecarToChildren edge_sidecar_to_children = shortcode_media.getEdge_sidecar_to_children();
                        ShortcodeMediaOwner owner = responseModel.getGraphql().getShortcode_media().getOwner();
                        List<CaptionNode> edges = shortcode_media.getEdge_media_to_caption().getEdges();
                        instagramData.setId(shortcode_media.getId());
                        instagramData.setInstagramUser(new InstagramUser(0, owner.getId(), owner.getUsername(), owner.getFull_name(), owner.getProfile_pic_url()));
                        if (edges != null && edges.size() > 0) {
                            try {
                                instagramData.setDescribe(edges.get(0).getNode().getText());
                            } catch (Exception unused) {
                                unused.printStackTrace();
                            }
                        }
                        instagramData.setVideo(shortcode_media.isIs_video());
                        instagramData.setCoverUrl(shortcode_media.getDisplay_url());
                        ArrayList<InstagramRes> arrayList = new ArrayList<>();
                        if (shortcode_media.isIs_video()) {
                            String id = shortcode_media.getId();
                            String shortcode = shortcode_media.getShortcode();
                            boolean isIs_video = shortcode_media.isIs_video();
                            String video_url = shortcode_media.getVideo_url();
                            String display_url = shortcode_media.getDisplay_url();
                            InstagramRes instagramRes = new InstagramRes();
                            instagramRes.setId(id);
                            instagramRes.setDisplay_url(display_url);
                            instagramRes.setVideo_url(video_url);
                            instagramRes.setIs_video(isIs_video);
                            instagramRes.setShortcode(shortcode);
                            instagramRes.setViewUrl(str);
                            arrayList.add(instagramRes);
                            instagramData.setViewUrl(str);
                        } else if (edge_sidecar_to_children != null) {
                            List<EdgeSidecarToChildrenEdge> edges2 = edge_sidecar_to_children.getEdges();
                            for (int i = 0; i < edges2.size(); i++) {
                                EdgeSidecarToChildrenEdgeNode node = edges2.get(i).getNode();
                                String id2 = node.getId();
                                String shortcode2 = node.getShortcode();
                                String display_url2 = node.getDisplay_url();
                                boolean isIs_video2 = node.isIs_video();
                                String video_url2 = node.getVideo_url();
                                InstagramRes instagramRes2 = new InstagramRes();
                                instagramRes2.setId(id2);
                                instagramRes2.setDisplay_url(display_url2);
                                instagramRes2.setVideo_url(video_url2);
                                instagramRes2.setIs_video(isIs_video2);
                                instagramRes2.setShortcode(shortcode2);
                                instagramRes2.setViewUrl(str);
                                arrayList.add(instagramRes2);
                            }
                            instagramData.setViewUrl(str);
                        } else {
                            InstagramRes instagramRes3 = new InstagramRes();
                            instagramRes3.setId(shortcode_media.getId());
                            instagramRes3.setDisplay_url(shortcode_media.getDisplay_url());
                            instagramRes3.setVideo_url(shortcode_media.getVideo_url());
                            instagramRes3.setIs_video(shortcode_media.isIs_video());
                            instagramRes3.setShortcode(shortcode_media.getShortcode());
                            instagramRes3.setViewUrl(str);
                            arrayList.add(instagramRes3);
                            instagramData.setViewUrl(str);
                        }
                        instagramData.setInstagramRes(arrayList);
                    } else {
                        ResponseModel2 responseModel2 = (ResponseModel2) new Gson().fromJson(str3, ResponseModel2.class);
                        if (responseModel2 != null) {
                            Items items = responseModel2.getItems().get(0);
                            instagramData.setId(items.getId());
                            ShortcodeMediaOwner user = items.getUser();
                            instagramData.setInstagramUser(new InstagramUser(0, user.getPk(), user.getUsername(), user.getFull_name(), user.getProfile_pic_url()));
                            try {
                                instagramData.setDescribe(items.getCaption().getText());
                            } catch (Exception unused2) {
                                unused2.printStackTrace();
                            }
                            boolean z = true;
                            instagramData.setVideo(items.getMedia_type() == 2);
                            if (items.getImage_versions2() != null) {
                                instagramData.setCoverUrl(items.getImage_versions2().getCandidates().get(0).getUrl());
                            }
                            ArrayList<InstagramRes> arrayList2 = new ArrayList<>();
                            if (items.getMedia_type() == 2) {
                                List<VideoVersions> video_versions = items.getVideo_versions();
                                String id3 = items.getId();
                                if (items.getMedia_type() != 2) {
                                    z = false;
                                }
                                String url = items.getImage_versions2().getCandidates().get(0).getUrl();
                                InstagramRes instagramRes4 = new InstagramRes();
                                instagramRes4.setId(id3);
                                instagramRes4.setDisplay_url(url);
                                instagramRes4.setVideo_url(video_versions.get(0).getUrl());
                                instagramRes4.setIs_video(z);
                                instagramRes4.setShortcode(items.getCode());
                                instagramRes4.setViewUrl(str);
                                arrayList2.add(instagramRes4);
                            } else {
                                List<CarouselMedia> carousel_media = items.getCarousel_media();
                                if (carousel_media != null && carousel_media.size() > 0) {
                                    for (CarouselMedia carouselMedia : carousel_media) {
                                        String id4 = carouselMedia.getId();
                                        boolean z2 = carouselMedia.getMedia_type() == 2;
                                        String url2 = carouselMedia.getImage_versions2().getCandidates().get(0).getUrl();
                                        InstagramRes instagramRes5 = new InstagramRes();
                                        instagramRes5.setId(id4);
                                        instagramRes5.setDisplay_url(url2);
                                        instagramRes5.setVideo_url(url2);
                                        instagramRes5.setIs_video(z2);
                                        instagramRes5.setShortcode(items.getCode());
                                        instagramRes5.setViewUrl(str);
                                        arrayList2.add(instagramRes5);
                                    }
                                    if (TextUtils.isEmpty(instagramData.getCoverUrl())) {
                                        try {
                                            instagramData.setCoverUrl(carousel_media.get(0).getImage_versions2().getCandidates().get(0).getUrl());
                                        } catch (Exception unused3) {
                                        }
                                    }
                                } else {
                                    String id5 = items.getId();
                                    if (items.getMedia_type() != 2) {
                                        z = false;
                                    }
                                    InstagramRes instagramRes6 = new InstagramRes();
                                    ImageVersions2 image_versions2 = items.getImage_versions2();
                                    if (image_versions2 != null) {
                                        instagramRes6.setDisplay_url(image_versions2.getCandidates().get(0).getUrl());
                                    }
                                    instagramRes6.setId(id5);
                                    instagramRes6.setIs_video(z);
                                    instagramRes6.setShortcode(items.getCode());
                                    instagramRes6.setViewUrl(str);
                                    arrayList2.add(instagramRes6);
                                }
                            }
                            instagramData.setInstagramRes(arrayList2);
                        }
                    }
                    instagramData.setShareUrl(str);
                    instagramData.setViewUrl(str);
                     
                    if (httpListener == null) {
                        return;
                    }
                    httpListener.onSuccess(instagramData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "TAG-Throwable:onNext:" + e.toString());
                    if (httpListener == null) {
                        httpListener.onError("Please paste a valid Instagram link");
                    }

                }
            }

            @Override // com.apps.inslibrary.http.HttpManager.OnHttpCallback
            public void onError(String str3) {
                Log.e("TAG", "onError:" + str3);
                try {
                    String[] split = str.split("/");
                    HttpManager.queryAllData(split[split.length - 1], str2, httpListener);
                } catch (Exception unused) {
                     
                    if (httpListener == null) {
                        return;
                    }
                    httpListener.onError(str3);
                }
            }
        });
    }
    
    public static void queryAllData(final String str, String str2, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(QUERY_URL);
        requestParams.addQueryStringParameter("query_hash", QUERY_RES);
        requestParams.addQueryStringParameter("variables", "{\"shortcode\":\"" + str + "\"}");
        requestParams.addHeader(HttpHeaders.COOKIE, str2);
        requestParams.setConnectTimeout(10000);
        get(requestParams, new OnHttpCallback() { // from class: com.apps.inslibrary.http.HttpManager.2
            @Override // com.apps.inslibrary.http.HttpManager.OnHttpCallback
            public void onSuccess(String str3) {
                try {
                    InstagramData instagramData = new InstagramData();
                    ResponseModel responseModel = (ResponseModel) new Gson().fromJson(str3,  ResponseModel.class);
                    ShortcodeMedia shortcode_media = responseModel.getData().getShortcode_media();
                    EdgeSidecarToChildren edge_sidecar_to_children = shortcode_media.getEdge_sidecar_to_children();
                    ShortcodeMediaOwner owner = responseModel.getData().getShortcode_media().getOwner();
                    List<CaptionNode> edges = shortcode_media.getEdge_media_to_caption().getEdges();
                    instagramData.setId(shortcode_media.getId());
                    instagramData.setInstagramUser(new InstagramUser(0, owner.getId(), owner.getUsername(), owner.getFull_name(), owner.getProfile_pic_url()));
                    if (edges != null) {
                        try {
                            if (edges.size() > 0) {
                                instagramData.setDescribe(edges.get(0).getNode().getText());
                            }
                        } catch (Exception unused) {
                            unused.printStackTrace();
                        }
                    }
                    instagramData.setVideo(shortcode_media.isIs_video());
                    instagramData.setCoverUrl(shortcode_media.getDisplay_url());
                    ArrayList<InstagramRes> arrayList = new ArrayList<>();
                    if (shortcode_media.isIs_video()) {
                        String id = shortcode_media.getId();
                        String shortcode = shortcode_media.getShortcode();
                        boolean isIs_video = shortcode_media.isIs_video();
                        String video_url = shortcode_media.getVideo_url();
                        String display_url = shortcode_media.getDisplay_url();
                        InstagramRes instagramRes = new InstagramRes();
                        instagramRes.setId(id);
                        instagramRes.setDisplay_url(display_url);
                        instagramRes.setVideo_url(video_url);
                        instagramRes.setIs_video(isIs_video);
                        instagramRes.setShortcode(shortcode);
                        instagramRes.setViewUrl("https://www.instagram.com/p/" + shortcode);
                        arrayList.add(instagramRes);
                    } else if (edge_sidecar_to_children != null) {
                        List<EdgeSidecarToChildrenEdge> edges2 = edge_sidecar_to_children.getEdges();
                        for (int i = 0; i < edges2.size(); i++) {
                            EdgeSidecarToChildrenEdgeNode node = edges2.get(i).getNode();
                            String id2 = node.getId();
                            String shortcode2 = node.getShortcode();
                            String display_url2 = node.getDisplay_url();
                            boolean isIs_video2 = node.isIs_video();
                            String video_url2 = node.getVideo_url();
                            InstagramRes instagramRes2 = new InstagramRes();
                            instagramRes2.setId(id2);
                            instagramRes2.setDisplay_url(display_url2);
                            instagramRes2.setVideo_url(video_url2);
                            instagramRes2.setIs_video(isIs_video2);
                            instagramRes2.setShortcode(shortcode2);
                            instagramRes2.setViewUrl("https://www.instagram.com/p/" + shortcode2);
                            arrayList.add(instagramRes2);
                        }
                    } else {
                        InstagramRes instagramRes3 = new InstagramRes();
                        instagramRes3.setId(shortcode_media.getId());
                        instagramRes3.setDisplay_url(shortcode_media.getDisplay_url());
                        instagramRes3.setVideo_url(shortcode_media.getVideo_url());
                        instagramRes3.setIs_video(shortcode_media.isIs_video());
                        instagramRes3.setShortcode(shortcode_media.getShortcode());
                        instagramRes3.setViewUrl("https://www.instagram.com/p/" + str);
                        arrayList.add(instagramRes3);
                    }
                    instagramData.setViewUrl("https://www.instagram.com/p/" + str);
                    instagramData.setInstagramRes(arrayList);
                     
                    if (httpListener == null) {
                        return;
                    }
                    httpListener.onSuccess(instagramData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "TAG-Throwable:onNext:" + e.getMessage());
                     
                    if ( httpListener == null) {
                        return;
                    }
                     httpListener.onError(e.getMessage());
                }
            }

            @Override // com.apps.inslibrary.http.HttpManager.OnHttpCallback
            public void onError(String str3) {
                Log.e("TAG", "Throwable:" + str3);
                 
                if (httpListener != null) {
                    httpListener.onError(str3);
                }
            }
        });
    }
    public static void getStories(final String str, String str2, final String str3, final HttpListener<InstagramData> httpListener) {
        RequestParams requestParams = new RequestParams(str2 + KEY);
        requestParams.setConnectTimeout(10000);
        get(requestParams, new OnHttpCallback() {
            public void onSuccess(String str) {
                String id = ((StoriesUserResult) new Gson().fromJson(str, StoriesUserResult.class)).getUser().getId();
                HttpManager.queryReelData(str, HttpManager.STORIES_URL + id, str3, httpListener);
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
                try {
                    InstagramData instagramData = new InstagramData();
                    ReelsMediaResult reelsMediaResult = (ReelsMediaResult) new Gson().fromJson(str, ReelsMediaResult.class);
                    if (reelsMediaResult != null) {
                        ReelsMedia reelsMedia = reelsMediaResult.getReels_media().get(0);
                        List<ReelsMediaItems> items = reelsMedia.getItems();
                        ShortcodeMediaOwner user = reelsMedia.getUser();
                        InstagramUser instagramUser = new InstagramUser(0, user.getId(), user.getUsername(), user.getFull_name(), user.getProfile_pic_url());
                        instagramData.setId(String.valueOf(reelsMedia.getId()));
                        instagramData.setInstagramUser(instagramUser);
                        ArrayList<InstagramRes> arrayList = new ArrayList<>();
                        for (ReelsMediaItems next : items) {
                            if (str.equals(next.getPk())) {
                                InstagramRes instagramRes = new InstagramRes();
                                instagramRes.setId(next.getId());
                                int media_type = next.getMedia_type();
                                boolean z = true;
                                instagramRes.setIs_video(media_type == 2);
                                if (media_type != 2) {
                                    z = false;
                                }
                                instagramData.setVideo(z);
                                String url = next.getImage_versions2().getCandidates().get(0).getUrl();
                                instagramData.setCoverUrl(url);
                                instagramRes.setDisplay_url(url);
                                if (media_type == 2) {
                                    instagramRes.setVideo_url(next.getVideo_versions().get(0).getUrl());
                                }
                                if (next.getStory_feed_media() == null || next.getStory_feed_media().size() <= 0) {
                                    String str2 = "http://instagram.com/" + user.getUsername();
                                    instagramRes.setViewUrl(str2);
                                    instagramData.setViewUrl(str2);
                                } else {
                                    String str3 = "http://instagram.com/p/" + next.getStory_feed_media().get(0).getMedia_code();
                                    instagramRes.setViewUrl(str3);
                                    instagramData.setViewUrl(str3);
                                }
                                arrayList.add(instagramRes);
                            }
                        }
                        instagramData.setInstagramRes(arrayList);
                          
                        if (httpListener != null) {
                            httpListener.onSuccess(instagramData);
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
        RequestParams requestParams = new RequestParams(str + KEY);
        String cookies = LoginHelper.getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            requestParams.addHeader(HttpHeaders.COOKIE, cookies);
            requestParams.setConnectTimeout(10000);
            get(requestParams, new OnHttpCallback() {
                public void onSuccess(String str) {
                    try {
                        UserInfoResult userInfoResult = (UserInfoResult) new Gson().fromJson(str, UserInfoResult.class);
                        if (userInfoResult != null) {
                            GraphqlUser user = userInfoResult.getGraphql().getUser();
                              
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

    public static void queryUserStoriesTray(String str, final String str2, final HttpListener<InstagramData> httpListener) {
        final String str3;
        String str1;
        String str4 = "";
        try {
            str1 = Uri.parse(str).getQueryParameter("story_media_id");
            try {
                str4 = getStoriesTrayUrl(str1.split("_")[1]);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        } catch (Exception unused2) {
            str1 = str4;
        }
        str3 = str1;
        if (!TextUtils.isEmpty(str4)) {
            RequestParams requestParams = new RequestParams(str4);
            requestParams.addHeader("x-ig-app-id", APP_ID);
            requestParams.addHeader(HttpHeaders.COOKIE, str2);
            requestParams.setConnectTimeout(10000);
            get(requestParams, new OnHttpCallback() {
                public void onSuccess(String str) {
                    try {
                        List<Tray> tray = ((UserStoriesResult) new Gson().fromJson(str, UserStoriesResult.class)).getTray();
                        Tray tray2 = null;
                        Iterator<Tray> it = tray.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Tray next = it.next();
                            CoverMedia cover_media = next.getCover_media();
                            if (!TextUtils.isEmpty(cover_media.getMedia_id()) && cover_media.getMedia_id().equals(str3)) {
                                tray2 = next;
                                break;
                            }
                        }
                        if (tray2 != null) {
                            String id = tray2.getId();
                            HttpManager.queryUserStories(str3, str2, httpListener, Arrays.asList(id));
                            return;
                        }
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (Tray id2 : tray) {
                            arrayList.add(id2.getId());
                        }
                        HttpManager.queryUserStories(str3, str2, httpListener, arrayList);
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
    }

    public static void queryUserStories(final String str, String str2, final HttpListener<InstagramData> httpListener, List<String> list) {
        RequestParams storiesUrl = getStoriesUrl(list);
        storiesUrl.addHeader("x-ig-app-id", APP_ID);
        storiesUrl.addHeader(HttpHeaders.COOKIE, str2);
        storiesUrl.setConnectTimeout(10000);
        get(storiesUrl, new OnHttpCallback() {
            public void onSuccess(String str) {
                try {
                    InstagramData instagramData = new InstagramData();
                    ReelsStoriesMedia reelsStoriesMedia = null;
                    StoriesItems storiesItems = null;
                    for (ReelsStoriesMedia next : ((StoriesReelsMediaResult) new Gson().fromJson(str, StoriesReelsMediaResult.class)).getReels_media()) {
                        Iterator<StoriesItems> it = next.getItems().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            StoriesItems next2 = it.next();
                            if (next2.getId().equals(str)) {
                                reelsStoriesMedia = next;
                                storiesItems = next2;
                                break;
                            }
                        }
                    }
                    if (reelsStoriesMedia != null) {
                        instagramData.setId(reelsStoriesMedia.getId());
                        StoriesUser user = reelsStoriesMedia.getUser();
                        if (user != null) {
                            instagramData.setInstagramUser(new InstagramUser(0, user.getPk(), user.getUsername(), user.getFull_name(), user.getProfile_pic_url()));
                        }
                        boolean z = true;
                        instagramData.setVideo(storiesItems.getMedia_type() == 2);
                        instagramData.setDescribe("");
                        String url = storiesItems.getImage_versions2().getCandidates().get(0).getUrl();
                        instagramData.setCoverUrl(url);
                        ArrayList<InstagramRes> arrayList = new ArrayList<>();
                        int media_type = storiesItems.getMedia_type();
                        if (storiesItems.getMedia_type() == 2) {
                            String url2 = storiesItems.getVideo_versions().get(0).getUrl();
                            String id = storiesItems.getId();
                            if (media_type != 2) {
                                z = false;
                            }
                            arrayList.add(new InstagramRes(id, z, "", url2));
                        } else {
                            String id2 = storiesItems.getId();
                            if (media_type != 2) {
                                z = false;
                            }
                            arrayList.add(new InstagramRes(id2, z, "", url));
                        }
                        instagramData.setInstagramRes(arrayList);
                        if (httpListener != null) {
                            httpListener.onSuccess(instagramData);
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
    }

    public static void getUserFollows(String str, String str2, final HttpListener<List<InstagramUser>> httpListener) {
        RequestParams requestParams = new RequestParams("https://i.instagram.com/api/v1/friendships/" + str + "/following/?count=12");
        requestParams.addHeader("cookie", str2);
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
                      
                    if (httpListener != null) {
                        httpListener.onSuccess(arrayList);
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

    private static void get(RequestParams requestParams, final OnHttpCallback onHttpCallback) {
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
