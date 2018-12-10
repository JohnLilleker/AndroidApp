package com.liljo.story.runnner;

import android.util.Log;

import com.liljo.story.model.Option;
import com.liljo.story.model.Story;

import java.util.List;

public class StoryRunner {

    private static final String TAG = "com.liljo...StoryRunner";

    private final Story story;
    private String currentScene;

    public StoryRunner(final Story story) {
        this.story = story;
        transitionScene("introduction");
    }

    public String currentScene() {
        return currentScene;
    }

    public List<String> currentDisplayText() {
        return story.getScene(currentScene).getDisplayText();
    }

    public List<Option> currentOptions() {
        return story.getScene(currentScene).getOptions();
    }

    public void transitionScene(String scene) {
        if (story.getScene(scene) == null) {
            Log.e(TAG, "No such scene: " + scene);
            throw new IllegalStateException("No such scene: " + scene);
        }
        this.currentScene = scene;
    }

    public boolean hasEnded() {
        return story.getScene(currentScene).isEnd();
    }
}
