package com.liljo.story.model;

import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class StoryTest {

    @Test
    public void story_getSceneByHandle_returnsScene() {
        Story story = new Story();
        Scene scene = new Scene();
        story.addScene("scene", scene);

        assertThat(story.getScene("scene"), is(scene));
    }
}