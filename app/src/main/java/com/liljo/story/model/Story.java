package com.liljo.story.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Story {

    private Map<String, Scene> scenes;
    private static final String TAG = "com.liljo...Story";

    public Story() {
        this.scenes = new HashMap<>();
    }

    public void addScene(String sceneHandle, Scene scene) {
        this.scenes.put(sceneHandle, scene);
    }

    public Scene getScene(String sceneHandle) {
        if (!scenes.containsKey(sceneHandle)) {
            Log.e(TAG, "Scene " + sceneHandle + " not in story");
        }
        return scenes.get(sceneHandle);
    }

}
