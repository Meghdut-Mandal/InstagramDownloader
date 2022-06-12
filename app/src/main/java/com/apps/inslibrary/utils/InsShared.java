package com.apps.inslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InsShared {
    private static final String FILE_NAME = "ins_shared";
    private static SharedPreferences.Editor editor;

    private static SharedPreferences sharedPreferences;
    private static InsShared tbShared;

    public InsShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, 0);
        InsShared.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public static InsShared init(Context context) {
        if (tbShared == null) {
            tbShared = new InsShared(context);
        }
        return tbShared;
    }

    public void setValue(String str, Object obj) {
        try {
            String simpleName = obj.getClass().getSimpleName();

            switch (simpleName) {
                case "Boolean":
                    editor.putBoolean(str, (Boolean) obj);
                    break;
                case "Float":
                    editor.putFloat(str, (Float) obj);
                    break;
                case "Long":
                    editor.putLong(str, (Long) obj);
                    break;
                case "String":
                    editor.putString(str, (String) obj);
            }
            editor.apply();
        } catch (Exception unused) {
            throw new IllegalThreadStateException();
        }
    }

    public Object getValue(String str, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        if ("String".equals(simpleName)) {
            return sharedPreferences.getString(str, (String) obj);
        }
        if ("int".equals(simpleName) || "Integer".equals(simpleName)) {
            return sharedPreferences.getInt(str, ((Integer) obj).intValue());
        }
        if ("Boolean".equals(simpleName)) {
            return sharedPreferences.getBoolean(str, ((Boolean) obj).booleanValue());
        }
        if ("Float".equals(simpleName)) {
            return sharedPreferences.getFloat(str, ((Float) obj).floatValue());
        }
        if ("Long".equals(simpleName)) {
            return sharedPreferences.getLong(str, ((Long) obj).longValue());
        }
        return null;
    }

    public <K, V> boolean putHashMapData(String str, Map<K, V> map) {
        boolean z;
        try {
            editor.putString(str, new Gson().toJson((Object) map));
            z = true;
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        editor.apply();
        return z;
    }

    public <V> Map<String, V> getHashMapData(String str, Class<V> cls) {
        try {
            String string = sharedPreferences.getString(str, "");
            if (string == null) {
                return null;
            }
            HashMap hashMap = new HashMap();
            Gson gson = new Gson();
            for (Map.Entry next : new JsonParser().parse(string).getAsJsonObject().entrySet()) {
                hashMap.put((String) next.getKey(), gson.fromJson((JsonElement) (JsonObject) next.getValue(), cls));
            }
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> boolean setListEntity(String str, List<T> list) {
        try {
            String json = new Gson().toJson((Object) list);
            if (list != null) {
                if (list.size() > 0) {
                    editor.putString(str, json);
                    editor.apply();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Log.i("setList异常信息>>>", e.toString() + "");
            e.printStackTrace();
            return false;
        }
    }

    public <T> List<T> getListEntity(String str, Class<T> cls) {
        try {
            ArrayList arrayList = new ArrayList();
            Gson gson = new Gson();
            String string = sharedPreferences.getString(str, (String) null);
            if (string != null) {
                if (!"".equals(string)) {
                    Iterator<JsonElement> it = new JsonParser().parse(string).getAsJsonArray().iterator();
                    while (it.hasNext()) {
                        arrayList.add(gson.fromJson(it.next(), cls));
                    }
                    return arrayList;
                }
            }
            return null;
        } catch (Exception e) {
            Log.i("getList异常信息>>>", e.toString() + "");
            e.printStackTrace();
            return null;
        }
    }

    public boolean setEntity(String str, Object obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String encode = URLEncoder.encode(byteArrayOutputStream.toString(C.ISO88591_NAME), "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            editor.putString(str, encode);
            editor.apply();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public Object getEntity(String str) {
        try {
            String string = sharedPreferences.getString(str, (String) null);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(URLDecoder.decode(string, "UTF-8").getBytes(C.ISO88591_NAME));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean remove(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        editor.remove(str);
        editor.commit();
        return true;
    }

    public boolean clearShared() {
        editor.clear();
        editor.commit();
        return true;
    }
}
