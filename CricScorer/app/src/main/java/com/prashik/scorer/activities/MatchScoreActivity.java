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
    int runsScoredOnBadDelivery = -1;

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

        }

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

        this.match.addToActivities("0");
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

        this.rotateStrike();
        this.match.addToActivities("1");
        this.updateScore();
        this.syncMatch();
    }

    public void handleTwoClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
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
        this.updateScore();
        this.syncMatch();
    }

    public void handleThreeClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
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
        this.updateScore();
        this.syncMatch();
    }

    public void handleFourClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
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
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // Update the over
        this.currentOver.getOverSummary().add("4");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addFourToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addFourToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("4");
        this.updateScore();
        this.syncMatch();
    }

    public void handleFiveClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
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
        this.updateScore();
        this.syncMatch();
    }

    public void handleSixClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // update team score
        this.battingTeam.addSixToRuns();
        this.battingTeam.incrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("6");
        this.strikerBatsman.getMatchPlayerBatting().addSixToRunsScored();
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
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        this.match.addToActivities("6");
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

        // Over is now completed. Check if innings is completed
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

    }

    public void handleRotateStrikeClick(View view) {
        // basic
        rotateStrike();
        this.match.addToActivities("RotateStrike");
        this.syncMatch();
    }

    public void handleUndoClick(View view) {
        // TODO: This needs to be implemented properly and later
    }

    public void handleWideBallClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        int runsScoredOnWideBall = -1;

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Runs scored on wide ball?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(runsOptions, runsScoredOnBadDelivery,
                (dialog, which) -> runsScoredOnBadDelivery = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsScoredOnBadDelivery == -1) {
                Toast.makeText(this, "You need to select runs scored on wide ball.", Toast.LENGTH_LONG).show();
            }
            System.out.println("Runs scored on bad delivery: " + runsScoredOnBadDelivery);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            runsScoredOnBadDelivery = -1;
        });

        builder.show();

        if(runsScoredOnBadDelivery == -1) {
            Toast.makeText(this, "You need to select runs scored on wide ball.", Toast.LENGTH_LONG).show();
            return;
        }

        runsScoredOnWideBall = this.runsScoredOnBadDelivery + 1;
        System.out.println("Runs scored on wide ball: " + runsScoredOnWideBall);

        // update team score
        this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnWideBall);
        this.battingTeam.updateRunRate();

        // wide ball runs will not go to the batsman

        // Update the over
        String activityValue = this.runsScoredOnBadDelivery + "WD";
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
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();


        this.match.addToActivities(activityValue);
        if(this.runsScoredOnBadDelivery % 2 == 1) {
            rotateStrike();
        }
        this.runsScoredOnBadDelivery = -1;
        this.updateScore();
        this.syncMatch();
    }

    public void handleNoBallClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: need option to again see runs scored
    }

    public void handleEditOversClick(View view) {
        // TODO: this will be handled later
    }

    public void handleByeClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: give option to select runs scored
    }

    public void handleBowledClick(View view) {
        if(this.currentOver.isOverCompleted()) {
            Toast.makeText(this, "Over is completed. Please start a new over.",
                    Toast.LENGTH_LONG).show();
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
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out");
        this.strikerBatsman.getMatchPlayerBatting().setBowledBy(this.match.getCurrentBowler());

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

        this.match.addToActivities("Out");
        // TODO: handle selection of the new batsman here

        this.updateScore();
        this.syncMatch();
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