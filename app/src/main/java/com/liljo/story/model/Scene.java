package com.liljo.story.model;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    private final List<String> displayText;
    private final List<Option> options;

    public Scene() {
        this.displayText = new LinkedList<>();
        this.options = new LinkedList<>();
    }

    public void addDisplayText(String displayText) {
        this.displayText.add(displayText);
    }

    public void addOption(Option option) {
        this.options.add(option);
    }

    public List<String> getDisplayText() {
        return displayText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isEnd() {
        return options.isEmpty();
    }
}
