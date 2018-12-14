package com.liljo.story.parser;

import com.liljo.story.exception.StoryParseException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class StoryParserExceptionTest {

    @Rule
    public ExpectedException expectedException = none();

    private StoryParser storyParser = new StoryParser();

    @Test
    public void storyParser_storyParseException_unknownSyntax() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \"Hello world\": line 1");

        storyParser.parse("Hello world");
    }

    @Test
    public void storyParser_storyParseException_nestedSceneTag() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Illegal nesting of scene tags: line 2");

        final String storyString = ":scene1\n" +
                        ":scene2\n" +
                        ";";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_orphanEndScene() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Orphan end scene tag: line 3");

        final String storyString = ":scene1\n" +
                ";\n" +
                ";\n\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_orphanDisplayText() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Orphan display text: line 4");

        final String storyString = ":scene1\n" +
                " - This is fine\n" +
                ";\n" +
                " - This ain\'t";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_orphanOption() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Orphan option: line 4");

        final String storyString = ":scene1\n" +
                " | Valid [scene2]\n" +
                ";\n" +
                " | an Option [scene2]\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_emptySceneName() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \" : \": line 1");

        final String storyString = " : \n" +
                " - Bad \n" +
                ";\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_invalidSceneName() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \" :scene the first\": line 1");

        final String storyString = " :scene the first\n" +
                " - Bad \n" +
                ";\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_optionNoText() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \" | [scene]\": line 2");

        final String storyString = " :scene\n" +
                " | [scene]\n" +
                ";\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_optionNoLink() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \" | a button\": line 2");

        final String storyString = " :scene\n" +
                " | a button\n" +
                ";\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_optionInvalidScene() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("Unknown line format \" | button [scene 12 and a half]\": line 2");

        final String storyString = " : scene \n" +
                " | button [scene 12 and a half]\n" +
                ";\n";
        storyParser.parse(storyString);
    }

    @Test
    public void storyParser_storyParseException_notClosedScene() throws Exception {
        expectedException.expect(StoryParseException.class);
        expectedException.expectMessage("End of file reached while reading scene scene2");

        final String storyString = " :scene1\n" +
                " - Good \n" +
                ";\n" +
                "\n" +
                ":scene2\n" +
                "-Bad";
        storyParser.parse(storyString);
    }
}
