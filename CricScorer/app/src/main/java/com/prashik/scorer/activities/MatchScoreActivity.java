package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.Over;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.models.Team;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class MatchScoreActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    Match match;
    HashMap<String, String> nameToIdMap;
    HashMap<String, Player> allPlayers;
    String filesDirectory;

    Team battingTeam;
    Team bowlingTeam;
    MatchPlayer strikerBatsman;
    MatchPlayer nonStrikeBatsman;
    MatchPlayer bowler;
    Over currentOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_score);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.filesDirectory = this.dataFilesMap.get("files_directory");

        // Check if this match already exists
        boolean alreadyExists = Utils.isMatchAlreadyExists(this.filesDirectory, this.match);
        if(alreadyExists) {
            Toast.makeText(this, "The match with similar details already exists. Please edit the existing match or delete that match first.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            String matchDataFile = this.match.getDataFileName(this.filesDirectory);
            System.out.println("Match Data File Name: " + matchDataFile);
            // match file name is always going to be unique due to uuid
            Utils.createFile(matchDataFile);
        }
        setBattingAndBowlingTeams();

        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
            if (s.equals("players_name_to_id_map_file_location")) {
                this.nameToIdMap = Utils.readNameToIdMapFile(dataFile);
            }
        }

        System.out.println("Match Object: ");
        System.out.println(this.match);

        this.updateScore();

        this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(this.match.getStrikerBatsman());
        this.nonStrikeBatsman = this.battingTeam.getMatchPlayerFromName(this.match.getNonStrikeBatsman());
        this.bowler = this.bowlingTeam.getMatchPlayerFromName(this.match.getCurrentBowler());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(MatchScoreActivity.this, "You cannot go back now. " +
                        "Press home instead.", Toast.LENGTH_LONG).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

        this.syncMatch();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void updateScore() {
        this.currentOver = this.bowlingTeam.getCurrentOverObject();
        TextView teamNameText = findViewById(R.id.team_name_ms);
        TextView runsAndWicketsText = findViewById(R.id.score_value_ms);
        TextView oversText = findViewById(R.id.overs_value_ms);
        TextView runRateText = findViewById(R.id.run_rate_ms);

        String name = this.battingTeam.getName();
        double runRate = this.battingTeam.getRunRate();
        String score = Utils.getScore(this.battingTeam);
        String overs = Utils.getOvers(this.bowlingTeam, this.currentOver, this.match);
        String overDetails = Utils.getOverDetails(this.currentOver);

        // show batting team name
        teamNameText.setText(name);

        // show runs and wickets
        runsAndWicketsText.setText(score);
        System.out.println("Score value: " + score);

        // show overs
        oversText.setText(overs);
        System.out.println("Overs value: " + overs);

        // show runrate
        runRateText.setText(String.format("RR: %.2f", runRate));

        // show striker
        TextView strikerName = findViewById(R.id.striker_name_ms);
        String[] strikePlayerSplit = this.match.getStrikerBatsman().split(" ");
        String strikerNameToDisplay = strikePlayerSplit[0] + " " + "0(0)";
        strikerName.setText(strikerNameToDisplay);

        // show non striker
        String[] nonStrikePlayerSplit = this.match.getNonStrikeBatsman().split(" ");
        String nonStrikerNameToDisplay = nonStrikePlayerSplit[0] + " " + "0(0)";
        TextView nonStrikerName = findViewById(R.id.non_striker_name_ms);
        nonStrikerName.setText(nonStrikerNameToDisplay);

        // show bowler
        TextView bowlerNameText = findViewById(R.id.current_bowler_ms);
        String temp = "Bowler Name\n" + this.match.getCurrentBowler().split(" ")[0];
        bowlerNameText.setText(temp);

        // show over details
        TextView overDetailsText = findViewById(R.id.current_over_details_ms);
        overDetailsText.setText(overDetails);
    }

    public void syncMatch() {
        Utils.syncMatchData(this.match.getDataFileName(this.filesDirectory), this.match);
    }

    public void setBattingAndBowlingTeams() {
        this.battingTeam = this.match.getBattingAndBowlingTeams().get(0);
        this.bowlingTeam = this.match.getBattingAndBowlingTeams().get(1);
    }

    public void handleHomeClick(View view) {
        this.syncMatch();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void handleZeroClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // update team score
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // Add dot ball to striker batsman
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("0");
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();

        // Updated the over
        this.currentOver.getOverSummary().add("0");
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.updateOverCompleted();

        // Add dot ball info to current bowler
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        if(this.currentOver.isOverCompleted()) {
            this.bowler.getMatchPlayerBowling().incrementNoOfOvers();
            this.bowlingTeam.incrementCurrentOverBowling();
            this.battingTeam.incrementCurrentOverBatting();
            if(this.currentOver.isMaiden()) {
                this.bowler.getMatchPlayerBowling().incrementMaidenOverBowled();
                this.bowler.getMatchPlayerBowling().addToMaidenOverBowledTo(
                        this.match.getStrikerBatsman());
            }
        }

        this.updateScore();
        this.syncMatch();
    }

    public void handleOneClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // update team score
        this.battingTeam.incrementRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // add single
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("1");
        this.strikerBatsman.getMatchPlayerBatting().incrementRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("1");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementRuns();
        this.currentOver.updateOverCompleted();

        // Add single run info to current bowler
        this.bowler.getMatchPlayerBowling().incrementRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        if(this.currentOver.isOverCompleted()) {
            this.bowler.getMatchPlayerBowling().incrementNoOfOvers();
            this.bowlingTeam.incrementCurrentOverBowling();
            this.battingTeam.incrementCurrentOverBatting();
        }

        this.updateScore();
        this.syncMatch();
    }

    public void handleTwoClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // add two
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("2");
        this.strikerBatsman.getMatchPlayerBatting().addTwoToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("2");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementRuns();
        this.currentOver.updateOverCompleted();

        // Add single run info to current bowler
        this.bowler.getMatchPlayerBowling().addTwoToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        if(this.currentOver.isOverCompleted()) {
            this.bowler.getMatchPlayerBowling().incrementNoOfOvers();
        }

        this.updateScore();
        this.syncMatch();
    }

    public void handleThreeClick(View view) {

    }

    public void handleFourClick(View view) {

    }

    public void handleFiveClick(View view) {

    }

    public void handleSixClick(View view) {

    }

    public void handleRotateStrikeClick(View view) {
        // basic
        String temp1 = this.match.getStrikerBatsman();
        String temp2 = this.match.getNonStrikeBatsman();
        this.match.setStrikerBatsman(temp2);
        this.match.setNonStrikeBatsman(temp1);

        MatchPlayer temp3 = this.strikerBatsman;
        this.strikerBatsman = this.nonStrikeBatsman;
        this.nonStrikeBatsman = temp3;

        this.syncMatch();
    }



    public void handleUndoClick(View view) {

    }



    public void handleWideBallClick(View view) {

    }

    public void handleNoBallClick(View view) {

    }

    public void handleEditOversClick(View view) {

    }

    public void handleByeClick(View view) {

    }

    public void handleBowledClick(View view) {

    }

    public void handleCaughtClick(View view) {

    }

    public void handleEditPlayersClick(View view) {

    }

    public void handleRunOutClick(View view) {

    }

    public void handleHitWicketClick(View view) {

    }

    public void handleInningsEndClick(View view) {

    }

    public void handleRetireClick(View view) {

    }
}