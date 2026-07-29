package com.prashik.scorer.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import java.util.Collections;
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
        this.dataFilesMap = (HashMap<String, String>) getIntent()
                .getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.filesDirectory = this.dataFilesMap.get("files_directory");

        if(!resumeMatch) {
            System.out.println("This is the first time we are playing this match.");
            // Check if this match already exists
            boolean alreadyExists = Utils.isMatchAlreadyExists(this.filesDirectory, this.match);
            if(alreadyExists) {
                Toast.makeText(this, "The match with similar details already exists. "
                        + "Please edit the existing match or delete that match first."
                        , Toast.LENGTH_LONG).show();
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
            Log.d("debug", String.format("Data file location: key - %s, location - %s"
                    , s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
            if (s.equals("players_name_to_id_map_file_location")) {
                this.nameToIdMap = Utils.readNameToIdMapFile(dataFile);
            }
        }

        System.out.println("All Players Object: " + this.allPlayers.toString());
        System.out.println("Name to Id Map object: " + this.nameToIdMap.toString());

        System.out.println("Match Object: ");
        System.out.println(this.match);

        this.strikerBatsman = this.battingTeam
                .getMatchPlayerFromName(this.match.getStrikerBatsman());
        this.nonStrikeBatsman = this.battingTeam
                .getMatchPlayerFromName(this.match.getNonStrikeBatsman());
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("DefaultLocale")
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
        String runsScored = Integer.toString(this.strikerBatsman.getMatchPlayerBatting()
                .getRunsScored());
        String ballsPlayed = Integer.toString(this.strikerBatsman.getMatchPlayerBatting()
                .getBallsPlayed());
        String strikerNameToDisplay = strikePlayerSplit[0] + " "
                + runsScored + "(" + ballsPlayed + ")";
        strikerName.setText(strikerNameToDisplay);

        // show non striker
        String[] nonStrikePlayerSplit = this.match.getNonStrikeBatsman().split(" ");
        runsScored = Integer.toString(this.nonStrikeBatsman.getMatchPlayerBatting()
                .getRunsScored());
        ballsPlayed = Integer.toString(this.nonStrikeBatsman.getMatchPlayerBatting()
                .getBallsPlayed());
        String nonStrikerNameToDisplay = nonStrikePlayerSplit[0] + " "
                + runsScored + "(" + ballsPlayed + ")";
        TextView nonStrikerName = findViewById(R.id.non_striker_name_ms);
        nonStrikerName.setText(nonStrikerNameToDisplay);

        // show bowler
        TextView bowlerNameText = findViewById(R.id.current_bowler_ms);
        String wickets = Integer.toString(this.bowler.getMatchPlayerBowling().getWicketsTaken());
        String runsGiven = Integer.toString(this.bowler.getMatchPlayerBowling().getRunsConceded());
        String overBowled = Integer.toString(this.bowler.getMatchPlayerBowling()
                .getLegalDeliveriesBowled()/6);
        String ballsBowled = Integer.toString(this.bowler.getMatchPlayerBowling()
                .getLegalDeliveriesBowled() % 6);

        String temp = String.format("%s \n%s-%s(%s.%s)", this.bowler
                        .getPlayerName().split(" ")[0],
                wickets, runsGiven, overBowled, ballsBowled);
        bowlerNameText.setText(temp);

        // show over details
        TextView overDetailsText = findViewById(R.id.current_over_details_ms);
        overDetailsText.setText(overDetails);
    }

    public void rebalanceTeams(ArrayList<String> team1, ArrayList<String> team2
            , String commonPlayer) {
        // Create new player objects
        System.out.println("Add players to match players.");
        // team1 -> all players of batting team
        // team2 -> all players of bowling team
        for(String player: team1) {
            this.match.addToMatchPlayers(player);
        }

        for(String player: team2) {
            this.match.addToMatchPlayers(player);
        }

        System.out.println("Removing common name.");
        this.battingTeam.setCommonName("");
        this.bowlingTeam.setCommonName("");

        System.out.println("Get batting team captain.");
        MatchPlayer battingCaptain = this.battingTeam
                .getMatchPlayerFromName(this.battingTeam.getCaptainName());
        System.out.println("Remove captain first");
        this.battingTeam.removeFromTeamPlayers(battingCaptain);

        System.out.println("Remove all players from " + this.battingTeam.getName());
        ArrayList<MatchPlayer> removedPlayers = new ArrayList<>(this.battingTeam.getTeamPlayers());

        // clear arrays and remove everyone
        this.battingTeam.getTeamPlayers().clear();
        this.battingTeam.getPlayerNames().clear();

        // add captain back
        this.battingTeam.addToTeam(battingCaptain);

        System.out.println("Remove all players from " + this.bowlingTeam.getName()
                + " except the captain.");
        MatchPlayer bowlingCaptain = this.bowlingTeam
                .getMatchPlayerFromName(this.bowlingTeam.getCaptainName());
        this.bowlingTeam.removeFromTeamPlayers(bowlingCaptain);

        removedPlayers.addAll(this.bowlingTeam.getTeamPlayers());

        this.bowlingTeam.getTeamPlayers().clear();
        this.bowlingTeam.getPlayerNames().clear();

        // add captain back
        this.bowlingTeam.addToTeam(bowlingCaptain);

        System.out.println("Updated team objects: ");
        System.out.println("Batting team: " + this.battingTeam.toString());
        System.out.println("Bowling team: " + this.bowlingTeam.toString());
        System.out.println("All Removed players: " + removedPlayers);

        for(String playerName: team1) {
            MatchPlayer matchPlayer = Utils.getMatchPlayersFromList(removedPlayers, playerName,
                    nameToIdMap, allPlayers);
            this.battingTeam.addToTeam(matchPlayer);
        }

        for(String playerName: team2) {
            MatchPlayer matchPlayer = Utils.getMatchPlayersFromList(removedPlayers, playerName,
                    nameToIdMap, allPlayers);
            this.bowlingTeam.addToTeam(matchPlayer);
        }

        System.out.println("Updated team objects after adding the new players");
        System.out.println("Batting team: " + this.battingTeam.toString());
        System.out.println("Bowling team: " + this.bowlingTeam.toString());
        System.out.println("All Removed players: " + removedPlayers);
        System.out.println("Removed players should now be empty.");

        // set common player
        if(!commonPlayer.isEmpty()) {
            System.out.println("Updating common player in both teams.");
            this.battingTeam.setCommonName(commonPlayer);
            this.bowlingTeam.setCommonName(commonPlayer);

            System.out.println("Batting team Common: " + this.battingTeam.getCommonName());
            System.out.println("Bowling team Common: " + this.bowlingTeam.getCommonName());
        } else {
            System.out.println("No new common player.");
        }

        if(this.battingTeam.getTeamSize() != this.bowlingTeam.getTeamSize()) {
            throw new RuntimeException("Team size not equal after rebalancing.");
        }

        System.out.println("Rebalancing complete. Syncing match.");
        this.syncMatch();
        this.updateScore();
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
        String[] returnArray = temp.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }

    public String[] getFieldingPlayers() {
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<this.match.getMatchPlayers().size(); i++) {
            if(!this.match.getMatchPlayers().get(i).equals(this.strikerBatsman.getPlayerName())
                && !this.match.getMatchPlayers()
                    .get(i).equals(this.nonStrikeBatsman.getPlayerName())) {
                temp.add(this.match.getMatchPlayers().get(i));
            }
        }
        String[] returnArray = temp.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
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
        this.currentOver.addToOverSummary("0");
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.updateOverCompleted();

        // Add dot ball info to current bowler
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("0");
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 0 Run";
        this.match.addToActivities(activity);
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoZeroRuns() {
        // remove from activities
        this.match.removeLastElementFromActivities();

        // update bowler
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementDotsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // update the over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementDotBalls();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.updateOverCompleted();

        // update batsman
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().decrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();

        // update team
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
        this.currentOver.addToOverSummary("1");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementRuns();
        this.currentOver.updateOverCompleted();

        // Add single run info to current bowler
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("1");
        this.bowler.getMatchPlayerBowling().incrementRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 1 Run";
        this.rotateStrike();
        this.match.addToActivities(activity);
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoOneRun() {
        // rotate strike
        this.rotateStrike();
        this.match.removeLastElementFromActivities();

        // update bowler
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // update over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementRuns();
        this.currentOver.updateOverCompleted();

        // remove single
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
        this.currentOver.addToOverSummary("2");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addTwoToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("2");
        this.bowler.getMatchPlayerBowling().addTwoToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 2 Runs";
        this.match.addToActivities(activity);
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoTwoRuns() {
        this.match.removeLastElementFromActivities();

        // update bowler runs
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementTwoFromRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // update the over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementTwoFromRuns();
        this.currentOver.updateOverCompleted();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementTwoFromRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementTwoFromRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
        this.currentOver.addToOverSummary("3");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addThreeToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("3");
        this.bowler.getMatchPlayerBowling().addThreeToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 3 Runs";
        this.match.addToActivities(activity);
        this.rotateStrike();
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoThreeRuns() {
        // update match
        this.rotateStrike();
        this.match.removeLastElementFromActivities();

        // update bowler
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementThreeFromRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // update over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementThreeFromRuns();
        this.currentOver.updateOverCompleted();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementThreeFromRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementThreeFromRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        // sync match
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
        this.currentOver.addToOverSummary("4");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addFourToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("4");
        this.bowler.getMatchPlayerBowling().addFourToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementFoursConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 4 Runs";
        this.match.addToActivities(activity);
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }
    public boolean undoFourRuns() {
        this.match.removeLastElementFromActivities();

        // update bowler
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementFourFromRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementFoursConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // Update the over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementFourFromRuns();
        this.currentOver.updateOverCompleted();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementFourFromRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().decrementFoursScored();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementFourFromRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
    }

    public void handleFiveClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        System.out.println("Score 5");
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
        this.currentOver.addToOverSummary("5");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addFiveToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("5");
        this.bowler.getMatchPlayerBowling().addFiveToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 5 Runs";
        this.match.addToActivities(activity);
        this.rotateStrike();
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoFiveRuns() {
        this.rotateStrike();
        this.match.removeLastElementFromActivities();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementFiveFromRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // Update the over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementFiveFromRuns();
        this.currentOver.updateOverCompleted();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementFiveFromRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementFiveFromRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
        this.currentOver.addToOverSummary("6");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.addSixToRuns();
        this.currentOver.updateOverCompleted();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("6");
        this.bowler.getMatchPlayerBowling().addSixToRunsConceded();
        this.bowler.getMatchPlayerBowling().incrementSixesConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " - 6 Runs";
        this.match.addToActivities(activity);
        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoSixruns() {
        this.match.removeLastElementFromActivities();

        // Update bowler runs
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementSixesConceded();
        this.bowler.getMatchPlayerBowling().decrementSixFromRunsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();

        // Update the over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementSixFromRuns();
        this.currentOver.updateOverCompleted();

        // update batsmen score
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().decrementSixFromRunsScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementSixesScored();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();

        // update team score
        this.battingTeam.decrementSixFromRuns();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
    }

    public void handleOverCompleteClick(View view) {
        if(!this.currentOver.isOverCompleted()) {
            Toast.makeText(this,
                    "You cannot end the over without bowling 6 legal deliveries."
                    , Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Over completed. Updating records");
        if(this.bowlingTeam.getCurrentOverBowling() <= this.match.getMaxOvers()) {
            System.out.println("Records are already updated.");
            // update records after over is completed
            this.bowler.getMatchPlayerBowling().incrementNoOfOvers();
            this.bowlingTeam.incrementCurrentOverBowling();
            this.battingTeam.incrementCurrentOverBatting();
            if(this.currentOver.isMaiden()) {
                System.out.println("A Maiden over was bowled.");
                this.bowler.getMatchPlayerBowling().incrementMaidenOverBowled();
                this.bowler.getMatchPlayerBowling().addToMaidenOverBowledTo(
                        this.match.getStrikerBatsman());
                String activity = "Maiden over bowled by " + this.bowler.getPlayerName()
                        + " to " + this.strikerBatsman.getPlayerName();
                this.match.addToActivities(activity);
            }
            this.match.updateMatchCompleted();
        }

        String activity = "Over " + this.battingTeam.getCurrentOverBatting() + " Completed";
        this.match.addToActivities(activity);

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
                Toast.makeText(this, "You need to select a new bowler."
                        , Toast.LENGTH_LONG).show();
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
            this.bowler.getMatchPlayerBowling()
                    .addToOverBowled(this.bowlingTeam.getCurrentOverBowling());

            this.currentOver = this.bowlingTeam.getCurrentOverObject();
            this.currentOver.setPlayerName(this.bowler.getPlayerName());
            System.out.println("New Over Object: " + this.currentOver.toString());

            // add over complete to match activities
            this.match.addToActivities("New over bowled by " + this.bowler.getPlayerName());

            // rotate strike
            this.rotateStrike();
            this.updateScore();
            this.syncMatch();

            System.out.println("New Bowler Selected is: " + bowlerOptions[bowlerSelected[0]]);
        });

        builder.show();
    }

    public void handleRotateStrikeClick(View view) {
        System.out.println("Rotate strike clicked.");
        if(this.checkCompletions()) {
            return;
        }

        // basic
        rotateStrike();
        String activity = "RotateStrike -> " + this.strikerBatsman.getPlayerName()
                + "*, " + this.nonStrikeBatsman.getPlayerName();
        this.match.addToActivities(activity);
        this.updateScore();
        this.syncMatch();
    }

    public boolean undoRotateStrike() {
        System.out.println("Undo Rotate Strike");
        this.match.removeLastElementFromActivities();
        rotateStrike();

        this.updateScore();
        this.syncMatch();

        return true;
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
                (dialog, which) ->
                        runsScoredOnBadDelivery[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsScoredOnBadDelivery[0] == -1) {
                Toast.makeText(this,
                        "You need to select runs scored on wide ball.", Toast.LENGTH_LONG).show();
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
            this.bowler.getMatchPlayerBowling().addToBowlingDetails(activityValue);
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

            String activity = this.battingTeam.getCurrentOverBatting() + "."
                    + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                    + " to " + this.strikerBatsman.getPlayerName() + " " + activityValue;
            this.match.addToActivities(activity);
            if(runsScoredOnBadDelivery[0] % 2 == 1) {
                rotateStrike();
            }

            this.match.updateMatchCompleted();
            this.updateScore();
            this.syncMatch();
        });

        builder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which)
                -> runsScoredOnBadDelivery[0] = -1);

        builder.show();
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
                (dialog, which)
                        -> runsScoredOnBadDelivery[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsScoredOnBadDelivery[0] == -1) {
                Toast.makeText(this,
                        "You need to select runs scored on no ball.", Toast.LENGTH_LONG).show();
                return;
            }
            System.out.println("Runs scored on bad delivery: " + runsScoredOnBadDelivery[0]);

            int runsScoredOnNoBall = runsScoredOnBadDelivery[0] + 1;
            System.out.println("Runs scored on no ball: " + runsScoredOnNoBall);
            String batsmanActivity = Integer.toString(runsScoredOnBadDelivery[0]);

            int[] runsByeSelected = {-1};
            String[] runsByeOptions = {"Legal Runs", "Bye Runs"};

            AlertDialog.Builder byeRunsBuilder = new AlertDialog
                    .Builder(MatchScoreActivity.this);
            byeRunsBuilder.setTitle("Are runs scored bye runs?");
            byeRunsBuilder.setCancelable(false);

            byeRunsBuilder.setSingleChoiceItems(runsByeOptions, runsByeSelected[0],
                    (dialog1, which1) -> runsByeSelected[0] = which1);

            byeRunsBuilder.setNegativeButton("Cancel", (dialog1, which1)
                    -> dialog1.dismiss());

            byeRunsBuilder.setNeutralButton("Clear", (dialog1, which1)
                    -> runsByeSelected[0] = -1);

            byeRunsBuilder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(runsByeSelected[0] == -1) {
                    Toast.makeText(this,
                            "You need to select if the runs scored are bye runs.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String runsByeValue = runsByeOptions[runsByeSelected[0]];
                System.out.println("Runs scored on bad delivery are : " + runsByeValue);

                // update team score
                this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnNoBall);
                this.battingTeam.setExtras(this.battingTeam.getExtras() + runsScoredOnNoBall);
                this.battingTeam.updateRunRate();


                this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
                if(runsByeValue.equals("Legal Runs")) {
                    // no ball runs will go to the batsman
                    this.strikerBatsman.getMatchPlayerBatting()
                            .addToBattingDetails(batsmanActivity);
                    this.strikerBatsman.getMatchPlayerBatting().setRunsScored(
                            this.strikerBatsman.getMatchPlayerBatting().getRunsScored()
                                    + runsScoredOnBadDelivery[0]
                    );
                    if(runsScoredOnBadDelivery[0] == 4) {
                        this.strikerBatsman.getMatchPlayerBatting().incrementFoursScored();
                    } else if (runsScoredOnBadDelivery[0] == 6) {
                        this.strikerBatsman.getMatchPlayerBatting().incrementSixesScored();
                    }
                    this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
                    this.strikerBatsman.getMatchPlayerBatting().updateRecords();
                } else {
                    this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("0");
                    this.currentOver.setByes(this.currentOver.getByes()
                            + runsScoredOnBadDelivery[0]);
                }


                // Update the over
                String activityValue = runsScoredOnBadDelivery[0] + "NB";
                this.currentOver.getOverSummary().add(activityValue);
                this.currentOver.incrementNoBalls();
                this.currentOver.setExtras(this.currentOver.getExtras() + runsScoredOnNoBall);
                this.currentOver.setRuns(this.currentOver.getRuns() + runsScoredOnNoBall);

                // update bowler runs
                this.bowler.getMatchPlayerBowling().addToBowlingDetails(activityValue);
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

                String activity = this.battingTeam.getCurrentOverBatting() + "."
                        + this.currentOver.getLegalDeliveries() + " -> "
                        + this.bowler.getPlayerName() + " to "
                        + this.strikerBatsman.getPlayerName() + " " + activityValue;
                this.match.addToActivities(activity);
                if(runsScoredOnBadDelivery[0] % 2 == 1) {
                    rotateStrike();
                }

                this.match.updateMatchCompleted();
                this.updateScore();
                this.syncMatch();
            });

            byeRunsBuilder.show();

            if(runsByeSelected[0] == -1) {
                Toast.makeText(this,
                        "You need to select if the runs scored are bye runs.",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which)
                -> runsScoredOnBadDelivery[0] = -1);

        builder.show();
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
                (dialog, which)
                        -> runsGivenOnBye[0] = Integer.parseInt(runsOptions[which]));

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(runsGivenOnBye[0] == -1) {
                Toast.makeText(this, "You need to select runs given on bye.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            int runsScoredOnBye = runsGivenOnBye[0];
            System.out.println("Runs given on bye: " + runsScoredOnBye);

            // update team score
            this.battingTeam.setRuns(this.battingTeam.getRuns() + runsScoredOnBye);
            this.battingTeam.setExtras(this.battingTeam.getRuns() + runsScoredOnBye);
            this.battingTeam.incrementLegalDeliveriesPlayed();
            this.battingTeam.updateRunRate();

            // bye runs will not go to the batsman, but he will play the balls
            this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("0");
            this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
            this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
            this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
            this.strikerBatsman.getMatchPlayerBatting().updateRecords();

            // Update the over
            String activityValue = runsScoredOnBye + "B";
            this.currentOver.addToOverSummary(activityValue);
            this.currentOver.incrementLegalDeliveries();
            this.currentOver.setExtras(this.currentOver.getExtras() + runsScoredOnBye);
            this.currentOver.setRuns(this.currentOver.getRuns() + runsScoredOnBye);
            this.currentOver.updateOverCompleted();

            // update bowler runs
            this.bowler.getMatchPlayerBowling().addToBowlingDetails(activityValue);
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


            String activity = this.battingTeam.getCurrentOverBatting() + "."
                    + this.currentOver.getLegalDeliveries() + " -> "
                    + this.bowler.getPlayerName() + " to " + this.strikerBatsman.getPlayerName()
                    + " " + activityValue;
            this.match.addToActivities(activity);
            if(runsScoredOnBye % 2 == 1) {
                rotateStrike();
            }
            this.match.updateMatchCompleted();
            this.updateScore();
            this.syncMatch();
        });

        builder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which)
                -> runsGivenOnBye[0] = -1);

        builder.show();
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
        this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(true);
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out-Bowled");
        this.strikerBatsman.getMatchPlayerBatting().setBowledBy(this.bowler.getPlayerName());
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());

        // update over
        this.currentOver.addToOverSummary("Out");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementWickets();
        this.currentOver.updateOverCompleted();

        // update bowling record
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("Out-Bowled");
        this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling().addToBowledPlayers(this.match.getStrikerBatsman());
        this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

        // update match activities
        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " -Out-Bowled";
        this.match.addToActivities(activity);
        this.match.addToActivities(this.strikerBatsman.getPlayerName() + "-Out-Bowled");

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
                Toast.makeText(this, "You need to select the new batsman.",
                        Toast.LENGTH_LONG).show();
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

        builder.show();
    }

    public boolean undoBowled(String lastPlayerName) {
        MatchPlayer player = this.battingTeam.getMatchPlayerFromName(lastPlayerName);

        // remove new batsman in activity
        this.match.removeLastElementFromActivities();

        // not batted anymore
        this.strikerBatsman.getMatchPlayerBatting().setBatted(false);
        // set striker batsman
        this.match.setStrikerBatsman(lastPlayerName);
        this.match.setStrikerBatsmanIndex(
                this.battingTeam.getMatchPlayerIndex(lastPlayerName)
        );
        // update striker batsman to last batsman
        this.strikerBatsman = player;

        // remove last batsman out activity of batsman-out-bowled
        this.match.removeLastElementFromActivities();
        this.match.removeLastElementFromActivities(); // remove bowler to batsman activity

        // update bowling record
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().decrementDotsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling().removeFromWicketsTakenPlayers();
        this.bowler.getMatchPlayerBowling().removeFromBowledPlayers();

        // update over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementDotBalls();
        this.currentOver.decrementWickets();
        this.currentOver.updateOverCompleted();

        // update batsman score
        this.strikerBatsman.getMatchPlayerBatting().decrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(false);
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().setBowledBy("");
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy("");

        // update team score
        this.battingTeam.decrementWickets();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();
        this.battingTeam.removeFromFallOfWickets();

        this.match.updateMatchCompleted();
        this.updateScore();
        this.syncMatch();

        return true;
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
                Toast.makeText(this, "You need to select who caught the ball.",
                        Toast.LENGTH_LONG).show();
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
            this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
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
            this.bowler.getMatchPlayerBowling().addToBowlingDetails("Out-Caught");
            this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
            this.bowler.getMatchPlayerBowling().incrementDotsConceded();
            this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
            this.bowler.getMatchPlayerBowling().updateRecords();
            this.bowler.getMatchPlayerBowling().addToWicketsTakenPlayers(this.match.getStrikerBatsman());

            // update fielding record
            this.match.getMatchPlayerObject(caughtBy).getMatchPlayerFielding().incrementNoOfCatches();
            this.match.getMatchPlayerObject(caughtBy).getMatchPlayerFielding()
                    .addToCaughtPlayers(this.match.getStrikerBatsman());


            String activity = this.battingTeam.getCurrentOverBatting() + "."
                    + this.currentOver.getLegalDeliveries() + " -> "
                    + this.bowler.getPlayerName() + " to " + this.strikerBatsman.getPlayerName()
                    + " -Out-Caught";
            this.match.addToActivities(activity);
            this.match.addToActivities("Catch by " + caughtBy);

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
                    Toast.makeText(this, "You need to select the new batsman."
                            , Toast.LENGTH_LONG).show();
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

            builder.setNeutralButton("Clear", (dialog1, which1)
                    -> optionSelected[0] = -1);

            builder.show();

            if(optionSelected[0] == -1) {
                System.out.println("This condition should never arrive.");
                Toast.makeText(this,
                        "You need to select a new batsman after the wicket.",
                        Toast.LENGTH_LONG).show();
            }
        });

        caughtBybuilder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        caughtBybuilder.setNeutralButton("Clear", (dialog, which)
                -> caughtBySelected[0] = -1);

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
        this.strikerBatsman.getMatchPlayerBatting().incrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().incrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(true);
        this.strikerBatsman.getMatchPlayerBatting().addToBattingDetails("Out-Hit-Wicket");
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy(this.bowler.getPlayerName());

        // update over
        this.currentOver.addToOverSummary("Out");
        this.currentOver.incrementLegalDeliveries();
        this.currentOver.incrementDotBalls();
        this.currentOver.incrementWickets();
        this.currentOver.updateOverCompleted();

        // update bowling record
        this.bowler.getMatchPlayerBowling().addToBowlingDetails("Out-Hit-Wicket");
        this.bowler.getMatchPlayerBowling().incrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().incrementDotsConceded();
        this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().incrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling()
                .addToWicketsTakenPlayers(this.match.getStrikerBatsman());

        // update match activities
        String activity = this.battingTeam.getCurrentOverBatting() + "."
                + this.currentOver.getLegalDeliveries() + " -> " + this.bowler.getPlayerName()
                + " to " + this.strikerBatsman.getPlayerName() + " -Out-Hit-Wicket";
        this.match.addToActivities(activity);
        this.match.addToActivities(this.strikerBatsman.getPlayerName() + "-Out-Hit-Wicket");

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
                Toast.makeText(this, "You need to select the new batsman.",
                        Toast.LENGTH_LONG).show();
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

        builder.show();
    }

    public boolean undoHitWicket(String lastBatsman) {
        // remove new batsman in
        this.match.removeLastElementFromActivities();

        // set not batted
        this.strikerBatsman.getMatchPlayerBatting().setBatted(false);

        // remove reference
        this.match.setStrikerBatsman(lastBatsman);
        this.match.setStrikerBatsmanIndex(
                this.battingTeam.getMatchPlayerIndex(lastBatsman)
        );
        this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(lastBatsman);

        // remove out reference
        this.match.removeLastElementFromActivities();
        this.match.removeLastElementFromActivities(); // remove bowler to batsman activity

        // update bowling record
        this.bowler.getMatchPlayerBowling().removeLastElementOfBowlingDetails();
        this.bowler.getMatchPlayerBowling().decrementWicketsTaken();
        this.bowler.getMatchPlayerBowling().decrementDotsConceded();
        this.bowler.getMatchPlayerBowling().decrementDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().decrementLegalDeliveriesBowled();
        this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
        this.bowler.getMatchPlayerBowling().updateRecords();
        this.bowler.getMatchPlayerBowling().removeFromWicketsTakenPlayers();

        // update over
        this.currentOver.removeLastElementFromOverSummary();
        this.currentOver.decrementLegalDeliveries();
        this.currentOver.decrementDotBalls();
        this.currentOver.decrementWickets();
        this.currentOver.updateOverCompleted();

        // update batsman score
        this.strikerBatsman.getMatchPlayerBatting().decrementDotsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().decrementBallsPlayed();
        this.strikerBatsman.getMatchPlayerBatting().updateStrikeRate();
        this.strikerBatsman.getMatchPlayerBatting().updateRecords();
        this.strikerBatsman.getMatchPlayerBatting().setOut(false);
        this.strikerBatsman.getMatchPlayerBatting().removeLastFromBattingDetails();
        this.strikerBatsman.getMatchPlayerBatting().setWicketBy("");

        // update team score
        this.battingTeam.decrementWickets();
        this.battingTeam.decrementLegalDeliveriesPlayed();
        this.battingTeam.updateRunRate();
        this.battingTeam.removeFromFallOfWickets();

        this.updateScore();
        this.syncMatch();

        return true;
    }

    public void handleRunOutClick(View view) {
        if(this.checkCompletions()) {
            return;
        }

        int[] runOutBySelected = {-1};
        String[] allMatchPlayers = this.getFieldingPlayers();

        AlertDialog.Builder runOutBybuilder = new AlertDialog
                .Builder(MatchScoreActivity.this);
        runOutBybuilder.setTitle("Run out by?");
        runOutBybuilder.setCancelable(false);

        runOutBybuilder.setSingleChoiceItems(allMatchPlayers, runOutBySelected[0],
                (dialog, which) -> runOutBySelected[0] = which);

        runOutBybuilder.setPositiveButton("Ok", (dialog, which) -> {
            if(runOutBySelected[0] == -1) {
                Toast.makeText(this,
                        "You need to select the player who was responsible for runout.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String runOutByPlayer = allMatchPlayers[runOutBySelected[0]];
            System.out.println("The player responsible for runout is: " + runOutByPlayer);

            int[] runsScoredSelected = {-1};
            AlertDialog.Builder runsScoredbuilder = new AlertDialog
                    .Builder(MatchScoreActivity.this);
            runsScoredbuilder.setTitle("Runs scored on ball?");
            runsScoredbuilder.setCancelable(false);

            runsScoredbuilder.setSingleChoiceItems(runsOptions, runsScoredSelected[0],
                    (dialog1, which1) -> runsScoredSelected[0] = which1);

            runsScoredbuilder.setNegativeButton("Cancel", (dialog1, which1)
                    -> dialog1.dismiss());

            runsScoredbuilder.setNeutralButton("Clear", (dialog1, which1)
                    -> runsScoredSelected[0] = -1);

            runsScoredbuilder.setPositiveButton("Ok", (dialog1, which1) -> {
                if(runsScoredSelected[0] == -1) {
                    Toast.makeText(this,
                            "You need to select the runs scored on run out.",
                            Toast.LENGTH_LONG).show();
                }

                int runsScoredOnBall = Integer.parseInt(runsOptions[runsScoredSelected[0]]);
                System.out.println("The runs scored on run out are: " + runsScoredOnBall);

                int[] runsByeSelected = {-1};
                String[] runsByeOptions = {"Legal Runs", "Bye Runs"};

                AlertDialog.Builder runsByeBuilder = new AlertDialog
                        .Builder(MatchScoreActivity.this);
                runsByeBuilder.setTitle("Are the runs scored extra runs?");
                runsByeBuilder.setCancelable(false);

                runsByeBuilder.setSingleChoiceItems(runsByeOptions, runsByeSelected[0],
                        (dialog5, which5) -> runsByeSelected[0] = which5);

                runsByeBuilder.setNegativeButton("Cancel", (dialog5, which5)
                        -> dialog5.dismiss());

                runsByeBuilder.setNeutralButton("Clear", (dialog5, which5)
                        -> runsByeSelected[0] = -1);

                runsByeBuilder.setPositiveButton("Ok", (dialog5, which5) -> {
                    if(runsByeSelected[0] == -1) {
                        Toast.makeText(this,
                                "You need to select if the runs scored are bye runs.",
                                Toast.LENGTH_LONG).show();
                    }

                    String runsScoredBye = runsByeOptions[runsByeSelected[0]];
                    System.out.println("The runs scored are: " + runsScoredBye);

                    int[] ballTypeSelected = {-1};
                    String[] ballTypeOptions = {"Legal Delivery", "Wide Ball", "No Ball"};

                    AlertDialog.Builder ballTypeBuilder = new AlertDialog
                            .Builder(MatchScoreActivity.this);
                    ballTypeBuilder.setTitle("Is it a legal delivery?");
                    ballTypeBuilder.setCancelable(false);

                    ballTypeBuilder.setSingleChoiceItems(ballTypeOptions, ballTypeSelected[0],
                            (dialog2, which2) -> ballTypeSelected[0] = which2);

                    ballTypeBuilder.setNegativeButton("Cancel",
                            (dialog2, which2) -> dialog2.dismiss());

                    ballTypeBuilder.setNeutralButton("Clear",
                            (dialog2, which2) -> ballTypeSelected[0] = -1);

                    ballTypeBuilder.setPositiveButton("Ok",
                            (dialog2, which2) -> {
                                if(ballTypeSelected[0] == -1) {
                                    Toast.makeText(this,
                                            "You need to select the ball type bowled.",
                                        Toast.LENGTH_LONG).show();
                        }

                        String ballType = ballTypeOptions[ballTypeSelected[0]];
                        System.out.println("Selected ball type is: " + ballType);

                        // Select run out batsman
                        int[] runOutBatsmanSelected = {-1};
                        String[] runOutBatsmanOptions = {this.strikerBatsman.getPlayerName(),
                                this.nonStrikeBatsman.getPlayerName()};

                        AlertDialog.Builder runOutBatsmanBuilder = new AlertDialog
                                .Builder(MatchScoreActivity.this);
                        runOutBatsmanBuilder.setTitle("Select the batsman who is runout?");
                        runOutBatsmanBuilder.setCancelable(false);

                        runOutBatsmanBuilder.setSingleChoiceItems(runOutBatsmanOptions,
                                runOutBatsmanSelected[0],
                                (dialog3, which3)
                                        -> runOutBatsmanSelected[0] = which3);

                        runOutBatsmanBuilder.setNegativeButton("Cancel",
                                (dialog3, which3) -> dialog3.dismiss());

                        runOutBatsmanBuilder.setNeutralButton("Clear",
                                (dialog3, which3)
                                        -> runOutBatsmanSelected[0] = -1);

                        runOutBatsmanBuilder.setPositiveButton("Ok",
                                (dialog3, which3) -> {
                                    if(runOutBatsmanSelected[0] == -1) {
                                        Toast.makeText(this,
                                                "You need to select the batsman who is run out.",
                                            Toast.LENGTH_LONG).show();
                            }

                            // Point of no return

                            String runOutBatsman = runOutBatsmanOptions[runOutBatsmanSelected[0]];
                            MatchPlayer runOutPlayerObject = this.battingTeam
                                    .getMatchPlayerFromName(runOutBatsman);
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

                            // anyone of them depending on the conditions is zero
                            int totalRunsOnRunout = ballExtraRun + batExtraRuns + batLegalRuns;

                            // update team score
                            this.battingTeam.incrementWickets();
                            if(ballExtraRun == 0) {
                                System.out.println("This is a legal delivery on run out.");
                                // legal delivery
                                this.battingTeam.incrementLegalDeliveriesPlayed();
                                this.currentOver.incrementLegalDeliveries();
                                this.bowler.getMatchPlayerBowling()
                                        .incrementLegalDeliveriesBowled();
                                // a legal delivery
                                runOutPlayerObject.getMatchPlayerBatting().incrementBallsPlayed();
                                runOutPlayerObject.getMatchPlayerBatting().setRunsScored(
                                        runOutPlayerObject.getMatchPlayerBatting()
                                                .getRunsScored() + batLegalRuns
                                );
                                runOutPlayerObject.getMatchPlayerBatting().updateStrikeRate();
                                runOutPlayerObject.getMatchPlayerBatting().updateRecords();
                                this.currentOver.getOverSummary()
                                        .add(totalRunsOnRunout + "-Runout");
                                this.match.addToActivities(
                                        this.battingTeam.getCurrentOverBatting() + "."
                                                + this.currentOver.getLegalDeliveries() + " -> "
                                                + this.bowler.getPlayerName() + " to "
                                                + this.strikerBatsman.getPlayerName() + " "
                                                + totalRunsOnRunout + "-Runout");
                            }

                            this.battingTeam.setRuns(this.battingTeam.getRuns()
                                    + totalRunsOnRunout);
                            this.battingTeam.updateRunRate();
                            this.battingTeam.updateFallOfWickets();

                            // UPDATE BATSMAN RECORD
                            runOutPlayerObject.getMatchPlayerBatting().setOut(true);
                            runOutPlayerObject.getMatchPlayerBatting()
                                    .addToBattingDetails(batLegalRuns + "-Out-Runout");
                            runOutPlayerObject.getMatchPlayerBatting()
                                    .setWicketBy(this.bowler.getPlayerName());
                            runOutPlayerObject.getMatchPlayerBatting().setRunOutBy(runOutByPlayer);

                            // update over
                            this.currentOver.incrementWickets();
                            this.currentOver.updateOverCompleted();

                            // update bowler records
                            if(totalRunsOnRunout == 0) {
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
                                                totalRunsOnRunout
                                );
                                this.currentOver.incrementWides();
                                this.currentOver.setExtras(
                                        this.currentOver.getExtras() + totalRunsOnRunout
                                );

                                this.battingTeam.setExtras(this.battingTeam.getExtras() +
                                        ballExtraRun + batExtraRuns + batLegalRuns);
                                this.currentOver.getOverSummary().add((totalRunsOnRunout - 1)
                                        + "-WD-Runout");

                                this.match.addToActivities(
                                        this.battingTeam.getCurrentOverBatting() + "."
                                                + this.currentOver.getLegalDeliveries() + " -> "
                                                + this.bowler.getPlayerName() + " to "
                                                + this.strikerBatsman.getPlayerName() + " "
                                                + (totalRunsOnRunout - 1) + "-WD-Runout");
                            }

                            // update extras
                            if(ballType.equals("No Ball")) {
                                System.out.println("No ball on run out.");
                                this.bowler.getMatchPlayerBowling().incrementNoBalls();
                                this.bowler.getMatchPlayerBowling().setExtrasConceded(
                                        this.bowler.getMatchPlayerBowling().getExtrasConceded() +
                                                totalRunsOnRunout
                                );
                                this.currentOver.incrementNoBalls();
                                this.currentOver.setExtras(
                                        this.currentOver.getExtras() + totalRunsOnRunout
                                );

                                this.battingTeam.setExtras(this.battingTeam.getExtras() +
                                        ballExtraRun + batExtraRuns + batLegalRuns);
                                this.currentOver.getOverSummary().add((totalRunsOnRunout - 1)
                                        + "-NB-Runout");
                                this.match.addToActivities((totalRunsOnRunout - 1)
                                        + "-NB-Runout");
                                this.match.addToActivities(
                                        this.battingTeam.getCurrentOverBatting() + "."
                                                + this.currentOver.getLegalDeliveries() + " -> "
                                                + this.bowler.getPlayerName() + " to "
                                                + this.strikerBatsman.getPlayerName() + " "
                                                + (totalRunsOnRunout - 1) + "-NB-Runout");
                            }

                            // runs conceded
                            this.currentOver.setRuns(
                                    this.currentOver.getRuns() + totalRunsOnRunout
                            );
                            this.bowler.getMatchPlayerBowling().setRunsConceded(
                                    this.bowler.getMatchPlayerBowling().getRunsConceded()
                                            + totalRunsOnRunout
                            );
                            this.bowler.getMatchPlayerBowling().incrementDeliveriesBowled();
                            this.bowler.getMatchPlayerBowling().updateBowlingEconomy();
                            this.bowler.getMatchPlayerBowling().updateRecords();

                            this.match.addToActivities(runOutBatsman + "-Runout");

                            this.match.updateMatchCompleted();
                            this.syncMatch();
                            this.updateScore();

                            // Check if match is already completed.
                            if(this.battingTeam.isBattingInningsCompleted()) {
                                this.updateScore();
                                this.syncMatch();
                                Toast.makeText(this, this.match.getInnings() +
                                                " Innings is completed. Please start a new innings.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            if(this.match.isCompleted()) {
                                this.updateScore();
                                this.syncMatch();
                                Toast.makeText(this,
                                        "Match is completed. Please end the innings.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Get list of not out Batsman
                            String[] notOutBatsman = this.getNotOutBatsMan();
                            int[] optionSelected = {-1};

                            AlertDialog.Builder builder = new AlertDialog
                                    .Builder(MatchScoreActivity.this);
                            builder.setTitle("Select new Batsman?");
                            builder.setCancelable(false);

                            builder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                                    (dialog6, which6)
                                            -> optionSelected[0] = which6);

                            builder.setPositiveButton("Ok", (dialog6, which6)
                                    -> {
                                    if(optionSelected[0] == -1) {
                                        Toast.makeText(this,
                                                "You need to select the new batsman.",
                                                Toast.LENGTH_LONG).show();
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
                                    this.strikerBatsman = this.battingTeam
                                            .getMatchPlayerFromName(newBatsman);
                                    this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
                                    this.match.addToActivities(newBatsman + "-In");
                                } else {
                                    // update non striker batsman
                                    this.match.setNonStrikeBatsman(newBatsman);
                                    this.match.setNonStrikerBatsmanIndex(
                                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                                    );
                                    this.nonStrikeBatsman = this.battingTeam
                                            .getMatchPlayerFromName(newBatsman);
                                    this.nonStrikeBatsman.getMatchPlayerBatting().setBatted(true);
                                    this.match.addToActivities(newBatsman + "-In");
                                }

                                this.updateScore();
                                this.syncMatch();
                            });

                            builder.show();
                        });

                        runOutBatsmanBuilder.show();

                        if(runOutBatsmanSelected[0] == -1) {
                            Toast.makeText(this,
                                    "You need to select the batsman which is run out.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    ballTypeBuilder.show();
                    if(ballTypeSelected[0] == -1) {
                        Toast.makeText(this,
                                "You need to select the ball type on run out.",
                                Toast.LENGTH_LONG).show();
                    }

                });

                runsByeBuilder.show();

                if(runsByeSelected[0] == -1) {
                    Toast.makeText(this,
                            "You need to select if the runs scored are bye runs.",
                            Toast.LENGTH_LONG).show();
                }
            });

            runsScoredbuilder.show();

            if(runsScoredSelected[0] == -1) {
                Toast.makeText(this, "You need to select the runs scored on run out.",
                        Toast.LENGTH_LONG).show();
            }
        });

        runOutBybuilder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        runOutBybuilder.setNeutralButton("Clear", (dialog, which)
                -> runOutBySelected[0] = -1);

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

        if(this.getNotOutBatsMan().length == 0) {
            Toast.makeText(this,
                    "This is last wicket. You cannot retire player now.",
                    Toast.LENGTH_LONG).show();
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

        builder.setNeutralButton("Clear", (dialog, which)
                -> retireBatsmanSelected[0] = -1);

        builder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(retireBatsmanSelected[0] == -1) {
                Toast.makeText(this,
                        "You need to select the batsman who is going to retire.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String batsmanToRetire = retireBatsmanOptions[retireBatsmanSelected[0]];
            System.out.println("Batsman to retire is: " + batsmanToRetire);

            // Get list of not out Batsman
            String[] notOutBatsman = this.getNotOutBatsMan();
            int[] optionSelected = {-1};
            AlertDialog.Builder newBatsmanBuilder = new AlertDialog
                    .Builder(MatchScoreActivity.this);
            newBatsmanBuilder.setTitle("Select new Batsman?");
            newBatsmanBuilder.setCancelable(false);

            newBatsmanBuilder.setSingleChoiceItems(notOutBatsman, optionSelected[0],
                    (dialog1, which1) -> optionSelected[0] = which1);

            newBatsmanBuilder.setPositiveButton("Ok", (dialog1, which1)
                    -> {
                    if(optionSelected[0] == -1) {
                        Toast.makeText(this,
                                "You need to select the new batsman.",
                                Toast.LENGTH_LONG).show();
                        return;
                }
                String newBatsman = notOutBatsman[optionSelected[0]];
                System.out.println("New batsman is: " + newBatsman);

                this.match.addToActivities(batsmanToRetire + " -Retired");

                if(batsmanToRetire.equals(this.match.getStrikerBatsman())) {
                    // update the striker batsman
                    this.match.setStrikerBatsman(newBatsman);
                    this.match.setStrikerBatsmanIndex(
                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                    );
                    this.strikerBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                    this.strikerBatsman.getMatchPlayerBatting().setBatted(true);
                    this.match.addToActivities(newBatsman + " -In-On-Strike");
                } else {
                    this.match.setNonStrikeBatsman(newBatsman);
                    this.match.setNonStrikerBatsmanIndex(
                            this.battingTeam.getMatchPlayerIndex(newBatsman)
                    );
                    this.nonStrikeBatsman = this.battingTeam.getMatchPlayerFromName(newBatsman);
                    this.nonStrikeBatsman.getMatchPlayerBatting().setBatted(true);
                    this.match.addToActivities(newBatsman + " -In-On-Non-Strike");
                }

                this.updateScore();
                this.syncMatch();
            });

            newBatsmanBuilder.setNeutralButton("Clear", (dialog1, which1)
                    -> optionSelected[0] = -1);

            newBatsmanBuilder.setNegativeButton("Cancel", (dialog1, which1)
                    -> dialog1.dismiss());

            newBatsmanBuilder.show();

            if(optionSelected[0] == -1) {
                Toast.makeText(this,
                        "You need to select the new batsman after a wicket.",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public void handleEditOversClick(View view) {
        System.out.println("Edit overs clicked.");

        if(this.checkInningsCompletion()) {
            return;
        }

        String[] overIncreaseOptions = {"0", "1", "2", "3", "-1", "-2", "-3"};
        int[] overIncreaseSelected = {0};

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select the number of overs to increase or decrease?");
        builder.setCancelable(false);

        builder.setSingleChoiceItems(overIncreaseOptions, overIncreaseSelected[0],
                (dialog, which) -> overIncreaseSelected[0] = which);

        builder.setNeutralButton("Clear", (dialog, which)
                -> overIncreaseSelected[0] = -1);

        builder.setNegativeButton("Cancel", (dialog, which)
                -> dialog.dismiss());

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(overIncreaseSelected[0] == -1) {
                Toast.makeText(this,
                        "You need to select the number of overs to increase.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            int oversToIncrease = Integer.parseInt(overIncreaseOptions[overIncreaseSelected[0]]);
            System.out.println("Number of overs to increase: " + oversToIncrease);

            if(oversToIncrease < 0) {
                int currentOverPlayed = this.battingTeam.getCurrentOverBatting();
                int maxOvers = this.match.getMaxOvers();
                if(maxOvers + oversToIncrease <= currentOverPlayed) {
                    Toast.makeText(this, "Invalid selection of overs."
                            + currentOverPlayed + " overs are already completed.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                this.match.setMaxOvers(
                        this.match.getMaxOvers() + oversToIncrease
                );
                this.match.getTeamA().decrementMaxOvers(oversToIncrease);
                this.match.getTeamB().decrementMaxOvers(oversToIncrease);
                this.match.addToActivities("Over-Decrease- " + oversToIncrease);
            } else if(oversToIncrease > 0) {
                // Set match properties
                this.match.setMaxOvers(
                        this.match.getMaxOvers() + oversToIncrease
                );
                this.match.getTeamA().incrementMaxOvers(oversToIncrease);
                this.match.getTeamB().incrementMaxOvers(oversToIncrease);
                this.match.addToActivities("Over-Increase- " + oversToIncrease);
            }

            this.syncMatch();
            this.updateScore();

            // edit bowler if no bowls are bowled in this over.
            if(this.currentOver.getLegalDeliveries() == 0) {
                AlertDialog.Builder newBowlerBuilder = new AlertDialog
                        .Builder(MatchScoreActivity.this);
                newBowlerBuilder.setTitle("Update bowler bowling the current over?");
                newBowlerBuilder.setCancelable(false);

                String[] options = this.bowlingTeam.getPlayerNames().toArray(new String[0]);
                Arrays.sort(options);
                int[] newBowlerSelected = {-1};
                for(int i=0; i<options.length; i++) {
                    String player = options[i];
                    if(this.currentOver.getPlayerName().equals(player)) {
                        newBowlerSelected[0] = i;
                        break;
                    }
                }

                newBowlerBuilder.setSingleChoiceItems(options, newBowlerSelected[0],
                        (dialog1, which1) -> newBowlerSelected[0] = which1);

                newBowlerBuilder.setPositiveButton("Ok", (dialog1, which1)
                        -> {
                        if(newBowlerSelected[0] == -1) {
                            Toast.makeText(this,
                                    "You need to select the new bowler to bowl the over.",
                                    Toast.LENGTH_LONG).show();
                            return;
                    }

                    String newBowler = options[newBowlerSelected[0]];
                    System.out.println("The new bowler selected is: " + newBowler);

                    if(newBowler.equals(this.bowler.getPlayerName())) {
                        System.out.println("Same bowler. Returning.");
                        return;
                    }

                    // update previous bowler
                    if(this.bowler.getMatchPlayerBowling().getDeliveriesBowled() == 0) {
                        this.bowler.getMatchPlayerBowling().setBowled(false);
                    }
                    this.bowler.getMatchPlayerBowling().removeFromOversBowled();
                    String activity = this.bowler.getPlayerName() + "-Replaced";
                    this.match.addToActivities(activity);

                    // update new bowler
                    this.match.setCurrentBowler(newBowler);
                    int newBowlerIndex = this.bowlingTeam.getMatchPlayerIndex(newBowler);
                    this.match.setCurrentBowlerIndex(newBowlerIndex);
                    this.bowler = this.bowlingTeam.getTeamPlayers().get(newBowlerIndex);
                    this.bowler.getMatchPlayerBowling().setBowled(true);
                    this.bowler.getMatchPlayerBowling().addToOverBowled(this.bowlingTeam
                            .getCurrentOverBowling());

                    this.currentOver.setPlayerName(this.bowler.getPlayerName());
                    activity = this.bowler.getPlayerName() + "-New-Bowler";
                    this.match.addToActivities(activity);

                    this.syncMatch();
                    this.updateScore();
                });

                newBowlerBuilder.show();
            }
        });

        builder.show();

        if(overIncreaseSelected[0] == -1) {
            Toast.makeText(this, "You need to select the number of overs to increase.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void handleInningsEndClick(View view) {
        System.out.println("Innings end clicked.");

        // Innings complete
        if(this.battingTeam.isBattingInningsCompleted()) {
            if(this.match.getInnings() == 1) {
                System.out.println("First Innings Completed. Start the second innings now.");

                // Open a new dialogue box here to ask to start second innings.
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(MatchScoreActivity.this);
                builder.setTitle("Start second innings?");
                builder.setCancelable(false);

                String[] options = {"Yes", "No"};
                final int[] optionSelected = {-1};

                builder.setSingleChoiceItems(options, optionSelected[0],
                        (dialog, which) -> optionSelected[0] = which);

                builder.setPositiveButton("Ok", (dialog, which) -> {
                    if(optionSelected[0] == -1) {
                        Toast.makeText(this,
                                "Select Yes to start a the new innings.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer = options[optionSelected[0]];
                    System.out.println("Option selected is: " + answer);

                    if(answer.equals("Yes")) {
                        this.match.addToActivities("1st Innings End.");

                        // update innings
                        this.match.setInnings(2);
                        this.match.addToActivities("2nd Innings Started.");
                        // update bowling and batting teams names
                        this.match.setBattingAndBowlingTeamNames();
                        this.syncMatch();

                        System.out.println("Go to the screen to select the new openors "
                                + "for second innings.");
                        System.out.println("Opening Select Openers Activity");
                        Intent intent = new Intent(this, SelectOpenersActivity.class);
                        intent.putExtra("data_files_hashmap", this.dataFilesMap);
                        intent.putExtra("match_object", this.match);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which)
                        -> dialog.dismiss());

                builder.setNeutralButton("Clear", (dialog, which)
                        -> optionSelected[0] = -1);

                builder.show();
            }

            if(this.match.getInnings() == 2) {
                System.out.println("Second Innings Completed. Match is finished.");
                // Open a new dialogue box here to ask to end the match.
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(MatchScoreActivity.this);
                builder.setTitle("End the match?");
                builder.setCancelable(false);

                String[] options = {"Yes", "No"};
                final int[] optionSelected = {-1};

                builder.setSingleChoiceItems(options, optionSelected[0],
                        (dialog, which) -> optionSelected[0] = which);

                builder.setPositiveButton("Ok", (dialog, which) -> {
                    if(optionSelected[0] == -1) {
                        Toast.makeText(this, "Select Yes to end the match.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer = options[optionSelected[0]];
                    System.out.println("Option selected is: " + answer);

                    if(answer.equals("Yes")) {
                        this.match.addToActivities("Match Completed.");

                        this.match.setCompleted(true);
                        this.match.updateResult();
                        this.syncMatch();

                        Utils.updateGlobalRecords(dataFilesMap, this.match);
                        this.match.addToActivities("Records Updated.");

                        System.out.println("Go to match information.");
                        Intent intent = new Intent(this,
                                MatchInformationActivity.class);
                        intent.putExtra("data_files_hashmap", this.dataFilesMap);
                        intent.putExtra("match_object", match);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which)
                        -> dialog.dismiss());

                builder.setNeutralButton("Clear", (dialog, which)
                        -> optionSelected[0] = -1);

                builder.show();
            }
        } else if(this.match.isCompleted()) {
            if (this.match.getInnings() == 2) {
                System.out.println("Second Innings Completed. Match is finished.");
                // Open a new dialogue box here to ask to end the match.
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(MatchScoreActivity.this);
                builder.setTitle("End the match?");
                builder.setCancelable(false);

                String[] options = {"Yes", "No"};
                final int[] optionSelected = {-1};

                builder.setSingleChoiceItems(options, optionSelected[0],
                        (dialog, which) -> optionSelected[0] = which);

                builder.setPositiveButton("Ok", (dialog, which) -> {
                    if (optionSelected[0] == -1) {
                        Toast.makeText(this, "Select Yes to end the match.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String answer = options[optionSelected[0]];
                    System.out.println("Option selected is: " + answer);

                    if (answer.equals("Yes")) {
                        this.match.setCompleted(true);
                        this.match.updateResult();
                        this.syncMatch();

                        Utils.updateGlobalRecords(dataFilesMap, this.match);

                        System.out.println("Go to match information.");
                        Intent intent = new Intent(this,
                                MatchInformationActivity.class);
                        intent.putExtra("data_files_hashmap", this.dataFilesMap);
                        intent.putExtra("match_object", match);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which)
                        -> dialog.dismiss());

                builder.setNeutralButton("Clear", (dialog, which)
                        -> optionSelected[0] = -1);

                builder.show();
            }
        } else {
            Toast.makeText(this, "You cannot end the innings yet.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void handleUndoClick(View view) {
        // we will support 0, 1, 2, 3, 4, 5, 6, bowled and hit wicket.
        // all other decisions cannot be clicked by mistake

        boolean undoDone = false;

        int size = this.match.getActivities().size();
        // get last activity
        if(size == 0) {
            Toast.makeText(this, "Nothing to undo.", Toast.LENGTH_LONG).show();
            return;
        }

        String activity = this.match.getActivities().get(size - 1);

        if(activity.contains(" - 0 Run")) {
            System.out.println("Undo 0");
            undoDone = undoZeroRuns();
        }

        if(activity.contains(" - 1 Run")) {
            System.out.println("Undo 1");
            undoDone = undoOneRun();
        }

        if(activity.contains(" - 2 Runs")) {
            System.out.println("Undo 2");
            undoDone = undoTwoRuns();
        }

        if(activity.contains(" - 3 Runs")) {
            System.out.println("Undo 3");
            undoDone = undoThreeRuns();
        }

        if(activity.contains(" - 4 Runs")) {
            System.out.println("Undo 4");
            undoDone = undoFourRuns();
        }

        if(activity.contains(" - 5 Runs")) {
            System.out.println("Undo 5");
            undoDone = undoFiveRuns();
        }

        if(activity.contains(" - 6 Runs")) {
            System.out.println("Undo 6");
            undoDone = undoSixruns();
        }

        if(activity.contains("RotateStrike -> ")) {
            System.out.println("Undo Rotate Strike");
            undoDone = undoRotateStrike();
        }

        // last wicket out bowled
        if(activity.contains("-Out-Bowled")) {
            System.out.println("Undo bowled");
            String playerName = activity.replace("-Out-Bowled", "");
            undoDone = undoBowled(playerName);
        }

        // last wicket hit wicket
        if(activity.contains("-Out-Hit-Wicket")) {
            System.out.println("Undo hit wicket");
            String playerName = activity.replace("-Out-Hit-Wicket", "");
            undoDone = undoHitWicket(playerName);
        }

        // new batsman activity was added.
        if(activity.contains("-In")) {
            String previousActivity = this.match.getActivities().get(size - 2);

            if(previousActivity.contains("-Out-Bowled")) {
                System.out.println("Undo bowled");
                String playerName = previousActivity.replace("-Out-Bowled", "");
                undoDone = undoBowled(playerName);
            } else if(previousActivity.contains("-Out-Hit-Wicket")) {
                System.out.println("Undo hit wicket");
                String playerName = previousActivity
                        .replace("-Out-Hit-Wicket", "");
                undoDone = undoHitWicket(playerName);
            }
        }

        if(!undoDone) {
            Toast.makeText(this, "Unsupported undo.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleInfoClick(View view) {
        System.out.println("Go to match info activity");
        // push match object
        Intent intent = new Intent(this, MatchInformationActivity.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", match);
        startActivity(intent);
    }

    public void handleEditPlayersClick(View view) {
        System.out.println("Edit the current match players.");
        // Select new players -> cancellable
        String[] remainingPlayers = Utils.getRemainingPlayersList(nameToIdMap, this.match);
        System.out.println("Remaining Players: " + Arrays.toString(remainingPlayers));
        boolean[] selectedPlayers = new boolean[remainingPlayers.length];
        ArrayList<Integer> playersList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScoreActivity.this);
        builder.setTitle("Select the new players which are playing?");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(remainingPlayers, selectedPlayers,
                (dialog, which, isChecked) -> {
                    // Check condition
                    if(isChecked) {
                        // when this checkbox is selected, we will add this in our players list
                        playersList.add(which);
                        // we sort our array list
                        Collections.sort(playersList);
                    } else {
                        // when unselected, remove position from our list
                        playersList.remove(Integer.valueOf(which));
                    }
                });

        builder.setPositiveButton("OK", (dialog, which) -> {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i<playersList.size(); i++) {
                stringBuilder.append(remainingPlayers[playersList.get(i)]);
                if(i != playersList.size() - 1) {
                    stringBuilder.append(", ");
                }
            }

            System.out.println("The new players selected are: " + stringBuilder);

            // now we have selected new players

            // for selecting the players for batting team show all the match players
            // plus these new players

            ArrayList<String> temp = new ArrayList<>();
            for(int val: playersList) {
                temp.add(remainingPlayers[val]);
            }

            // temp only contains new players.
            temp.addAll(this.match.getMatchPlayers());
            temp.remove(this.battingTeam.getCaptainName());
            temp.remove(this.bowlingTeam.getCaptainName());
            Collections.sort(temp);

            // now sorted temp contains all players which are playing the match.
            boolean isEven = Utils.isEven(temp.size());
            int maxPlayersToSelect = temp.size()/2;

            // player options to select for batting team
            String[] playersOptionList = temp.toArray(new String[0]);
            boolean[] playersSelectedInTeam = new boolean[playersOptionList.length];
            ArrayList<String> playerSelectedArray = new ArrayList<>();

            // update playersSelectedInTeam
            for(int i=0; i<playersOptionList.length; i++) {
                String playerName = playersOptionList[i];
                if(this.battingTeam.getPlayerNames().contains(playerName)) {
                    System.out.println("Player " + playerName + " is already present in "
                            + this.battingTeam.getName() + " team");
                    playersSelectedInTeam[i] = true;
                    playerSelectedArray.add(playerName);
                }
            }

            // Now, our updated boolean list and playerSelectedIndex contains all players which are in batting team.
            AlertDialog.Builder selectPlayersBuilder = new AlertDialog
                    .Builder(MatchScoreActivity.this);
            selectPlayersBuilder.setTitle("Select " + maxPlayersToSelect + " players for "
                    + this.battingTeam.getName() + " team.");
            selectPlayersBuilder.setCancelable(false);

            selectPlayersBuilder.setNegativeButton("Cancel",
                    (dialog1, which1) -> dialog1.dismiss());

            selectPlayersBuilder.setNeutralButton("Clear", (dialog1, which1) -> {
                for(int i=0; i<playersOptionList.length; i++) {
                    playersSelectedInTeam[i] = false;
                }
                playerSelectedArray.clear();
            });

            selectPlayersBuilder.setMultiChoiceItems(playersOptionList, playersSelectedInTeam,
                    (dialog1, which1, isChecked) -> {
                        // Check condition
                        if(isChecked) {
                            // when this checkbox is selected, we will add this in our players list
                            playerSelectedArray.add(playersOptionList[which1]);
                            // we sort our array list
                            Collections.sort(playerSelectedArray);
                        } else {
                            // when unselected, remove position from our list
                            playerSelectedArray.remove(playersOptionList[which1]);
                        }
                    });

            selectPlayersBuilder.setPositiveButton("OK",
                    (dialog1, which1) -> {
                    if(playerSelectedArray.size() > maxPlayersToSelect) {
                        Toast.makeText(this,
                                "You need to select only " + maxPlayersToSelect + " players.",
                                Toast.LENGTH_LONG).show();
                        return;
                }

                ArrayList<String> temp2 = new ArrayList<>(playerSelectedArray);
                System.out.println("the players selected in " + this.battingTeam.getName()
                        + " are : "  + temp2);

                ArrayList<String> temp3 = new ArrayList<>();
                for(String player: playersOptionList) {
                    // if
                    if(!temp2.contains(player)) {
                        temp3.add(player);
                    }
                }

                if(isEven) {
                    // we have balanced teams now.
                    this.rebalanceTeams(temp2, temp3, "");
                    this.battingTeam.setCommonName("");
                    this.bowlingTeam.setCommonName("");
                    Toast.makeText(this, "Teams have been rebalanced.",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Go to options to select the common player
                    System.out.println("Select the common player from the remaining players.");

                    String[] commonOptions = temp3.toArray(new String[0]);
                    int[] commonPlayerSelected = {-1};

                    AlertDialog.Builder commonPlayerBuilder = new AlertDialog
                            .Builder(MatchScoreActivity.this);
                    commonPlayerBuilder.setTitle("Please select common player");
                    commonPlayerBuilder.setCancelable(false);

                    commonPlayerBuilder.setSingleChoiceItems(commonOptions, commonPlayerSelected[0],
                            (dialog3, which3) -> commonPlayerSelected[0] = which3);

                    commonPlayerBuilder.setPositiveButton("Ok",
                            (dialog3, which3) -> {
                            if(commonPlayerSelected[0] == -1) {
                                Toast.makeText(this,
                                        "You need to select the common player.",
                                        Toast.LENGTH_LONG).show();
                        }

                        String commonPlayer = commonOptions[commonPlayerSelected[0]];
                        System.out.println("Common player selected is: " + commonPlayer);

                        // temp3 already has common player. Add it to temp2.
                        temp2.add(commonPlayer);

                        // rebalance teams
                        this.rebalanceTeams(temp2, temp3, commonPlayer);

                        Toast.makeText(this, "Teams have been rebalanced.",
                                Toast.LENGTH_LONG).show();
                    });

                    commonPlayerBuilder.show();

                    if(commonPlayerSelected[0] == -1) {
                        System.out.println("Impossible condition.");
                        Toast.makeText(this, "You need to select the common player.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            selectPlayersBuilder.show();
        });

        builder.setNegativeButton("Cancel",
                (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear All", (dialog, which) -> {
            Arrays.fill(selectedPlayers, false);
            playersList.clear();
        });

        builder.show();

        Toast.makeText(this,
                "You need to select the new players.",
                Toast.LENGTH_LONG).show();
    }

}