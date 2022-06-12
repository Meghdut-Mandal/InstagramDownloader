package com.apps.inslibrary.utils;

import com.apps.inslibrary.InstagramRes;
import com.apps.inslibrary.entity.InstagramUser;
import com.apps.inslibrary.http.TtmlNode;
import com.google.android.gms.common.internal.ImagesContract;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonObjectUtils {
    public static String parseInstagramID(JSONObject jSONObject) {
        try {
            if (!jSONObject.isNull(TtmlNode.ATTR_ID)) {
                return jSONObject.getString(TtmlNode.ATTR_ID);
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static String parseShortCode(JSONObject jSONObject) {
        String string;
        try {
            if (!jSONObject.isNull("code")) {
                string = jSONObject.getString("code");
            } else if (jSONObject.isNull("shortcode")) {
                return "";
            } else {
                string = jSONObject.getString("shortcode");
            }
            return string;
        } catch (Exception unused) {
            return "";
        }
    }

    public static boolean parseType(JSONObject jSONObject) {
        try {
            if (jSONObject.isNull("media_type")) {
                if (!jSONObject.isNull("is_video")) {
                    return jSONObject.getBoolean("is_video");
                }
                return false;
            } else if (jSONObject.getInt("media_type") == 2) {
                return true;
            } else {
                return false;
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public static List<InstagramRes> parseVideoUrl(JSONObject jSONObject, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        try {
            if (!jSONObject.isNull("video_versions")) {
                JSONArray jSONArray = jSONObject.getJSONArray("video_versions");
                if (jSONArray.length() > 0) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(0);
                    InstagramRes instagramRes = new InstagramRes(jSONObject2.getString(TtmlNode.ATTR_ID), true, str, jSONObject2.getString(ImagesContract.URL));
                    instagramRes.setDisplay_url(str2);
                    arrayList.add(instagramRes);
                }
            } else if (!jSONObject.isNull("video_url")) {
                InstagramRes instagramRes2 = new InstagramRes(jSONObject.getString(TtmlNode.ATTR_ID), true, str, jSONObject.getString("video_url"));
                instagramRes2.setDisplay_url(str2);
                arrayList.add(instagramRes2);
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public static List<InstagramRes> parseImageUrl(JSONObject jSONObject, String str) {
        ArrayList<InstagramRes> arrayList = new ArrayList<>();
        try {
            if (!jSONObject.isNull("carousel_media")) {
                JSONArray jSONArray = jSONObject.getJSONArray("carousel_media");
                if (jSONArray.length() > 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        String string = jSONObject2.getString(TtmlNode.ATTR_ID);
                        int i2 = jSONObject2.getInt("media_type");
                        JSONArray jSONArray2 = jSONObject2.getJSONObject("image_versions2").getJSONArray("candidates");
                        if (jSONArray2.length() > 0) {
                            arrayList.add(new InstagramRes(string, i2 == 2, str, jSONArray2.getJSONObject(0).getString(ImagesContract.URL)));
                        }
                    }
                }
                return arrayList;
            }
            if (!jSONObject.isNull("edge_sidecar_to_children")) {
                JSONArray jSONArray3 = jSONObject.getJSONObject("edge_sidecar_to_children").getJSONArray("edges");
                if (jSONArray3.length() > 0) {
                    for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                        JSONObject jSONObject3 = jSONArray3.getJSONObject(i3).getJSONObject("node");
                        arrayList.add(new InstagramRes(parseShortCode(jSONObject3), parseType(jSONObject3), parseShortCode(jSONObject3), jSONObject.getString("display_url")));
                    }
                }
            } else if (!jSONObject.isNull("image_versions2")) {
                String parseInstagramID = parseInstagramID(jSONObject);
                JSONArray jSONArray4 = jSONObject.getJSONObject("image_versions2").getJSONArray("candidates");
                if (jSONArray4.length() > 0) {
                    arrayList.add(new InstagramRes(parseInstagramID, false, str, jSONArray4.getJSONObject(0).getString(ImagesContract.URL)));
                }
            } else if (!jSONObject.isNull("display_url")) {
                arrayList.add(new InstagramRes(parseInstagramID(jSONObject), false, str, jSONObject.getString("display_url")));
            }
            return arrayList;
        } catch (Exception unused) {
        }
        return null;
    }

    public static String parseCover(JSONObject jSONObject) {
        try {
            if (!jSONObject.isNull("display_url")) {
                return jSONObject.getString("display_url");
            }
            if (!jSONObject.isNull("image_versions2")) {
                JSONArray jSONArray = jSONObject.getJSONObject("image_versions2").getJSONArray("candidates");
                if (jSONArray.length() > 0) {
                    return jSONArray.getJSONObject(0).getString(ImagesContract.URL);
                }
                return "";
            } else if (!jSONObject.isNull("carousel_media")) {
                JSONArray jSONArray2 = jSONObject.getJSONArray("carousel_media");
                if (jSONArray2.length() <= 0) {
                    return "";
                }
                JSONArray jSONArray3 = jSONArray2.getJSONObject(0).getJSONObject("image_versions2").getJSONArray("candidates");
                if (jSONArray3.length() > 0) {
                    return jSONArray3.getJSONObject(0).getString(ImagesContract.URL);
                }
                return "";
            } else if (jSONObject.isNull("image_versions2")) {
                return "";
            } else {
                JSONArray jSONArray4 = jSONObject.getJSONObject("image_versions2").getJSONArray("candidates");
                if (jSONArray4.length() > 0) {
                    return jSONArray4.getJSONObject(0).getString(ImagesContract.URL);
                }
                return "";
            }
        } catch (Exception unused) {
            return "";
        }
    }

    public static InstagramUser parseInstagramUser(JSONObject jSONObject) {
        InstagramUser instagramUser = new InstagramUser();
        try {
            if (!jSONObject.isNull("user")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("user");
                instagramUser.setUser_id(parsePK(jSONObject2));
                instagramUser.setUsername(parseUserName(jSONObject2));
                instagramUser.setFull_name(parseFullName(jSONObject2));
                instagramUser.setProfile_pic_url(parseProfilePicUrl(jSONObject2));
            } else if (!jSONObject.isNull("owner")) {
                JSONObject jSONObject3 = jSONObject.getJSONObject("owner");
                if (!jSONObject3.isNull(TtmlNode.ATTR_ID)) {
                    instagramUser.setUser_id(Long.parseLong(jSONObject3.getString(TtmlNode.ATTR_ID)));
                }
                instagramUser.setUsername(parseUserName(jSONObject3));
                instagramUser.setFull_name(parseFullName(jSONObject3));
                instagramUser.setProfile_pic_url(parseProfilePicUrl(jSONObject3));
            } else {
                instagramUser.setUser_id(parsePK(jSONObject));
                instagramUser.setUsername(parseUserName(jSONObject));
                instagramUser.setFull_name(parseFullName(jSONObject));
                instagramUser.setProfile_pic_url(parseProfilePicUrl(jSONObject));
            }
        } catch (Exception unused) {
        }
        return instagramUser;
    }

    private static long parsePK(JSONObject jSONObject) {
        if (!jSONObject.isNull("pk")) {
            try {
                return jSONObject.getLong("pk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static String parseUserName(JSONObject jSONObject) {
        if (!jSONObject.isNull("username")) {
            try {
                return jSONObject.getString("username");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static String parseFullName(JSONObject jSONObject) {
        if (!jSONObject.isNull("full_name")) {
            try {
                return jSONObject.getString("full_name");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static String parseProfilePicUrl(JSONObject jSONObject) {
        if (!jSONObject.isNull("profile_pic_url")) {
            try {
                return jSONObject.getString("profile_pic_url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String parseCaption(JSONObject jSONObject) {
        try {
            if (!jSONObject.isNull("caption")) {
                return jSONObject.getJSONObject("caption").getString("text");
            }
            if (!jSONObject.isNull("edge_media_to_caption")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("edge_media_to_caption");
                if (jSONObject2.isNull("edges")) {
                    return "";
                }
                JSONArray jSONArray = jSONObject2.getJSONArray("edges");
                if (jSONArray.length() > 0) {
                    return jSONArray.getJSONObject(0).getJSONObject("node").getString("text");
                }
                return "";
            } else if (!jSONObject.isNull("accessibility_caption")) {
                return jSONObject.getString("accessibility_caption");
            } else {
                return "";
            }
        } catch (Exception unused) {
            return "";
        }
    }
}
