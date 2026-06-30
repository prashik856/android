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
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Over;
import com.prashik.scorer.models.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SelectOpenersActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    Match match;

    String[] playersOptions;
    boolean[] playersSelected;
    ArrayList<String> openers = new ArrayList<>();

    String[] bowlerOptions;
    int bowlerSelected = -1;
    String bowler = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_openers);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.playersOptions = this.match.getBattingTeam().getPlayerNames().toArray(new String[0]);
        this.bowlerOptions = this.match.getBowlingTeam().getPlayerNames().toArray(new String[0]);

        System.out.println("Players Options: " + Arrays.toString(this.playersOptions));
        System.out.println("Bowlers Options: " + Arrays.toString(this.bowlerOptions));
        this.playersSelected = new boolean[this.playersOptions.length];

        // Only select two
        TextView textView = findViewById(R.id.select_opening_batsmen_so);
        textView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectOpenersActivity.this);
            TextView openingBatsmen = (TextView) v;
            builder.setTitle("Select Opening Batsmen.");
            builder.setCancelable(false);

            builder.setMultiChoiceItems(this.playersOptions, this.playersSelected, (dialog, which, isChecked) -> {
                if(isChecked) {
                    this.playersSelected[which] = true;
                    openers.add(this.playersOptions[which]);
                } else {
                    this.playersSelected[which] = false;
                    openers.remove(this.playersOptions[which]);
                }
                System.out.println("Openers value: " + openers);
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                if(openers.size() != 2) {
                    Toast.makeText(this, "You need to select two players as openers.", Toast.LENGTH_LONG).show();
                    return;
                }
                String string = openers.get(0) +
                        "*, " +
                        openers.get(1);
                System.out.println("Openers text value: " + string);
                openingBatsmen.setText(string);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear All", (dialog, which) -> {
                for(int i=0; i<playersSelected.length; i++) {
                    playersSelected[i] = false;
                    this.openers.clear();
                    openingBatsmen.setText("");
                }
            });

            builder.show();
        });

        // single choice
        textView = findViewById(R.id.select_opening_bowler_so);
        textView.setOnClickListener(v -> {
            TextView openingBowler = (TextView) v;
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectOpenersActivity.this);
            builder.setTitle("Select Opening Bowler.");
            builder.setCancelable(false);

            builder.setSingleChoiceItems(bowlerOptions, bowlerSelected, (dialog, which) -> {
                bowlerSelected = which;
            });

            builder.setPositiveButton("Ok", (dialog, which) -> {
                if(bowlerSelected != -1) {
                    this.bowler = bowlerOptions[bowlerSelected];
                    openingBowler.setText(this.bowler);
                } else {
                    Toast.makeText(this, "You need to select opening bowler.", Toast.LENGTH_LONG).show();
                }
                System.out.println("Opening bowler: " + this.bowler);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                bowlerSelected = -1;
                bowler = "";
                openingBowler.setText("");
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
        System.out.println("Openers value: " + openers.toString());
        if(openers.size() != 2) {
            Toast.makeText(this, "You need to select two players as openers.", Toast.LENGTH_LONG).show();
            return;
        }

        if(bowler.isEmpty()) {
            Toast.makeText(this, "You need to select one bowler.", Toast.LENGTH_LONG).show();
            return;
        }

        this.match.setStrikerBatsman(openers.get(0));
        this.match.setNonStrikeBatsman(openers.get(1));
        this.match.setCurrentBowler(bowler);

        ArrayList<Team> teams = this.match.getBattingAndBowlingTeams();
        Team battingTeam = teams.get(0);
        Team bowlingTeam = teams.get(1);

        int strikerIndex = -1;
        int nonStrikerIndex = -1;
        int bowlerIndex = -1;

        strikerIndex = battingTeam.getMatchPlayerIndex(openers.get(0));
        nonStrikerIndex = battingTeam.getMatchPlayerIndex(openers.get(1));
        bowlerIndex = bowlingTeam.getMatchPlayerIndex(bowler);

        battingTeam.getTeamPlayers().get(strikerIndex).getMatchPlayerBatting().setBatted(true);
        battingTeam.getTeamPlayers().get(nonStrikerIndex).getMatchPlayerBatting().setBatted(true);
        bowlingTeam.getTeamPlayers().get(bowlerIndex).getMatchPlayerBowling().setBowled(true);

        // Set bowler name in over object
        bowlingTeam.getOvers().get(bowlingTeam.getCurrentOver())
                .setPlayerName(bowler);

        this.match.setStrikerBatsmanIndex(strikerIndex);
        this.match.setNonStrikerBatsmanIndex(nonStrikerIndex);
        this.match.setCurrentBowlerIndex(bowlerIndex);

        // set innings
        this.match.setInnings(1);

        System.out.println("Striker: " + this.match.getStrikerBatsman());
        System.out.println("Non Striker: " + this.match.getNonStrikeBatsman());
        System.out.println("Bowler: " + this.match.getCurrentBowler());

        System.out.println("Match object before going to match score: " + this.match.toString());

        System.out.println("Going to match score.");
        Intent intent = new Intent(this, MatchScoreActivity.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", this.match);
        startActivity(intent);
    }
}