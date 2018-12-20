package com.liljo.story;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liljo.story.exception.StoryParseException;
import com.liljo.story.model.Story;
import com.liljo.story.parser.StoryParser;
import com.liljo.story.verification.Message;
import com.liljo.story.verification.Severity;
import com.liljo.story.verification.StoryVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.liljo.story.StoryActivity.FILE_NAME;
import static com.liljo.story.util.FileUtil.inputStreamToString;

public class ValidationActivity extends AppCompatActivity {

    private static final String TAG = "ValidationActivity";

    private String fileName;
    private Story story;
    private StoryVerifier storyVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        fileName = getIntent().getStringExtra(FILE_NAME);
        storyVerifier = new StoryVerifier();
        final TextView storyName = findViewById(R.id.storyName);
        storyName.setText(fileName);
    }

    public void onReadClick(View view) {
        final StoryParser storyParser = new StoryParser();
        final TextView runStatus = findViewById(R.id.readStatus);

        try {
            final InputStream open = getAssets().open(fileName);
            story = storyParser.parse(inputStreamToString(open));

            runStatus.setText(getText(R.string.readSuccess));

            final Button runButton = findViewById(R.id.runButton);
            runButton.setEnabled(true);
            runButton.setBackgroundColor(getResources().getColor(R.color.buttonBackgroundEnabled));
        } catch (StoryParseException | IOException e) {
            Log.e(TAG, e.getMessage());

            String error = String.valueOf(getText(R.string.readFail)) + ":\n" +
                    e.getMessage();

            runStatus.setText(error);
        }
    }

    public void onRunClick(View view) {

        final TextView runStatus = findViewById(R.id.runStatus);

        final List<Message> messages = storyVerifier.verify(story);
        final Severity status = storyVerifier.statusOf(messages);

        runStatus.setText(status.name());

        if (status != Severity.PASS) {
            Collections.sort(messages);
            final StringBuilder stringBuilder = new StringBuilder();
            for (Message message : messages) {
                stringBuilder.append(message.getSeverity()).append(": ")
                        .append(message.getMessage()).append("\n\n");
            }

            final TextView runErrors = findViewById(R.id.runErrors);
            runErrors.setText(stringBuilder.toString());
            runErrors.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
