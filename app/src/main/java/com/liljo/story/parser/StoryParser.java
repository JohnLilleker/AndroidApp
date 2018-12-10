package com.liljo.story.parser;

import android.util.Log;

import com.liljo.story.model.Option;
import com.liljo.story.model.Scene;
import com.liljo.story.model.Story;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoryParser {

    private static final String TAG = "com.liljo...StoryParser";

    private static final String SENTENCE_GROUP = "(\\S(?:.*\\S)?)";
    private static final String SCENE_GROUP = "(\\w[\\w\\-.]*)";

    private static final Pattern OPTION_REGEX = Pattern.compile("\\s*\\|\\s*" + SENTENCE_GROUP + "\\s*\\[" + SCENE_GROUP + "]\\s*");
    private static final Pattern DISPLAY_REGEX = Pattern.compile("\\s*-\\s*" + SENTENCE_GROUP + "?\\s*");
    private static final Pattern SCENE_TAG_REGEX = Pattern.compile("\\s*:\\s*" + SCENE_GROUP + "\\s*");
    private static final Pattern SCENE_END_REGEX = Pattern.compile("\\s*;\\s*");
    private static final Pattern COMMENT_REGEX = Pattern.compile("\\s*//.*");
    private static final Pattern EMPTY_REGEX = Pattern.compile("\\s*");

    public Story parse(String storyString) {
        final Story story = new Story();

        final String[] lines = storyString.split("\\r\\n|\\n|\\r");

        String tag = null;
        Scene scene = null;
        int lineNo = 0;

        for (final String line : lines) {
            lineNo++;

            Matcher matcher;

            if ((matcher = SCENE_TAG_REGEX.matcher(line)).matches()) {
                if (isReadingScene(scene)) {
                    throwError("Illegal nesting of scene tags: line " + lineNo);
                }
                tag = parseTag(matcher);
                scene = new Scene();
                continue;
            }

            if (SCENE_END_REGEX.matcher(line).matches()) {
                if (!isReadingScene(scene)) {
                    throwError("Orphan end scene tag: line " + lineNo);
                }
                story.addScene(tag, scene);
                tag = null;
                scene = null;
                continue;
            }

            if ((matcher = DISPLAY_REGEX.matcher(line)).matches()) {
                if (!isReadingScene(scene)) {
                    throwError("Orphan display text: line " + lineNo);
                }
                scene.addDisplayText(parseDisplayText(matcher));
                continue;
            }

            if ((matcher = OPTION_REGEX.matcher(line)).matches()) {
                if (!isReadingScene(scene)) {
                    throwError("Orphan option: line " + lineNo);
                }
                scene.addOption(parseOption(matcher));
                continue;
            }

            if (COMMENT_REGEX.matcher(line).matches() || EMPTY_REGEX.matcher(line).matches()) {
                continue;
            }

            throwError("Unknown line format:" + line + ": line " + lineNo);
        }

        if (isReadingScene(scene)) {
            throwError("End of file reached while reading scene " + tag);
        }

        return story;
    }

    private void throwError(String error) {
        Log.e(TAG, error);
        throw new IllegalStateException(error);
    }

    private boolean isReadingScene(Scene scene) {
        return scene != null;
    }

    private String parseTag(final Matcher matcher) {
        return matcher.group(1);
    }

    private Option parseOption(final Matcher matcher) {
        String displayText = matcher.group(1);
        String sceneLink = matcher.group(2);
        return new Option(displayText, sceneLink);
    }

    private String parseDisplayText(final Matcher matcher) {
        String display = matcher.group(1);
        return display != null ? display : "";
    }
}
