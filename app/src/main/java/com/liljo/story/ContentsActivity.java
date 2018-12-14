package com.liljo.story;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.Arrays;

public class ContentsActivity extends AppCompatActivity {

    private static final String TAG = "com...ContentsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        try {
            final String[] stories = getAssets().list("stories");
            Log.d(TAG, Arrays.toString(stories));

            LinearLayout linearLayout = findViewById(R.id.buttons);
            for (String file : stories) {
                linearLayout.addView(createButton(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Button createButton(String file) {
        final Button button = new Button(this);
        button.setText(file);
        button.setAllCaps(false);
        button.setTag(file);
        button.setPadding(0,0,0,10);
        button.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        button.setTextColor(getResources().getColor(R.color.buttonText));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoryClick(v);
            }
        });
        return button;

    }

    public void onStoryClick(View view) {
        final Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.FILE_NAME, "stories/" + view.getTag());
        startActivity(intent);
    }
}
