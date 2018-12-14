package com.liljo.story.runnner;

import com.liljo.story.exception.StoryRunException;
import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class StoryRunnerExceptionTest {


    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void storyRunner_storyRunException_noIntroduction() throws StoryRunException {
        expectedException.expect(StoryRunException.class);
        expectedException.expectMessage("Unknown scene: introduction");

        new StoryRunner(new Story());
    }

    @Test
    public void storyRunner_storyRunException_sceneNotFound() throws StoryRunException {
        expectedException.expect(StoryRunException.class);
        expectedException.expectMessage("Unknown scene: scene");

        final Story story = new Story();
        story.addScene("introduction", new Scene());

        final StoryRunner storyRunner = new StoryRunner(story);
        storyRunner.transitionScene("scene");
    }
}
