package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final String TAG = "JsonUtils";

    private int pos;

    public Sandwich parseSandwichJson(String json) {
        this.pos = 0;
        try {
            Map<String, Object> map = readMap(json);
            Log.i(TAG, "Parsed: " + map);

            Sandwich sandwich = new Sandwich();
            Map<String, Object> nameMap = (Map) map.get("name");
            sandwich.setMainName((String) nameMap.get("mainName"));
            sandwich.setAlsoKnownAs((List<String>) nameMap.get("alsoKnownAs"));
            sandwich.setDescription((String) map.get("description"));
            sandwich.setImage((String) map.get("image"));
            sandwich.setIngredients((List<String>) map.get("ingredients"));
            sandwich.setPlaceOfOrigin((String) map.get("placeOfOrigin"));
            return sandwich;
        } catch (Exception e) {
            Log.e(TAG, "Bad json, pos=" + pos, e);
            Log.e(TAG, json);
        }
        return null;
    }

    /**
     * Expecting {"name":value,...} list.
     */
    private Map<String, Object> readMap(String json) throws Exception {
        if (json.charAt(pos++) != '{') throw new Exception("Expected {");
        Map<String, Object> map = new HashMap<>();
        while (true) {
            // read name
            String name = readString(json);
            if (json.charAt(pos++) != ':') throw new Exception("Expected :");
            // read value
            char c = json.charAt(pos);
            switch (c) {
                case '"':
                    // string
                    String value0 = readString(json);
                    map.put(name, value0);
                    break;
                case '{':
                    // map
                    Map<String, Object> value1 = readMap(json);
                    map.put(name, value1);
                    break;
                case '[':
                    // list
                    List<Object> value2 = readList(json);
                    map.put(name, value2);
                    break;
            }
            char next = json.charAt(pos);
            if (next == '}') {
                pos++;
                break;
            }
            if (next != ',') throw new Exception("Expected ,");
            pos++;
        }
        return map;
    }

    private List<Object> readList(String json) throws Exception {
        if (json.charAt(pos++) != '[') throw new Exception("Expected [");
        List<Object> list = new ArrayList<>();
        while (true) {
            // read value
            char c = json.charAt(pos);
            switch (c) {
                case '"':
                    // string
                    String value0 = readString(json);
                    list.add(value0);
                    break;
                case '{':
                    // map
                    Map<String, Object> value1 = readMap(json);
                    list.add(value1);
                    break;
                case '[':
                    // list
                    List<Object> value2 = readList(json);
                    list.add(value2);
                    break;
            }
            char next = json.charAt(pos);
            if (next == ']') {
                pos++;
                break;
            }
            if (next != ',') throw new Exception("Expected ,");
            pos++;
        }
        return list;
    }

    private String readString(String json) throws Exception {
        if (json.charAt(pos++) != '"') throw new Exception("Expected \"");
        int end = json.indexOf('"', pos);
        if (end == -1) throw new Exception("Missing \"");
        String name = json.substring(pos, end);
        pos = end + 1;
        return name;
    }
}
