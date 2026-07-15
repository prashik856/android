package com.prashik.scorer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    boolean resumeMatch = false;
    String[] runsOptions = {"0", "1", "2", "3", "4", "5", "6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_score);

        this.resumeMatch = getIntent().getSerializableExtra("resume_match") != null;
        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.filesDirectory = this.dataFilesMap.get("files_directory");

        if(!resumeMatch) {
            System.out.println("This is the first time we are playing this match.");
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
        } else {
            System.out.println("Resuming match.");
        }

        setBattingAndBowlingTeams();
        System.out.println("Batting team: " + this.battingTeam.getName());
        System.out.println("Bowling team: " + this.bowlingTeam.getName());

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

        this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(this.match.getStrikerBatsman());
        this.nonStrikeBatsman = this.battingTeam.getMatchPlayerFromName(this.match.getNonStrikeBatsman());
        this.bowler = this.bowlingTeam.getMatchPlayerFromName(this.match.getCurrentBowler());
        this.currentOver = this.bowlingTeam.getCurrentOverObject();

        this.updateScore();

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
        TextView teamNameText = findViewById(R.id.team_name_ms);
        TextView runsAndWicketsText = findViewById(R.id.score_value_ms);
        TextView oversText = findViewById(R.id.overs_value_ms);
        TextView runRateText = findViewById(R.id.run_rate_ms);

        String name = this.battingTeam.getName();
        double runRate = this.battingTeam.getRunRate();
        String score = Utils.getScore(this.battingTeam);
        String overs = Utils.getOvers(this.battingTeam, this.match);
        String overDetails = Utils.getOverDetails(this.currentOver);
        String inningsValue = "1st Innings";
        if(this.match.getInnings() != 1) {
            inningsValue = "2nd Innings";
        }

        // show batting team name
        teamNameText.setText(String.format("%s (%s)", name, inningsValue));

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
        String runsScored = Integer.toString(this.strikerBatsman.getMatchPlayerBatting().getRunsScored());
        String ballsPlayed = Integer.toString(this.strikerBatsman.getMatchPlayerBatting().getBallsPlayed());
        String strikerNameToDisplay = strikePlayerSplit[0] + " " + runsScored + "(" + ballsPlayed + ")";
        strikerName.setText(strikerNameToDisplay);

        // show non striker
        String[] nonStrikePlayerSplit = this.match.getNonStrikeBatsman().split(" ");
        runsScored = Integer.toString(this.nonStrikeBatsman.getMatchPlayerBatting().getRunsScored());
        ballsPlayed = Integer.toString(this.nonStrikeBatsman.getMatchPlayerBatting().getBallsPlayed());
        String nonStrikerNameToDisplay = nonStrikePlayerSplit[0] + " " + runsScored + "(" + ballsPlayed + ")";
        TextView nonStrikerName = findViewById(R.id.non_striker_name_ms);
        nonStrikerName.setText(nonStrikerNameToDisplay);

        // show bowler
        TextView bowlerNameText = findViewById(R.id.current_bowler_ms);
        String wickets = Integer.toString(this.bowler.getMatchPlayerBowling().getWicketsTaken());
        String runsGiven = Integer.toString(this.bowler.getMatchPlayerBowling().getRunsConceded());
        String overBowled = Integer.toString(this.bowler.getMatchPlayerBowling().getLegalDeliveriesBowled()/6);
        String ballsBowled = Integer.toString(this.bowler.getMatchPlayerBowling().getLegalDeliveriesBowled() % 6);

        String temp = String.format("%s \n%s-%s(%s.%s)", this.bowler.getPlayerName().split(" ")[0],
                wickets, runsGiven, overBowled, ballsBowled);
        bowlerNameText.setText(temp);

        // show over details
        TextView overDetailsText = findViewById(R.id.current_over_details_ms);
        overDetailsText.setText(overDetails);
    }

    public String[] getNotOutBatsMan() {
        ArrayList<String> temp = new ArrayList<>();
        for(MatchPlayer matchPlayer: this.battingTeam.getTeamPlayers()) {
            if(!matchPlayer.getMatchPlayerBatting().isOut()
            && !matchPlayer.getPlayerName().equals(this.strikerBatsman.getPlayerName())
            && !matchPlayer.getPlayerName().equals(this.nonStrikeBatsman.getPlayerName())) {
                temp.add(matchPlayer.getPlayerName());
            }
        }
        return temp.toArray(new String[0]);
    }

    public String[] getFieldingPlayers() {
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<this.match.getMatchPlayers().size(); i++) {
            if(!this.match.getMatchPlayers().get(i).equals(this.strikerBatsman.getPlayerName())
                && !this.match.getMatchPlayers().get(i).equals(this.nonStrikeBatsman.getPlayerName())) {
                temp.add(this.match.getMatchPlayers().get(i));
            }
        }
        return temp.toArray(new String[0]);
    }

    public boolean checkInningsCompletion() {
        if(this.battingTeam.isBattingInningsCompleted()) {
            Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                            "Please start a new innings.",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        if(this.match.isCompleted()) {
            Toast.makeText(this, "Match is completed. Please end the innings.",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    public boolean checkCompletions() {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return this.checkInningsCompletion();
    }

    public void syncMatch() {
        Utils.syncMatchData(this.match.getDataFileName(this.filesDirectory), this.match);
    }

    public void setBattingAndBowlingTeams() {
        this.battingTeam = this.match.getBattingTeam();
        this.bowlingTeam = this.match.getBowlingTeam();
    }

    public void rotateStrike() {
        // basic
        String temp1 = this.match.getStrikerBatsman();
        String temp2 = this.match.getNonStrikeBatsman();
        this.match.setStrikerBatsman(temp2);
        this.match.setNonStrikeBatsman(temp1);

        MatchPlayer temp3 = this.strikerBatsman;
        this.strikerBatsman = this.nonStrikeBatsman;
        this.nonStrikeBatsman = temp3;
    }

    public void handleHomeClick(View view) {
        this.syncMatch();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void handleZeroClick(View view) {
        if(this.checkCompletions()) {
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

        this.match.addToActivities("0");
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleOneClick(View view) {
        if(this.checkCompletions()) {
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

        this.rotateStrike();
        this.match.addToActivities("1");
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleTwoClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.addTwoToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("2");
        this.strikerBatsman.getMatchPlayerBatting().addTwoToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("2");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addTwoToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addTwoToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("2");
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleThreeClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.addThreeToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("3");
        this.strikerBatsman.getMatchPlayerBatting().addThreeToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("3");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addThreeToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addThreeToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("3");
        this.rotateStrike();
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleFourClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.addFourToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("4");
        this.strikerBatsman.getMatchPlayerBatting().addFourToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().incrementFoursScored();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("4");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addFourToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addFourToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementFoursConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("4");
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleFiveClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.addFiveToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("5");
        this.strikerBatsman.getMatchPlayerBatting().addFiveToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("5");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addFiveToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addFiveToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("5");
        this.rotateStrike();
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleSixClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.addSixToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("6");
        this.strikerBatsman.getMatchPlayerBatting().addSixToRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementSixesScored();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("6");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addSixToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addSixToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementSixesConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("6");
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public void handleOverCompleteClick(View view) {
        if(!this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "You cannot end the over without bowling 6 legal deliveries."
                    , Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Over completed. Updating records");
        // update records after over is completed
        this.bowler.getMatchPlayerBowling().incrementNoOfOvers();
        this.bowlingTeam.incrementCurrentOverBowling();
        this.battingTeam.incrementCurrentOverBatting();
        if(this.currentOver.isMaiden()) {
            System.out.println("A Maiden over was bowled.");
            this.bowler.getMatchPlayerBowling().incrementMaidenOverBowled();
            this.bowler.getMatchPlayerBowling().addToMaidenOverBowledTo(
                    this.match.getStrikerBatsman());
        }
        this.match.updateMatchCompleted();

        if(this.battingTeam.isBattingInningsCompleted()) {
            Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                            "Please start a new innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(this.match.isCompleted()) {
            Toast.makeText(this, "Match is completed. Please end the innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Open the screen to select the new bowler.");
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select Bowler.");
        builder.setCancelable(false);

        ArrayList<String> temp = new ArrayList<>();
        for(String str: this.bowlingTeam.getPlayerNames()) {
            if(!str.equals(this.bowler.getPlayerName())) {
                temp.add(str);
            }
        }
        String[] bowlerOptions = temp.toArray(new String[0]);
        System.out.println("Bowler options: " + Arrays.toString(bowlerOptions));

        final int[] bowlerSelected = {-1};
        builder.setSingleChoiceItems(bowlerOptions, bowlerSelected[0],
                (dialog, which) -> bowlerSelected[0] = which);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(bowlerSelected[0] == -1) {
                Toast.makeText(this, "You need to select a new bowler.", Toast.LENGTH_LONG).show();
                return;
            }

            // else, we now have the new bowler selected.
            String newBowler = bowlerOptions[bowlerSelected[0]];
            System.out.println("Bowler to bowl the new over is: " + newBowler);
            this.match.setCurrentBowler(newBowler);

            int bowlerIndex = this.bowlingTeam.getMatchPlayerIndex(newBowler);
            this.match.setCurrentBowlerIndex(bowlerIndex);
            this.bowler = this.bowlingTeam.getTeamPlayers().get(bowlerIndex);
            this.bowler.getMatchPlayerBowling().setBowled(true);
            this.bowler.getMatchPlayerBowling().addToOverBowled(this.bowlingTeam.getCurrentOverBowling());

            this.currentOver = this.bowlingTeam.getCurrentOverObject();
            this.currentOver.setPlayerName(this.bowler.getPlayerName());
            System.out.println("New Over Object: " + this.currentOver.toString());

            // add over complete to match activities
            this.match.addToActivities("OverComplete");

            // rotate strike
            this.rotateStrike();
            this.updateScore();
            this.syncMatch();

            System.out.println("New Bowler Selected is: " + bowlerOptions[bowlerSelected[0]]);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            bowlerSelected[0] = -1;
        });

        builder.show();

        if(bowlerSelected[0] == -1) {
            Toast.makeText(this, "You need to select a new bowler.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleRotateStrikeClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // basic
        rotateStrike();
        this.match.addToActivities("RotateStrike");
        this.syncMatch();
    }

    public void handleWideBallClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Runs scored on wide ball?");
        builder.setCancelable(false);
        int[] runsScoredOnBadDelivery = {-1};

        builder.setSingleChoiceItems(runsOptions, runsScoredOnBadDelivery[0],
                (dialog, which) -> runsScoredOnBadDelivery[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsScoredOnBadDelivery[0] == -1) {
                Toast.makeText(this, "You need to select runs scored on wide ball.", Toast.LENGTH_LONG).show();
            }
            System.out.println("Runs scored on bad delivery: " + runsScoredOnBadDelivery[0]);

            int runsScoredOnWideBall = runsScoredOnBadDelivery[0] + 1;
            System.out.println("Runs scored on wide ball: " + runsScoredOnWideBall);

            // update team score
            this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnWideBall);
            this.battingTeam.setExtras(this.battingTeam.getExtras() + runsScoredOnWideBall);
            this.battingTeam.updateRunRate();

            // wide ball runs will not go to the batsman

            // Update the over
            String activityValue = runsScoredOnBadDelivery[0] + "WD";
            this.currentOver.getOverSummary().add(activityValue);
            this.currentOver.incrementWides();
            this.currentOver.setExtras(this.currentOver.getExtras() + runsScoredOnWideBall);
            this.currentOver.setRuns(this.currentOver.getRuns() + runsScoredOnWideBall);

            // update bowler runs
            this.bowler.getMatchPlayerBowling().setRunsConceded(
                    this.bowler.getMatchPlayerBowling().getRunsConceded() + runsScoredOnWideBall);
            this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
            this.bowler.getMatchPlayerBowling().incrementWides();
            this.bowler.getMatchPlayerBowling().setExtrasConceded(
                    this.bowler.getMatchPlayerBowling().getExtrasConceded() + runsScoredOnWideBall
            );
            if(runsScoredOnBadDelivery[0] == 4) {
                this.bowler.getMatchPlayerBowling().incrementFoursConceded();
            } else if(runsScoredOnBadDelivery[0] == 6) {
                this.bowler.getMatchPlayerBowling().incrementSixesConceded();
            }
            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

            this.match.addToActivities(activityValue);
            if(runsScoredOnBadDelivery[0] % 2 == 1) {
                rotateStrike();
            }

            this.match.updateMatchCompleted();
            this.updateScore();
            this.syncMatch();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            runsScoredOnBadDelivery[0] = -1;
        });

        builder.show();

        if(runsScoredOnBadDelivery[0] == -1) {
            Toast.makeText(this, "You need to select runs scored on wide ball.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleNoBallClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Runs scored on no ball?");
        builder.setCancelable(false);
        int[] runsScoredOnBadDelivery = {-1};

        builder.setSingleChoiceItems(runsOptions, runsScoredOnBadDelivery[0],
                (dialog, which) -> runsScoredOnBadDelivery[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsScoredOnBadDelivery[0] == -1) {
                Toast.makeText(this, "You need to select runs scored on no ball.", Toast.LENGTH_LONG).show();
                return;
            }
            System.out.println("Runs scored on bad delivery: " + runsScoredOnBadDelivery[0]);

            int runsScoredOnNoBall = runsScoredOnBadDelivery[0] + 1;
            System.out.println("Runs scored on no ball: " + runsScoredOnNoBall);
            String batsmanActivity = Integer.toString(runsScoredOnBadDelivery[0]);

            int[] runsByeSelected = {-1};
            String[] runsByeOptions = {"Legal Runs", "Bye Runs"};

            AlertDialog.Builder byeRunsBuilder = new AlertDialog.Builder(MatchScoreActivity.this);
            byeRunsBuilder.setTitle("Are runs scored bye runs?");
            byeRunsBuilder.setCancelable(false);

            byeRunsBuilder.setSingleChoiceItems(runsByeOptions, runsByeSelected[0],
                    (dialog1, which1) -> runsByeSelected[0] = Integer.parseInt(runsByeOptions[which]));

            byeRunsBuilder.setNegativeButton("Cancel", (dialog1, which1) -> dialog1.dismiss());

            byeRunsBuilder.setNeutralButton("Clear", (dialog1, which1) -> {
                runsByeSelected[0] = -1;
            });

            byeRunsBuilder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(runsByeSelected[0] == -1) {
                    Toast.makeText(this, "You need to select if the runs scored are bye runs.", Toast.LENGTH_LONG).show();
                    return;
                }

                String runsByeValue = runsByeOptions[runsByeSelected[0]];
                System.out.println("Runs scored on bad delivery are : " + runsByeValue);

                // update team score
                this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnNoBall);
                this.battingTeam.setExtras(this.battingTeam.getExtras() + runsScoredOnNoBall);
                this.battingTeam.updateRunRate();

                this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails(batsmanActivity);
                this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
                if(runsByeValue.equals("Legal Runs")) {
                    // no ball runs will go to the batsman
                    this.strikerBatsman.getMatchPlayerBatting().setRunsScored(
                            this.strikerBatsman.getMatchPlayerBatting().getRunsScored() + runsScoredOnBadDelivery[0]
                    );
                    if(runsScoredOnBadDelivery[0] == 4) {
                        this.strikerBatsman.getMatchPlayerBatting().incrementFoursScored();
                    } else if (runsScoredOnBadDelivery[0] == 6) {
                        this.strikerBatsman.getMatchPlayerBatting().incrementSixesScored();
                    }
                    this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
                    this.strikerBatsman.getMatchPlayerBatting().updateRecords();
                } else {
                    this.currentOver.setByes(this.currentOver.getByes() + runsScoredOnBadDelivery[0]);
                }


                // Update the over
                String activityValue = runsScoredOnBadDelivery[0] + "NB";
                this.currentOver.getOverSummary().add(activityValue);
                this.currentOver.incrementNoBalls();
                this.currentOver.setExtras(this.currentOver.getExtras() + runsScoredOnNoBall);
                this.currentOver.setRuns(this.currentOver.getRuns() + runsScoredOnNoBall);

                // update bowler runs
                this.bowler.getMatchPlayerBowling().setRunsConceded(
                        this.bowler.getMatchPlayerBowling().getRunsConceded() + runsScoredOnNoBall);
                this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
                this.bowler.getMatchPlayerBowling().incrementNoBalls();
                if(runsScoredOnBadDelivery[0] == 4) {
                    this.bowler.getMatchPlayerBowling().incrementFoursConceded();
                } else if(runsScoredOnBadDelivery[0] == 6) {
                    this.bowler.getMatchPlayerBowling().incrementSixesConceded();
                }
                this.bowler.getMatchPlayerBowling().setExtrasConceded(
                        this.bowler.getMatchPlayerBowling().getExtrasConceded() + runsScoredOnNoBall
                );
                this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

                this.match.addToActivities(activityValue);
                if(runsScoredOnBadDelivery[0] % 2 == 1) {
                    rotateStrike();
                }

                this.match.updateMatchCompleted();
                this.updateScore();
                this.syncMatch();
            });

            byeRunsBuilder.show();

            if(runsByeSelected[0] == -1) {
                Toast.makeText(this, "You need to select if the runs scored are bye runs.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            runsScoredOnBadDelivery[0] = -1;
        });

        builder.show();

        if(runsScoredOnBadDelivery[0] == -1) {
            Toast.makeText(this, "You need to select runs scored on no ball.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleByeClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Runs scored on bye?");
        builder.setCancelable(false);
        int[] runsGivenOnBye = {-1};

        builder.setSingleChoiceItems(runsOptions, runsGivenOnBye[0],
                (dialog, which) -> runsGivenOnBye[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsGivenOnBye[0] == -1) {
                Toast.makeText(this, "You need to select runs given on bye.", Toast.LENGTH_LONG).show();
                return;
            }
            int runsScoredOnBye = runsGivenOnBye[0];
            System.out.println("Runs given on bye: " + runsScoredOnBye);

            // update team score
            this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnBye);
            this.battingTeam.setExtras(this.battingTeam.getRuns() + runsScoredOnBye);
            this.battingTeam.updateRunRate();

            // bye runs will not go to the batsman

            // Update the over
            String activityValue = runsScoredOnBye + "B";
            this.currentOver.getOverSummary().add(activityValue);
            this.currentOver.incrementNoBalls();
            this.currentOver.setExtras(this.currentOver.getExtras() + runsScoredOnBye);
            this.currentOver.setRuns(this.currentOver.getRuns() + runsScoredOnBye);

            // update bowler runs
            this.bowler.getMatchPlayerBowling().setRunsConceded(
                    this.bowler.getMatchPlayerBowling().getRunsConceded() + runsScoredOnBye);
            this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
            this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
            if(runsScoredOnBye == 4) {
                this.bowler.getMatchPlayerBowling().incrementFoursConceded();
            } else if(runsScoredOnBye== 6) {
                this.bowler.getMatchPlayerBowling().incrementSixesConceded();
            }
            this.bowler.getMatchPlayerBowling().setExtrasConceded(
                    this.bowler.getMatchPlayerBowling().getExtrasConceded() + runsScoredOnBye
            );
            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();


            this.match.addToActivities(activityValue);
            if(runsScoredOnBye % 2 == 1) {
                rotateStrike();
            }
            this.match.updateMatchCompleted();
            this.updateScore();
            this.syncMatch();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            runsGivenOnBye[0] = -1;
        });

        builder.show();

        if(runsGivenOnBye[0] == -1) {
            Toast.makeText(this, "You need to select runs scored on bye.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleBowledClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // update team score
        this.battingTeam.incrementWickets();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();
        this.battingTeam.updateFallOfWickets();

        // update batsman score
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(true);
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out-Bowled");
        this.strikerBatsman.getMatchPlayerBatting().setBowledBy(this.bowler.getPlayerName());
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());

        // update over
        this.currentOver.getOverSummary().add("Out");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementWickets();
        this.currentOver.updateOverCompleted();

        // update bowling record
        this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling().addToBowledPlayers(this.match.getStrikerBatsman());
        this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

        // update match activities
        this.match.addToActivities(this.match.getStrikerBatsman() + "-Out-Bowled");

        // check if match completed because before I select the new batsman, match might already
        // be over.
        this.match.updateMatchCompleted();

        if(this.battingTeam.isBattingInningsCompleted()) {
            this.updateScore();
            this.syncMatch();
            Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                            "Please start a new innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(this.match.isCompleted()) {
            this.updateScore();
            this.syncMatch();
            Toast.makeText(this, "Match is completed. Please end the innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Get list of not out Batsman
        String[] notOutBatsman = this.getNotOutBatsMan();
        int[] optionSelected = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select new Batsman?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                (dialog, which) -> optionSelected[0] = which);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(optionSelected[0] == -1) {
                Toast.makeText(this, "You need to select the new batsman.", Toast.LENGTH_LONG).show();
                return;
            }
            String newBatsman = notOutBatsman[optionSelected[0]];
            System.out.println("New batsman is: " + newBatsman);

            // update the striker batsman
            this.match.setStrikerBatsman(newBatsman);
            this.match.setStrikerBatsmanIndex(
                    this.battingTeam.getMatchPlayerIndex(newBatsman)
            );
            this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
            this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
            this.match.addToActivities(newBatsman + "-In");

            this.match.updateMatchCompleted();
            this.updateScore();
            this.syncMatch();
        });

        builder.setNeutralButton("Clear", (dialog, which) -> {
            optionSelected[0] = -1;
        });

        builder.show();

        if(optionSelected[0] == -1) {
            System.out.println("This condition should never arrive.");
            Toast.makeText(this, "You need to select the new batsman after a wicket.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleCaughtClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        int[] caughtBySelected = {-1};
        String[] allMatchPlayers = this.getFieldingPlayers();

        AlertDialog.Builder caughtBybuilder = new AlertDialog.Builder(MatchScoreActivity.this);
        caughtBybuilder.setTitle("Catch was taken by?");
        caughtBybuilder.setCancelable(false);

        caughtBybuilder.setSingleChoiceItems(allMatchPlayers, caughtBySelected[0],
                (dialog, which) -> caughtBySelected[0] = which);

        caughtBybuilder.setPositiveButton("Ok", (dialog, which) -> {
            if(caughtBySelected[0] == -1) {
                Toast.makeText(this, "You need to select who caught the ball.", Toast.LENGTH_LONG).show();
                return;
            }
            String caughtBy = allMatchPlayers[caughtBySelected[0]];
            System.out.println("Batsman was caught by: " + caughtBy);

            // before selecting new batsman, check if match is already completed.

            // update team score
            this.battingTeam.incrementWickets();
            this.battingTeam.incrementLegalDeliveriesPlayed();
            this.battingTeam.updateRunRate();
            this.battingTeam.updateFallOfWickets();

            // update batsman score
            this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
            this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
            this.strikerBatsman.getMatchPlayerBatting().updateRecords();
            this.strikerBatsman.getMatchPlayerBatting().setOut(true);
            this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out-Caught");
            this.strikerBatsman.getMatchPlayerBatting().setCoughtBy(caughtBy);
            this.strikerBatsman.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());

            // update over
            this.currentOver.getOverSummary().add("Out");
            this.currentOver.incrementLegalDeliveries();
            this.currentOver.incrementDotBalls();
            this.currentOver.incrementWickets();
            this.currentOver.updateOverCompleted();

            // update bowling record
            this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
            this.bowler.getMatchPlayerBowling().incrementDotsConceded();
            this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
            this.bowler.getMatchPlayerBowling().updateRecords();
            this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

            // update fielding record
            this.match.getMatchPlayerObject(caughtBy).getMatchPlayerFielding().incrementNoOfCatches();
            this.match.getMatchPlayerObject(caughtBy).getMatchPlayerFielding().addToCaughtPlayers(this.match.getStrikerBatsman());

            this.match.addToActivities(this.match.getStrikerBatsman() + "-Out-Caught");

            // update match
            this.match.updateMatchCompleted();
            this.syncMatch();
            this.updateScore();

            if(this.battingTeam.isBattingInningsCompleted()) {
                this.updateScore();
                this.syncMatch();
                Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                                "Please start a new innings.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(this.match.isCompleted()) {
                this.updateScore();
                this.syncMatch();
                Toast.makeText(this, "Match is completed. Please end the innings.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Get list of not out Batsman
            String[] notOutBatsman = this.getNotOutBatsMan();
            int[] optionSelected = {-1};

            AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
            builder.setTitle("Select new Batsman?");
            builder.setCancelable(false);

            builder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                    (dialog1, which1) -> optionSelected[0] = which1);

            builder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(optionSelected[0] == -1) {
                    Toast.makeText(this, "You need to select the new batsman.", Toast.LENGTH_LONG).show();
                    return;
                }
                String newBatsman = notOutBatsman[optionSelected[0]];
                System.out.println("New batsman is: " + newBatsman);

                // update the striker batsman
                this.match.setStrikerBatsman(newBatsman);
                this.match.setStrikerBatsmanIndex(
                        this.battingTeam.getMatchPlayerIndex(newBatsman)
                );
                this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
                this.match.addToActivities(newBatsman + "-In");

                this.updateScore();
                this.syncMatch();
            });

            builder.setNeutralButton("Clear", (dialog1, which1) -> {
                optionSelected[0] = -1;
            });

            builder.show();

            if(optionSelected[0] == -1) {
                System.out.println("This condition should never arrive.");
                Toast.makeText(this, "You need to select a new batsman after the wicket.", Toast.LENGTH_LONG).show();
            }
        });

        caughtBybuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        caughtBybuilder.setNeutralButton("Clear", (dialog, which) -> {
            caughtBySelected[0] = -1;
        });

        caughtBybuilder.show();


        if(caughtBySelected[0] == -1) {
            Toast.makeText(this, "You need to select who caught the ball.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void handleHitWicketClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        System.out.println("Batsman hit wicket.");

        // update team score
        this.battingTeam.incrementWickets();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();
        this.battingTeam.updateFallOfWickets();

        // update batsman score
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(true);
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out-Hit-Wicket");
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());

        // update over
        this.currentOver.getOverSummary().add("Out");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementWickets();
        this.currentOver.updateOverCompleted();

        // update bowling record
        this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

        // update match activities
        this.match.addToActivities(this.match.getStrikerBatsman() + "-Out-Hit-Wicket");

        // check if match completed because before I select the new batsman, match might already
        // be over.
        this.match.updateMatchCompleted();

        if(this.battingTeam.isBattingInningsCompleted()) {
            this.updateScore();
            this.syncMatch();
            Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                            "Please start a new innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(this.match.isCompleted()) {
            this.updateScore();
            this.syncMatch();
            Toast.makeText(this, "Match is completed. Please end the innings.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Get list of not out Batsman
        String[] notOutBatsman = this.getNotOutBatsMan();
        int[] optionSelected = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select new Batsman?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                (dialog, which) -> optionSelected[0] = which);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(optionSelected[0] == -1) {
                Toast.makeText(this, "You need to select the new batsman.", Toast.LENGTH_LONG).show();
                return;
            }
            String newBatsman = notOutBatsman[optionSelected[0]];
            System.out.println("New batsman is: " + newBatsman);

            // update the striker batsman
            this.match.setStrikerBatsman(newBatsman);
            this.match.setStrikerBatsmanIndex(
                    this.battingTeam.getMatchPlayerIndex(newBatsman)
            );
            this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
            this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
            this.match.addToActivities(newBatsman + "-In");

            this.updateScore();
            this.syncMatch();
        });

        builder.setNeutralButton("Clear", (dialog, which) -> {
            optionSelected[0] = -1;
        });

        builder.show();

        if(optionSelected[0] == -1) {
            System.out.println("Impossible condition.");
            Toast.makeText(this, "You need to select the new batsman after a wicket.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleRunOutClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        // Before updating anything, we need to know 4 things
        // run out player
        // runs scored on the ball
        // is ball a legal delivery [wide, no, none]
        // are runs scored bye?
        // the batsman which is out

        // then, update the match

        // select the new batsman if match is not completed.

        int[] runOutBySelected = {-1};
        String[] allMatchPlayers = this.getFieldingPlayers();

        AlertDialog.Builder runOutBybuilder = new AlertDialog.Builder(MatchScoreActivity.this);
        runOutBybuilder.setTitle("Run out by?");
        runOutBybuilder.setCancelable(false);

        runOutBybuilder.setSingleChoiceItems(allMatchPlayers, runOutBySelected[0],
                (dialog, which) -> runOutBySelected[0] = which);

        runOutBybuilder.setPositiveButton("Ok", (dialog, which) -> {
            if(runOutBySelected[0] == -1) {
                Toast.makeText(this, "You need to select the player who was responsible for runout.", Toast.LENGTH_LONG).show();
                return;
            }

            String runOutByPlayer = allMatchPlayers[runOutBySelected[0]];
            System.out.println("The player responsible for runout is: " + runOutByPlayer);

            int[] runsScoredSelected = {-1};
            AlertDialog.Builder runsScoredbuilder = new AlertDialog.Builder(MatchScoreActivity.this);
            runsScoredbuilder.setTitle("Runs scored on ball?");
            runsScoredbuilder.setCancelable(false);

            runsScoredbuilder.setSingleChoiceItems(runsOptions, runsScoredSelected[0],
                    (dialog1, which1) -> runsScoredSelected[0] = which1);

            runsScoredbuilder.setNegativeButton("Cancel", (dialog1, which1) -> dialog1.dismiss());

            runsScoredbuilder.setNeutralButton("Clear", (dialog1, which1) -> {
                runsScoredSelected[0] = -1;
            });

            runsScoredbuilder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(runsScoredSelected[0] == -1) {
                    Toast.makeText(this, "You need to select the runs scored on run out.",
                            Toast.LENGTH_LONG).show();
                }

                int runsScoredOnBall = Integer.parseInt(runsOptions[runsScoredSelected[0]]);
                System.out.println("The runs scored on run out are: " + runsScoredOnBall);

                int[] runsByeSelected = {-1};
                String[] runsByeOptions = {"Legal Runs", "Bye Runs"};

                AlertDialog.Builder runsByeBuilder = new AlertDialog.Builder(MatchScoreActivity.this);
                runsByeBuilder.setTitle("Are the runs scored extra runs?");
                runsByeBuilder.setCancelable(false);

                runsByeBuilder.setSingleChoiceItems(runsByeOptions, runsByeSelected[0],
                        (dialog5, which5) -> runsByeSelected[0] = which5);

                runsByeBuilder.setNegativeButton("Cancel", (dialog5, which5) -> dialog5.dismiss());

                runsByeBuilder.setNeutralButton("Clear", (dialog5, which5) -> {
                    runsByeSelected[0] = -1;
                });

                runsByeBuilder.setPositiveButton("Ok", (dialog5, which5) -> {
                    if(runsByeSelected[0] == -1) {
                        Toast.makeText(this, "You need to select if the runs scored are bye runs.",
                                Toast.LENGTH_LONG).show();
                    }

                    String runsScoredBye = runsByeOptions[runsByeSelected[0]];
                    System.out.println("The runs scored are: " + runsScoredBye);

                    int[] ballTypeSelected = {-1};
                    String[] ballTypeOptions = {"Legal Delivery", "Wide Ball", "No Ball"};

                    AlertDialog.Builder ballTypeBuilder = new AlertDialog.Builder(MatchScoreActivity.this);
                    ballTypeBuilder.setTitle("Is it a legal delivery?");
                    ballTypeBuilder.setCancelable(false);

                    ballTypeBuilder.setSingleChoiceItems(ballTypeOptions, ballTypeSelected[0],
                            (dialog2, which2) -> ballTypeSelected[0] = which2);

                    ballTypeBuilder.setNegativeButton("Cancel", (dialog2, which2) -> dialog2.dismiss());

                    ballTypeBuilder.setNeutralButton("Clear", (dialog2, which2) -> {
                        ballTypeSelected[0] = -1;
                    });

                    ballTypeBuilder.setPositiveButton("Ok", (dialog2, which2) -> {
                        if(ballTypeSelected[0] == -1) {
                            Toast.makeText(this, "You need to select the ball type bowled.",
                                    Toast.LENGTH_LONG).show();
                        }

                        String ballType = ballTypeOptions[ballTypeSelected[0]];
                        System.out.println("Selected ball type is: " + ballType);

                        // Select run out batsman
                        int[] runOutBatsmanSelected = {-1};
                        String[] runOutBatsmanOptions = {this.strikerBatsman.getPlayerName(),
                                this.nonStrikeBatsman.getPlayerName()};

                        AlertDialog.Builder runOutBatsmanBuilder = new AlertDialog.Builder(MatchScoreActivity.this);
                        runOutBatsmanBuilder.setTitle("Select the batsman who is runout?");
                        runOutBatsmanBuilder.setCancelable(false);

                        runOutBatsmanBuilder.setSingleChoiceItems(runOutBatsmanOptions, runOutBatsmanSelected[0],
                                (dialog3, which3) -> runOutBatsmanSelected[0] = which3);

                        runOutBatsmanBuilder.setNegativeButton("Cancel", (dialog3, which3) -> dialog3.dismiss());

                        runOutBatsmanBuilder.setNeutralButton("Clear", (dialog3, which3) -> {
                            runOutBatsmanSelected[0] = -1;
                        });

                        runOutBatsmanBuilder.setPositiveButton("Ok", (dialog3, which3) -> {
                            if(runOutBatsmanSelected[0] == -1) {
                                Toast.makeText(this, "You need to select the batsman who is run out.",
                                        Toast.LENGTH_LONG).show();
                            }

                            // Point of no return

                            String runOutBatsman = runOutBatsmanOptions[runOutBatsmanSelected[0]];
                            MatchPlayer runOutPlayerObject = this.battingTeam.getMatchPlayerFromName(runOutBatsman);
                            System.out.println("The batsman who is runout is: " + runOutBatsman);

                            int ballExtraRun = 0;
                            if(!ballType.equals("Legal Delivery")) {
                                System.out.println("This is not a legal delivery.");
                                // this will go to bowler
                                ballExtraRun++;
                            }

                            int batExtraRuns = 0;
                            int batLegalRuns = 0;
                            if(runsScoredBye.equals("Legal Runs")) {
                                // this will go to batsman
                                batLegalRuns = batLegalRuns + runsScoredOnBall;
                            } else {
                                // this will go to bowler
                                batExtraRuns = batExtraRuns + runsScoredOnBall;
                            }

                            System.out.println("Ball Extra Runs: " + ballExtraRun);
                            System.out.println("Bat Extra Runs: " + batExtraRuns);
                            System.out.println("Bat Legal Runs: " + batLegalRuns);

                            // update team score
                            this.battingTeam.incrementWickets();
                            if(ballExtraRun == 0) {
                                System.out.println("This is a legal delivery on run out.");
                                // legal delivery
                                this.battingTeam.incrementLegalDeliveriesPlayed();
                                this.currentOver.incrementLegalDeliveries();
                                this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
                                // a legal delivery
                                runOutPlayerObject.getMatchPlayerBatting().incrementBallsPlayed();
                                runOutPlayerObject.getMatchPlayerBatting().setRunsScored(
                                        runOutPlayerObject.getMatchPlayerBatting().getRunsScored() + batLegalRuns
                                );
                                runOutPlayerObject.getMatchPlayerBatting().updateStrikeRate();
                                runOutPlayerObject.getMatchPlayerBatting().updateRecords();
                            }

                            // anyone of them depending on the conditions is zero
                            this.battingTeam.setRuns(this.battingTeam.getRuns() + ballExtraRun + batExtraRuns + batLegalRuns);
                            this.battingTeam.updateRunRate();
                            this.battingTeam.updateFallOfWickets();

                            // UPDATE BATSMAN RECORD
                            runOutPlayerObject.getMatchPlayerBatting().setOut(true);
                            runOutPlayerObject.getMatchPlayerBatting().addToBattingDetails("Out-Runout");
                            runOutPlayerObject.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());
                            runOutPlayerObject.getMatchPlayerBatting().setRunOutBy(runOutByPlayer);

                            // update over
                            this.currentOver.getOverSummary().add("Out");
                            this.currentOver.incrementWickets();
                            this.currentOver.updateOverCompleted();

                            // update bowler records
                            if(ballExtraRun + batLegalRuns + batExtraRuns == 0) {
                                System.out.println("Dot ball on run out.");
                                this.bowler.getMatchPlayerBowling().incrementDotsConceded();
                                this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
                                this.currentOver.incrementDotBalls();
                            }

                            // "Wide Ball", "No Ball"
                            if(ballType.equals("Wide Ball")) {
                                System.out.println("Wide ball on run out.");
                                this.bowler.getMatchPlayerBowling().incrementWides();
                                this.bowler.getMatchPlayerBowling().setExtrasConceded(
                                        this.bowler.getMatchPlayerBowling().getExtrasConceded() +
                                                ballExtraRun + batLegalRuns + batExtraRuns
                                );
                                this.currentOver.incrementWides();
                                this.currentOver.setExtras(
                                        this.currentOver.getExtras() + ballExtraRun + batLegalRuns + batExtraRuns
                                );

                                this.battingTeam.setExtras(this.battingTeam.getExtras() +
                                        ballExtraRun + batExtraRuns + batLegalRuns);
                            }

                            // update extras
                            if(ballType.equals("No Ball")) {
                                System.out.println("No ball on run out.");
                                this.bowler.getMatchPlayerBowling().incrementNoBalls();
                                this.bowler.getMatchPlayerBowling().setExtrasConceded(
                                        this.bowler.getMatchPlayerBowling().getExtrasConceded() +
                                                ballExtraRun + batLegalRuns + batExtraRuns
                                );
                                this.currentOver.incrementNoBalls();
                                this.currentOver.setExtras(
                                        this.currentOver.getExtras() + ballExtraRun + batLegalRuns + batExtraRuns
                                );

                                this.battingTeam.setExtras(this.battingTeam.getExtras() +
                                        ballExtraRun + batExtraRuns + batLegalRuns);
                            }

                            // runs conceded
                            this.currentOver.setRuns(
                                    this.currentOver.getRuns() + ballExtraRun + batLegalRuns + batExtraRuns
                            );
                            this.bowler.getMatchPlayerBowling().setRunsConceded(
                                    this.bowler.getMatchPlayerBowling().getRunsConceded()
                                            + ballExtraRun + batLegalRuns + batExtraRuns
                            );
                            this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
                            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
                            this.bowler.getMatchPlayerBowling().updateRecords();
                            this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

                            this.match.updateMatchCompleted();
                            this.syncMatch();
                            this.updateScore();

                            // Check if match is already completed.
                            if(this.battingTeam.isBattingInningsCompleted()) {
                                this.updateScore();
                                this.syncMatch();
                                Toast.makeText(this, this.match.getInnings() + " Innings is completed. " +
                                                "Please start a new innings.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            if(this.match.isCompleted()) {
                                this.updateScore();
                                this.syncMatch();
                                Toast.makeText(this, "Match is completed. Please end the innings.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Get list of not out Batsman
                            String[] notOutBatsman = this.getNotOutBatsMan();
                            int[] optionSelected = {-1};

                            AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
                            builder.setTitle("Select new Batsman?");
                            builder.setCancelable(false);

                            builder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                                    (dialog6, which6) -> optionSelected[0] = which6);

                            builder.setPositiveButton("Ok", (dialog6, which6) -> {
                                if(optionSelected[0] == -1) {
                                    Toast.makeText(this, "You need to select the new batsman.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                String newBatsman = notOutBatsman[optionSelected[0]];
                                System.out.println("New batsman is: " + newBatsman);

                                // update the striker batsman or non striker batsman
                                if(runOutBatsman.equals(this.match.getStrikerBatsman())) {
                                    // update striker batsman
                                    this.match.setStrikerBatsman(newBatsman);
                                    this.match.setStrikerBatsmanIndex(
                                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                                    );
                                    this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                                    this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
                                    this.match.addToActivities(newBatsman + "-In");
                                } else {
                                    // update non striker batsman
                                    this.match.setNonStrikeBatsman(newBatsman);
                                    this.match.setNonStrikerBatsmanIndex(
                                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                                    );
                                    this.nonStrikeBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                                    this.nonStrikeBatsman.getMatchPlayerBatting().setBatted(true);
                                    this.match.addToActivities(newBatsman + "-In");
                                }

                                this.updateScore();
                                this.syncMatch();
                            });

                            builder.setNeutralButton("Clear", (dialog6, which6) -> {
                                optionSelected[0] = -1;
                            });

                            builder.show();

                            if(optionSelected[0] == -1) {
                                System.out.println("This condition should never arrive.");
                                Toast.makeText(this, "You need to select a new batsman after the wicket.", Toast.LENGTH_LONG).show();
                            }


                        });

                        runOutBatsmanBuilder.show();

                        if(runOutBatsmanSelected[0] == -1) {
                            Toast.makeText(this, "You need to select the batsman which is run out.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    ballTypeBuilder.show();
                    if(ballTypeSelected[0] == -1) {
                        Toast.makeText(this, "You need to select the ball type on run out.",
                                Toast.LENGTH_LONG).show();
                    }

                });

                runsByeBuilder.show();

                if(runsByeSelected[0] == -1) {
                    Toast.makeText(this, "You need to select if the runs scored are bye runs.",
                            Toast.LENGTH_LONG).show();
                }
            });

            runsScoredbuilder.show();

            if(runsScoredSelected[0] == -1) {
                Toast.makeText(this, "You need to select the runs scored on run out.",
                        Toast.LENGTH_LONG).show();
            }
        });

        runOutBybuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        runOutBybuilder.setNeutralButton("Clear", (dialog, which) -> {
            runOutBySelected[0] = -1;
        });

        runOutBybuilder.show();

        if(runOutBySelected[0] == -1) {
            Toast.makeText(this, "You need to select who caught the ball.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void handleRetireClick(View view) {
        System.out.println("Clicked on retire batsman.");
        // do everything after over completion
        if(this.checkCompletions()) {
            return;
        }

        // select the batsman be to retired
        // select the new batsman from the remaining batsman
        // replace new batsman with existing batsman
        // Get list of not out Batsman
        String[] retireBatsmanOptions = {this.strikerBatsman.getPlayerName(),
                this.nonStrikeBatsman.getPlayerName()};
        int[] retireBatsmanSelected = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select the batsman to be retired?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(retireBatsmanOptions, retireBatsmanSelected[0],
                (dialog, which) -> retireBatsmanSelected[0] = which);

        builder.setNeutralButton("Clear", (dialog, which) -> {
            retireBatsmanSelected[0] = -1;
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(retireBatsmanSelected[0] == -1) {
                Toast.makeText(this, "You need to select the batsman who is going to retire.", Toast.LENGTH_LONG).show();
                return;
            }

            String batsmanToRetire = retireBatsmanOptions[retireBatsmanSelected[0]];
            System.out.println("Batsman to retire is: " + batsmanToRetire);

            // Get list of not out Batsman
            String[] notOutBatsman = this.getNotOutBatsMan();
            int[] optionSelected = {-1};
            AlertDialog.Builder newBatsmanBuilder = new AlertDialog.Builder(MatchScoreActivity.this);
            newBatsmanBuilder.setTitle("Select new Batsman?");
            newBatsmanBuilder.setCancelable(false);

            newBatsmanBuilder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                    (dialog1, which1) -> optionSelected[0] = which1);

            newBatsmanBuilder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(optionSelected[0] == -1) {
                    Toast.makeText(this, "You need to select the new batsman.", Toast.LENGTH_LONG).show();
                    return;
                }
                String newBatsman = notOutBatsman[optionSelected[0]];
                System.out.println("New batsman is: " + newBatsman);

                if(batsmanToRetire.equals(this.match.getStrikerBatsman())) {
                    // update the striker batsman
                    this.match.setStrikerBatsman(newBatsman);
                    this.match.setStrikerBatsmanIndex(
                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                    );
                    this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                    this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
                } else {
                    this.match.setNonStrikeBatsman(newBatsman);
                    this.match.setNonStrikerBatsmanIndex(
                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                    );
                    this.nonStrikeBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                    this.nonStrikeBatsman.getMatchPlayerBatting().setBatted(true);
                }
                this.match.addToActivities(batsmanToRetire + "-Retired");
                this.match.addToActivities(newBatsman + "-In");

                this.updateScore();
                this.syncMatch();
            });

            newBatsmanBuilder.setNeutralButton("Clear", (dialog1, which1) -> {
                optionSelected[0] = -1;
            });

            newBatsmanBuilder.setNegativeButton("Cancel", (dialog1, which1) -> dialog1.dismiss());

            newBatsmanBuilder.show();

            if(optionSelected[0] == -1) {
                Toast.makeText(this, "You need to select the new batsman after a wicket.", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();

        if(retireBatsmanSelected[0] == -1) {
            Toast.makeText(this, "You need to select the batsman who is going to retire.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleEditPlayersClick(View view) {
        System.out.println("Edit the current match players.");
        // by edit players?
        // add new players?
        //
    }

    public void handleEditOversClick(View view) {
        System.out.println("Edit overs clicked.");

        if(this.checkInningsCompletion()) {
            return;
        }

        String[] overIncreaseOptions = {"1", "2", "3", "-1", "-2", "-3"};
        int[] overIncreaseSelected = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select the number of overs to increase or decrease?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(overIncreaseOptions, overIncreaseSelected[0],
                (dialog, which) -> overIncreaseSelected[0] = which);

        builder.setNeutralButton("Clear", (dialog, which) -> {
            overIncreaseSelected[0] = -1;
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Ok", (dialog1, which1) -> {
            if(overIncreaseSelected[0] == -1) {
                Toast.makeText(this, "You need to select the number of overs to increase.", Toast.LENGTH_LONG).show();
                return;
            }

            int oversToIncrease = Integer.parseInt(overIncreaseOptions[overIncreaseSelected[0]]);
            System.out.println("Number of overs to increase: " + oversToIncrease);

            if(oversToIncrease < 0) {
                int currentOverPlayed = this.battingTeam.getCurrentOverBatting();
                int maxOvers = this.match.getMaxOvers();
                if(maxOvers + oversToIncrease <= currentOverPlayed) {
                    Toast.makeText(this, "Invalid selection of overs." + currentOverPlayed + " overs are already completed.", Toast.LENGTH_LONG).show();
                    return;
                }

                this.match.setMaxOvers(
                        this.match.getMaxOvers() + oversToIncrease
                );
                this.match.getTeamA().decrementMaxOvers(oversToIncrease);
                this.match.getTeamB().decrementMaxOvers(oversToIncrease);

            } else {
                // Set match properties
                this.match.setMaxOvers(
                        this.match.getMaxOvers() + oversToIncrease
                );
                this.match.getTeamA().incrementMaxOvers(oversToIncrease);
                this.match.getTeamB().incrementMaxOvers(oversToIncrease);
            }

            this.syncMatch();
            this.updateScore();
        });

        builder.show();

        if(overIncreaseSelected[0] == -1) {
            Toast.makeText(this, "You need to select the number of overs to increase.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleInningsEndClick(View view) {
        System.out.println("Innings end clicked.");
        // Innings complete
        if(this.battingTeam.isBattingInningsCompleted()) {
            if(this.match.getInnings() == 1) {
                System.out.println("First Innings Completed. Start the second innings now.");

                // Open a new dialogue box here to ask to start second innings.
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
                builder.setTitle("Start second innings?");
                builder.setCancelable(false);

                String[] options = {"Yes", "No"};
                final int[] optionSelected = {-1};

                builder.setSingleChoiceItems(options, optionSelected[0],
                        (dialog, which) -> optionSelected[0] = which);

                builder.setPositiveButton("Ok", (dialog, which) -> {
                    if(optionSelected[0] == -1) {
                        Toast.makeText(this, "Select Yes to start a the new innings.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer = options[optionSelected[0]];
                    System.out.println("Option selected is: " + answer);

                    if(answer.equals("Yes")) {
                        // update innings
                        this.match.setInnings(2);
                        // update bowling and batting teams
                        this.match.setBattingAndBowlingTeamNames();
                        this.syncMatch();

                        System.out.println("Go to the screen to select the new openors for second innings.");
                        System.out.println("Opening Select Openers Activity");
                        Intent intent = new Intent(this, SelectOpenersActivity.class);
                        intent.putExtra("data_files_hashmap", this.dataFilesMap);
                        intent.putExtra("match_object", this.match);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.setNeutralButton("Clear", (dialog, which) -> {
                    optionSelected[0] = -1;
                });

                builder.show();
            }

            if(this.match.getInnings() == 2) {
                System.out.println("Second Innings Completed. Match is finished.");
                // Open a new dialogue box here to ask to end the match.
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
                builder.setTitle("End the match?");
                builder.setCancelable(false);

                String[] options = {"Yes", "No"};
                final int[] optionSelected = {-1};

                builder.setSingleChoiceItems(options, optionSelected[0],
                        (dialog, which) -> optionSelected[0] = which);

                builder.setPositiveButton("Ok", (dialog, which) -> {
                    if(optionSelected[0] == -1) {
                        Toast.makeText(this, "Select Yes to end the match.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer = options[optionSelected[0]];
                    System.out.println("Option selected is: " + answer);

                    if(answer.equals("Yes")) {
                        this.match.setCompleted(true);
                        this.match.updateResult();
                        this.syncMatch();

                        System.out.println("Go to match information.");
                        Intent intent = new Intent(this, MatchInformationActivity.class);
                        intent.putExtra("data_files_hashmap", this.dataFilesMap);
                        intent.putExtra("match_object", match);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.setNeutralButton("Clear", (dialog, which) -> {
                    optionSelected[0] = -1;
                });

                builder.show();
            }
        } else {
            Toast.makeText(this, "You cannot end the innings yet.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleUndoClick(View view) {
        // TODO: This needs to be implemented properly and later
    }

}