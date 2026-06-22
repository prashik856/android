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
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.models.Team;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class NewMatchActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;

    Match match;
    Team teamA;
    Team teamB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_match);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleTeamANameActivation(View view) {
        EditText editText = findViewById(R.id.team_a_name);
        editText.setText("");
    }

    public void handleTeamBNameActivation(View view) {
        EditText editText = findViewById(R.id.team_b_name);
        editText.setText("");
    }

    public void handleClickOnNext(View view) {
        EditText editText = findViewById(R.id.team_a_name);
        String teamAName = editText.getText().toString();

        editText = findViewById(R.id.team_b_name);
        String teamBName = editText.getText().toString();

        if(teamAName.isEmpty() || teamBName.isEmpty()) {
            Toast.makeText(this, "Team Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        teamA = new Team();
        teamA.setName(teamAName);

        teamB = new Team();
        teamB.setName(teamBName);

        match = new Match();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setDate();

        // Proceed to select players who are playing
        Intent intent = new Intent(this, PlayersSelectActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("match_object", match);
        startActivity(intent);
    }
}