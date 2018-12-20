package com.liljo.story.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public Set<String> scenes() {
        return new HashSet<>(scenes.keySet());
    }
}
