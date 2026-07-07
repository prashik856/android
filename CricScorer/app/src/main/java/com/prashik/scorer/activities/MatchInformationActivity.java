package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

import java.util.HashMap;

public class MatchInformationActivity extends AppCompatActivity {
    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_information);

        this.match = (Match) getIntent().getSerializableExtra("match_object");

        // set done or resume
        Button button = findViewById(R.id.status_button_mi);
        if(this.match.isCompleted()) {
            button.setText("Done");
        } else {
            button.setText("Resume");
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}