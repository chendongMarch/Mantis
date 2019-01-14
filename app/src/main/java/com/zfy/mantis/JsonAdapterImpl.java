package com.zfy.mantis;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zfy.mantis.library.SerializeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonAdapterImpl implements SerializeParser {

    private Gson sGson = new Gson();

    public String toJson(Object object) {
        return sGson.toJson(object);
    }

    public <T> T toObj(String json, Class<T> cls) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return sGson.fromJson(json, cls);
    }

    public <T> List<T> toList(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(new Gson().fromJson(elem, clazz));
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    public <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz) {
        return sGson.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }


}