package com.liljo.story.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Story {

    private Map<String, Scene> scenes;

    public Story() {
        this.scenes = new HashMap<>();
    }

    public void addScene(String sceneHandle, Scene scene) {
        this.scenes.put(sceneHandle, scene);
    }

    public Scene getScene(String sceneHandle) {
        return scenes.get(sceneHandle);
    }

    public List<String> scenes() {
        return new LinkedList<>(scenes.keySet());
    }
}
