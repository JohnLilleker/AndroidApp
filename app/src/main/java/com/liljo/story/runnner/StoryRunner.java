package com.liljo.story.runnner;

import com.liljo.story.exception.StoryRunException;
import com.liljo.story.model.Option;
import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import java.util.List;

public class StoryRunner {

    private final Story story;
    private String currentScene;

    public StoryRunner(final Story story) throws StoryRunException {
        this.story = story;
        setCurrentScene("introduction");
    }

    public String currentScene() {
        return currentScene;
    }

    public List<String> scenes() {
        return story.scenes();
    }

    public List<String> currentDisplayText() {
        return getCurrentScene().getDisplayText();
    }

    public List<Option> currentOptions() {
        return getCurrentScene().getOptions();
    }

    public void transitionScene(String scene) throws StoryRunException {
        setCurrentScene(scene);
    }

    public boolean hasEnded() {
        return getCurrentScene().isEnd();
    }

    private Scene getCurrentScene() {
        return story.getScene(currentScene);
    }

    private void setCurrentScene(String scene) throws StoryRunException {
        if (story.getScene(scene) == null) {
            throw new StoryRunException("Unknown scene: " + scene);
        }
        currentScene = scene;
    }
}
