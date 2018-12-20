package com.liljo.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

public class ContentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        try {
            final String[] stories = getAssets().list("stories");

            LinearLayout linearLayout = findViewById(R.id.buttons);
            for (String file : stories) {
                linearLayout.addView(createButtons(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LinearLayout createButtons(String file) {
        final LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(16,0,16,10);

        Button play = createButton(file);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoryClick(v);
            }
        });
        play.setText(file);

        Button verify = createButton(file);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerifyClick(v);
            }
        });
        verify.setText("Verify");

        linearLayout.addView(play);
        linearLayout.addView(verify);

        return linearLayout;
    }

    private Button createButton(String file) {
        final Button button = new Button(this);
        button.setAllCaps(false);
        button.setTag(file);
        button.setBackgroundColor(getResources().getColor(R.color.buttonBackgroundEnabled));
        button.setTextColor(getResources().getColor(R.color.buttonText));
        return button;

    }

    public void onStoryClick(View view) {
        final Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.FILE_NAME, "stories/" + view.getTag());
        startActivity(intent);
    }

    public void onVerifyClick(View view) {
        final Intent intent = new Intent(this, ValidationActivity.class);
        intent.putExtra(StoryActivity.FILE_NAME, "stories/" + view.getTag());
        startActivity(intent);
    }
}
