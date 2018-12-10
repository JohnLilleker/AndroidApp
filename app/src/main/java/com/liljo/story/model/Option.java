package com.liljo.story.model;

public class Option {
    private final String displayText;
    private final String sceneLink;

    public Option(String displayText, String sceneLink) {
        this.displayText = displayText;
        this.sceneLink = sceneLink;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getSceneLink() {
        return sceneLink;
    }
}
