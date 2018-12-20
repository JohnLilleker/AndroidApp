package com.liljo.story.verification;

import com.liljo.story.exception.StoryRunException;
import com.liljo.story.model.Option;
import com.liljo.story.model.Story;
import com.liljo.story.runnner.StoryRunner;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.liljo.story.verification.Severity.CAUTION;
import static com.liljo.story.verification.Severity.ERROR;
import static com.liljo.story.verification.Severity.PASS;
import static com.liljo.story.verification.Severity.WARNING;
import static java.util.Arrays.asList;

public class StoryVerifier {

    public List<Message> verify(final Story story) {
        final Set<String> scenes = story.scenes();
        final StoryRunner storyRunner;
        try {
            storyRunner = new StoryRunner(story);
        } catch (StoryRunException e) {
            return Collections.singletonList(new Message("No introduction", ERROR));
        }

        List<String> scenesSoFar = new LinkedList<>();
        List<Message> messages = verify_rec(storyRunner, scenes, scenesSoFar);

        for (String scene : scenes) {
            messages.add(new Message("Scene not covered: " + scene, WARNING));
        }

        return messages;
    }

    private List<Message> verify_rec(StoryRunner storyRunner, Set<String> scenes, List<String> scenesSoFar) {

        final String currentScene = storyRunner.currentScene();
        scenes.remove(currentScene);

        List<String> trail = new LinkedList<>(scenesSoFar);
        trail.add(currentScene);

        if (storyRunner.hasEnded()) {
            return new LinkedList<>();
        }

        if (Collections.frequency(trail, currentScene) > 1) {
            return new LinkedList<>(asList(new Message("Cyclic scene " + currentScene + ": " + trail.toString(), CAUTION)));
        }


        List<Message> messages = new LinkedList<>();

        for (Option option : storyRunner.currentOptions()) {
            final String sceneLink = option.getSceneLink();

            try {
                final StoryRunner storyRunnerClone = storyRunner.copyState();
                storyRunnerClone.transitionScene(sceneLink);
                messages.addAll(verify_rec(storyRunnerClone, scenes, trail));
            } catch (StoryRunException e) {
                messages.add(new Message(e.getMessage(), ERROR));
            }
        }

        return messages;
    }

    public Severity statusOf(final List<Message> messages) {
        if (messages.isEmpty()) {
            return PASS;
        }
        final Message min = Collections.min(messages);
        return min.getSeverity();
    }
}
