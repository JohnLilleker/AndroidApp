package com.liljo.story.verification;

import com.liljo.story.model.Option;
import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.liljo.story.verification.Severity.CAUTION;
import static com.liljo.story.verification.Severity.ERROR;
import static com.liljo.story.verification.Severity.PASS;
import static com.liljo.story.verification.Severity.WARNING;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class StoryVerifierTest {

    private StoryVerifier storyVerifier = new StoryVerifier();

    @Test
    public void storyVerifier_verify_goodStory() {
        final Story story = new Story();

        final Scene intro = new Scene();
        intro.addOption(new Option(null, "scene1"));
        intro.addOption(new Option(null, "scene2"));
        story.addScene("introduction", intro);

        story.addScene("scene1", new Scene());

        final Scene scene2 = new Scene();
        scene2.addOption(new Option(null, "scene3"));
        story.addScene("scene2", scene2);

        story.addScene("scene3", new Scene());

        assertThat(storyVerifier.verify(story), is(empty()));
    }

    @Test
    public void storyVerifier_verify_error_noIntroduction() {
        final Story story = new Story();
        story.addScene("scene1", new Scene());

        final List<Message> messages = storyVerifier.verify(story);

        assertThat(messages, hasSize(1));
        assertThat(messages.get(0).getSeverity(), is(ERROR));
        assertThat(messages.get(0).getMessage(), is("No introduction"));
    }

    @Test
    public void storyVerifier_verify_error_unknownScene() {
        final Story story = new Story();

        final Scene intro = new Scene();
        intro.addOption(new Option(null, "I_don.t_know"));
        intro.addOption(new Option(null, "scene1"));
        story.addScene("introduction", intro);

        story.addScene("scene1", new Scene());

        final List<Message> messages = storyVerifier.verify(story);

        assertThat(messages, hasSize(1));
        assertThat(messages.get(0).getSeverity(), is(ERROR));
        assertThat(messages.get(0).getMessage(), is("Unknown scene: I_don.t_know"));
    }

    @Test
    public void storyVerifier_verify_error_unknownScenes() {
        final Story story = new Story();

        final Scene intro = new Scene();
        intro.addOption(new Option(null, "blank"));
        intro.addOption(new Option(null, "scene2"));
        intro.addOption(new Option(null, "scene1"));
        story.addScene("introduction", intro);

        story.addScene("scene1", new Scene());

        final Scene scene2 = new Scene();
        scene2.addOption(new Option(null, "not-a-scene"));
        story.addScene("scene2", scene2);

        final List<Message> messages = storyVerifier.verify(story);

        assertThat(messages, hasSize(2));

        assertThat(messages, contains(
                allOf(hasProperty("message", equalTo("Unknown scene: blank")), hasProperty("severity", equalTo(ERROR))),
                allOf(hasProperty("message", equalTo("Unknown scene: not-a-scene")), hasProperty("severity", equalTo(ERROR)))
        ));
    }

    @Test
    public void storyVerifier_verify_warning_orphanScene() {
        final Story story = new Story();

        final Scene intro = new Scene();
        intro.addOption(new Option(null, "scene2"));
        intro.addOption(new Option(null, "scene1"));
        story.addScene("introduction", intro);

        story.addScene("scene1", new Scene());

        story.addScene("scene2", new Scene());

        story.addScene("scene3", new Scene());

        final List<Message> messages = storyVerifier.verify(story);

        assertThat(messages, hasSize(1));

        assertThat(messages.get(0).getSeverity(), is(WARNING));
        assertThat(messages.get(0).getMessage(), is("Scene not covered: scene3"));
    }


    @Test
    public void storyVerifier_verify_caution_cyclicScene() {
        final Story story = new Story();

        final Scene intro = new Scene();
        intro.addOption(new Option(null, "scene2"));
        intro.addOption(new Option(null, "scene1"));
        story.addScene("introduction", intro);

        final Scene scene = new Scene();
        scene.addOption(new Option(null, "introduction"));
        story.addScene("scene1", scene);

        story.addScene("scene2", new Scene());


        final List<Message> messages = storyVerifier.verify(story);

        assertThat(messages, hasSize(1));

        assertThat(messages.get(0).getSeverity(), is(CAUTION));
        assertThat(messages.get(0).getMessage(), is("Cyclic scene introduction: [introduction, scene1, introduction]"));
    }

    @Test
    public void storyVerifier_statusOf_returnError() {

        assertThat(storyVerifier.statusOf(
                asList(
                        new Message("567", WARNING),
                        new Message("123", CAUTION),
                        new Message("dfg", WARNING),
                        new Message("abc", ERROR), // This should be flagged
                        new Message("khg", CAUTION)

                )), is(ERROR));
    }

    @Test
    public void storyVerifier_statusOf_returnWarning() {

        assertThat(storyVerifier.statusOf(
                asList(
                        new Message("abc", WARNING),
                        new Message("six", CAUTION),
                        new Message("548", WARNING),
                        new Message("132", CAUTION)
                )), is(WARNING));
    }

    @Test
    public void storyVerifier_statusOf_returnCaution() {

        assertThat(storyVerifier.statusOf(
                asList(
                        new Message("six", CAUTION),
                        new Message("132", CAUTION)
                )), is(CAUTION));
    }

    @Test
    public void storyVerifier_statusOf_returnPassIfEmpty() {

        assertThat(storyVerifier.statusOf(Collections.<Message>emptyList()), is(PASS));
    }

}