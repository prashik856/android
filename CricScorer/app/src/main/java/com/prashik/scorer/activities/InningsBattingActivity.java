package com.prashik.scorer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Team;

import java.util.HashMap;

public class InningsBattingActivity extends AppCompatActivity {

    Match match;
    HashMap<String, String> dataFilesMap;

    int innings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_innings_batting);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.innings = (int) getIntent().getSerializableExtra("innings_value");

        TextView inningsTitleView = findViewById(R.id.innings_title_ibat);
        Team battingTeam;
        if(this.innings == 1) {
            if(this.match.isTeamABatFirst()) {
                // team A
                battingTeam = this.match.getTeamA();
            } else {
                battingTeam = this.match.getTeamB();
            }
        } else {
            if(this.match.isTeamABatFirst()) {
                battingTeam = this.match.getTeamB();
            } else {
                battingTeam = this.match.getTeamA();
            }
        }

        String title = battingTeam.getName() + " Batting";
        inningsTitleView.setText(title);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}