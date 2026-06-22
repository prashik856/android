package com.prashik.scorer.activities;

import android.content.DialogInterface;
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
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.models.Team;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class SelectCaptainsActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, String> nameToIdMap;
    Match match;
    String[] matchPlayers;
    int teamACaptainChosen;
    String teamACaptain;
    int teamBCaptainChosen;
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
        this.teamACaptainChosen = 0;
        this.teamBCaptainChosen = 0;

        // Set team names
        EditText editTextA = findViewById(R.id.team_a_name_sc);
        editTextA.setText(match.getTeamA().getName());
        EditText editTextB = findViewById(R.id.team_b_name_sc);
        editTextB.setText(match.getTeamB().getName());

        TextView textView = findViewById(R.id.select_team_a_captain);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectCaptainsActivity.this);
                TextView selectTeamACaptain = findViewById(R.id.select_team_a_captain);
                String teamAName = editTextA.getText().toString();
                builder.setTitle("Select " + teamAName + " Captain");
                builder.setCancelable(false);

                // Setup on click
                builder.setSingleChoiceItems(matchPlayers, teamACaptainChosen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        teamACaptain = matchPlayers[which];
                        teamACaptainChosen = 1;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectTeamACaptain.setText(teamACaptain);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectTeamACaptain.setText("");
                        teamACaptain = "";
                        teamACaptainChosen = 0;
                    }
                });

                builder.show();
            }
        });

        TextView textView2 = findViewById(R.id.select_team_b_captain);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectCaptainsActivity.this);
                TextView selectTeamBCaptain = findViewById(R.id.select_team_b_captain);
                String teamBName = editTextB.getText().toString();
                builder.setTitle("Select " + teamBName + " Captain");
                builder.setCancelable(false);

                // Setup on click
                builder.setSingleChoiceItems(matchPlayers, teamBCaptainChosen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        teamBCaptain = matchPlayers[which];
                        teamBCaptainChosen = 1;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectTeamBCaptain.setText(teamBCaptain);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectTeamBCaptain.setText("");
                        teamBCaptain = "";
                        teamBCaptainChosen = 0;
                    }
                });

                builder.show();
            }
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

        if(teamACaptain.isEmpty() || teamBCaptain.isEmpty()) {
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