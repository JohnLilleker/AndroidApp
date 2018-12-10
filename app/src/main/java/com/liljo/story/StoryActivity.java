package com.liljo.story;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liljo.story.model.Option;
import com.liljo.story.model.Story;
import com.liljo.story.parser.StoryParser;
import com.liljo.story.runnner.StoryRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class StoryActivity extends AppCompatActivity {

    private final String TAG = "com.....StoryActivity";
    public static final String FILE_NAME = "com.liljo.story.FILE_NAME";

    private StoryRunner storyRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        final String fileName = getIntent().getStringExtra(FILE_NAME);

        final StoryParser storyParser = new StoryParser();
        final Story story = storyParser.parse(readFileToString(fileName));
        storyRunner = new StoryRunner(story);
        buildUI();
    }

    public void onOptionClick(View view) {
        final String sceneTag = (String) view.getTag();
        storyRunner.transitionScene(sceneTag);
        buildUI();
    }

    public void onEndClick(View view) {
        final Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
    }

    private void buildUI() {

        final List<String> displayText = storyRunner.currentDisplayText();

        final TextView displayTextView = findViewById(R.id.displayText);
        displayTextView.setText(TextUtils.join("\n", displayText));

        final LinearLayout optionsLayout = findViewById(R.id.optionsLayout);
        optionsLayout.removeAllViewsInLayout();
        if (storyRunner.hasEnded()) {
            optionsLayout.addView(endButton());
        }
        else {
            final List<Option> options = storyRunner.currentOptions();

            for (Option option : options) {
                optionsLayout.addView(buttonFrom(option));
            }
        }
    }

    private Button endButton() {
        Button button = buttonWithDefaults(getResources().getString(R.string.endButton));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEndClick(v);
            }
        });
        return button;
    }

    private Button buttonFrom(final Option option) {
        Button button = buttonWithDefaults(option.getDisplayText());
        button.setTag(option.getSceneLink());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionClick(v);
            }
        });
        return button;
    }

    private Button buttonWithDefaults(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        button.setTextColor(getResources().getColor(R.color.buttonText));
        return button;
    }

    private String readFileToString(String fileName) {
        try {
            final InputStream inputStream = getAssets().open(fileName);
            final Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            final String string = scanner.hasNext() ? scanner.next() : null;
            inputStream.close();
            return string;
        } catch (IOException e) {
            Log.e(TAG, "Error reading file " + fileName);
            throw new IllegalStateException(e);
        }
    }
}
