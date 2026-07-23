package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

import java.util.Arrays;
import java.util.HashMap;

public class SelectCaptainsActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, String> nameToIdMap;
    Match match;
    String[] matchPlayers;
    int teamACaptainChosen = -1;
    String teamACaptain;
    int teamBCaptainChosen = -1;
    String teamBCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_captains);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.nameToIdMap = (HashMap<String, String>) getIntent().getSerializableExtra("name_to_id_map");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.matchPlayers = (String[]) getIntent().getSerializableExtra("match_players");
        this.teamACaptain = "";
        this.teamBCaptain = "";
        this.teamACaptainChosen = -1;
        this.teamBCaptainChosen = -1;
        System.out.println("Match Players: " + Arrays.toString(this.matchPlayers));

        // Set team names
        EditText editTextA = findViewById(R.id.team_a_name_sc);
        editTextA.setText(match.getTeamA().getName());
        EditText editTextB = findViewById(R.id.team_b_name_sc);
        editTextB.setText(match.getTeamB().getName());

        TextView textView = findViewById(R.id.select_team_a_captain);
        textView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCaptainsActivity.this);
            TextView selectCaptainTextView = (TextView) v;
            String teamAName = editTextA.getText().toString();
            builder.setTitle("Select " + teamAName + " Captain");
            builder.setCancelable(false);

            // Setup on click
            builder.setSingleChoiceItems(matchPlayers, teamACaptainChosen, (dialog, which) -> {
                System.out.println("Team A captain chosen value before: " + teamACaptainChosen);
                System.out.println("Choice which value: " + which);
                this.teamACaptain = matchPlayers[which];
                this.teamACaptainChosen = which;
                System.out.println("Captain Chosen value afterwards: " + teamACaptainChosen);
                System.out.println("Captain value: " + this.teamACaptain);
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                System.out.println("Team A Captain value: " + this.teamACaptain);
                System.out.println("Team A Captain Chosen: " + this.teamACaptainChosen);
                selectCaptainTextView.setText(this.teamACaptain);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                selectCaptainTextView.setText("");
                this.teamACaptain = "";
                this.teamACaptainChosen = -1;
            });

            builder.show();
        });

        TextView textView2 = findViewById(R.id.select_team_b_captain);
        textView2.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCaptainsActivity.this);
            TextView selectCaptainTextView = (TextView) v;
            String teamBName = editTextB.getText().toString();
            builder.setTitle("Select " + teamBName + " Captain");
            builder.setCancelable(false);

            // Setup on click
            builder.setSingleChoiceItems(matchPlayers, teamBCaptainChosen, (dialog, which) -> {
                System.out.println("Team B captain chosen value: " + this.teamBCaptainChosen);
                System.out.println("Choice which value: " + which);
                this.teamBCaptainChosen = which;
                this.teamBCaptain = matchPlayers[which];
                System.out.println("Captain Chosen value afterwards: " + teamBCaptainChosen);
                System.out.println("Team B Captain value: " + this.teamBCaptain);
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                selectCaptainTextView.setText(this.teamBCaptain);
                System.out.println("Team B Captain: " + this.teamBCaptain);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                selectCaptainTextView.setText("");
                this.teamBCaptain = "";
                this.teamBCaptainChosen = -1;
            });

            builder.show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleNextClick(View view) {
        // Set team names if edited
        EditText editTextA = findViewById(R.id.team_a_name_sc);
        String teamAName = editTextA.getText().toString();
        EditText editTextB = findViewById(R.id.team_b_name_sc);
        String teamBName = editTextB.getText().toString();

        if(teamAName.isEmpty() || teamBName.isEmpty()) {
            Toast.makeText(this, "Team Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        match.getTeamA().setName(teamAName);
        match.getTeamB().setName(teamBName);

        if(teamACaptain.isEmpty() || teamBCaptain.isEmpty() || teamACaptainChosen == -1 || teamBCaptainChosen == -1) {
            Toast.makeText(this, "Please select the team Captains", Toast.LENGTH_LONG).show();
            return;
        }

        if(teamACaptain.equals(teamBCaptain)) {
            Toast.makeText(this, "Same player cannot be a captain of both teams.", Toast.LENGTH_LONG).show();
            return;
        }

        match.getTeamA().setCaptainName(teamACaptain);
        match.getTeamB().setCaptainName(teamBCaptain);

        // Select Players for Team A
        Intent intent = new Intent(this, SelectTeamAPlayersActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("match_object", this.match);
        intent.putExtra("match_players", matchPlayers);
        intent.putExtra("name_to_id_map", this.nameToIdMap);
        startActivity(intent);
    }
}