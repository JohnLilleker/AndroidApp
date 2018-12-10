package com.liljo.story.parser;

import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class StoryParserTest {

    private StoryParser storyParser = new StoryParser();

    @Test
    public void storyParser_simpleScene() {

        String storyString = ":introduction\n" +
                ";";

        Story story = storyParser.parse(storyString);

        assertThat(story, is(notNullValue()));
        assertThat(story.getScene("introduction"), is(notNullValue()));
    }

    @Test
    public void storyParser_multipleScenes() {
        String storyString = ":scene1\n" +
                ";\n" +
                " : scene2\n" +
                "\n" +
                "\n" +
                ";";

        Story story = storyParser.parse(storyString);

        assertThat(story, is(notNullValue()));
        assertThat(story.getScene("scene1"), is(notNullValue()));
        assertThat(story.getScene("scene2"), is(notNullValue()));
        // check the empty lines mean nothing
        assertThat(story.getScene("scene2").getDisplayText().size(), is(0));
        assertThat(story.getScene("scene2").getOptions().size(), is(0));
    }

    @Test
    public void storyParser_complexScene_displayText() {

        // windows line separators
        String storyString = ":introduction\r\n" +
                "- hello world\r\n" +
                "-how are you  \r\n" +
                "-\r\n" +
                "  - I am fine\r\n" +
                ";";

        Story story = storyParser.parse(storyString);

        assertThat(story, is(notNullValue()));

        Scene introduction = story.getScene("introduction");
        assertThat(introduction, is(notNullValue()));
        assertThat(introduction.getOptions().size(), is(0));

        assertThat(introduction.getDisplayText().size(), is(4));
        assertThat(introduction.getDisplayText().get(0), is("hello world"));
        assertThat(introduction.getDisplayText().get(1), is("how are you"));
        assertThat(introduction.getDisplayText().get(2), is(""));
        assertThat(introduction.getDisplayText().get(3), is("I am fine"));
    }


    @Test
    public void storyParser_complexScene_options() {
        String storyString = ":introduction\n" +
                "- how are you?  \n" +
                " | Fine [scene1] \n" +
                "|Annoyed[scene2]\n" +
                ";";

        Story story = storyParser.parse(storyString);

        assertThat(story, is(notNullValue()));

        Scene introduction = story.getScene("introduction");
        assertThat(introduction, is(notNullValue()));

        assertThat(introduction.getDisplayText().size(), is(1));
        assertThat(introduction.getDisplayText().get(0), is("how are you?"));

        assertThat(introduction.getOptions().size(), is(2));

        assertThat(introduction.getOptions().get(0).getDisplayText(), is("Fine"));
        assertThat(introduction.getOptions().get(0).getSceneLink(), is("scene1"));

        assertThat(introduction.getOptions().get(1).getDisplayText(), is("Annoyed"));
        assertThat(introduction.getOptions().get(1).getSceneLink(), is("scene2"));
    }

    @Test
    public void storyParser_comments() {
        String storyString = ":introduction\n" +
                " // IGNORE ME \n" +
                ";\n" +
                "// I AM UNIMPORTANT TOO\n" +
                "\n" +
                ":end\n" +
                " - The end\n" +
                ";";

        Story story = storyParser.parse(storyString);

        assertThat(story, is(notNullValue()));
        assertThat(story.getScene("introduction"), is(notNullValue()));
        assertThat(story.getScene("end"), is(notNullValue()));
        assertThat(story.getScene("end").getDisplayText().get(0), is("The end"));
    }
}