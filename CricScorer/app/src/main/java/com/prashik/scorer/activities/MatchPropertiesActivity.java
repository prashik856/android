package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

import java.util.HashMap;

public class MatchPropertiesActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_properties);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleNextClick(View view) {
        EditText maxOversText = findViewById(R.id.max_overs_mp);
        int maxOvers = Integer.parseInt(maxOversText.getText().toString());
        if(maxOvers <= 0) {
            Toast.makeText(this, "Number of overs needs to be greater than 0", Toast.LENGTH_LONG).show();
            return;
        }

        if(maxOvers > 20) {
            Toast.makeText(this, "Max overs cannot be greater than 20.", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println("Max overs value: " + maxOvers);

        // Set match properties
        this.match.setMaxOvers(maxOvers);
        this.match.getTeamA().setOvers(maxOvers);
        this.match.getTeamB().setOvers(maxOvers);

        this.match.setBattingAndBowlingTeamNames();

        System.out.println("Both team objects after setting max overs: ");
        System.out.println("Team 1 : " + this.match.getTeamA().toString());
        System.out.println("Team 2 : " + this.match.getTeamB().toString());
        System.out.println("Match object after setting max overs: " + this.match.toString());

        System.out.println("Opening Select Openers Activity");
        Intent intent = new Intent(this, SelectOpenersActivity.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", this.match);
        startActivity(intent);
    }
}