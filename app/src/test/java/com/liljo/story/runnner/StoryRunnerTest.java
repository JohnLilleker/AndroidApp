package com.liljo.story.runnner;

import com.liljo.story.model.Option;
import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StoryRunnerTest {

    @Test
    public void storyRunner_constructorDefaults() {
        final Story story = new Story();
        story.addScene("introduction", new Scene());
        StoryRunner storyRunner = new StoryRunner(story);
        assertThat(storyRunner.currentScene(), is("introduction"));
    }

    @Test
    public void storyRunner_currentDisplayText() {
        final Story story = new Story();
        final Scene scene = new Scene();
        scene.addDisplayText("hiya");
        scene.addDisplayText("boom");

        story.addScene("introduction", scene);

        final StoryRunner storyRunner = new StoryRunner(story);
        List<String> strings = storyRunner.currentDisplayText();

        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("hiya"));
        assertThat(strings.get(1), is("boom"));
    }

    @Test
    public void storyRunner_currentOptions() {
        final Story story = new Story();
        final Scene scene = new Scene();
        scene.addOption(new Option("left", "redDoor"));
        scene.addOption(new Option("right", "blueDoor"));

        story.addScene("introduction", scene);

        final StoryRunner storyRunner = new StoryRunner(story);
        List<Option> options = storyRunner.currentOptions();

        assertThat(options.size(), is(2));
        assertThat(options.get(0).getSceneLink(), is("redDoor"));
        assertThat(options.get(0).getDisplayText(), is("left"));
        assertThat(options.get(1).getSceneLink(), is("blueDoor"));
        assertThat(options.get(1).getDisplayText(), is("right"));
    }

    @Test
    public void storyRunner_transitionScene_updatesCurrentScene() {

        final Story story = new Story();
        story.addScene("introduction", new Scene());
        story.addScene("act2", new Scene());

        final StoryRunner storyRunner = new StoryRunner(story);
        assertThat(storyRunner.currentScene(), is("introduction"));

        storyRunner.transitionScene("act2");
        assertThat(storyRunner.currentScene(), is("act2"));
    }

    @Test
    public void storyRunner_transitionScene_updatesCurrentDisplayText() {
        final Story story = new Story();

        final Scene scene_1 = new Scene();
        scene_1.addDisplayText("hiya");
        story.addScene("introduction", scene_1);

        final Scene scene_2 = new Scene();
        scene_2.addDisplayText("boom");
        story.addScene("act2", scene_2);

        final StoryRunner storyRunner = new StoryRunner(story);

        storyRunner.transitionScene("act2");

        List<String> strings = storyRunner.currentDisplayText();
        assertThat(strings.size(), is(1));
        assertThat(strings.get(0), is("boom"));
    }

    @Test
    public void storyRunner_transitionScene_updatesCurrentOptions() {
        final Story story = new Story();

        final Scene scene_1 = new Scene();
        scene_1.addOption(new Option("abc", "def"));
        story.addScene("introduction", scene_1);

        final Scene scene_2 = new Scene();
        scene_2.addOption(new Option("boom", "bomb"));
        story.addScene("act2", scene_2);

        final StoryRunner storyRunner = new StoryRunner(story);

        storyRunner.transitionScene("act2");

        List<Option> strings = storyRunner.currentOptions();
        assertThat(strings.size(), is(1));
        assertThat(strings.get(0).getDisplayText(), is("boom"));
        assertThat(strings.get(0).getSceneLink(), is("bomb"));
    }

    @Test
    public void storyRunner_hasEnded_trueIfEndScene() {
        final Story story = new Story();
        story.addScene("introduction", new Scene());
        StoryRunner storyRunner = new StoryRunner(story);
        assertTrue(storyRunner.hasEnded());
    }

    @Test
    public void storyRunner_hasEnded_falseIfSceneHasOptions() {
        final Story story = new Story();
        final Scene scene = new Scene();
        scene.addOption(new Option("", ""));
        story.addScene("introduction", scene);
        StoryRunner storyRunner = new StoryRunner(story);
        assertFalse(storyRunner.hasEnded());
    }

}