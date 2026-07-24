package com.prashik.scorer.activities;

import android.annotation.SuppressLint;
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
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class PlayerInformationActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;
    String playerId;
    Player player;
    BattingStats battingStats;
    BowlingStats bowlingStats;
    MatchStats matchStats;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_information);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            switch (s) {
                case "players_data_file_location":
                    this.allPlayers = Utils.readPlayersFile(dataFile);
                    break;
                case "players_batting_data_file_location":
                    this.allBattingStats = Utils.readBattingStatsFile(dataFile);
                    break;
                case "players_bowling_data_file_location":
                    this.allBowlingStats = Utils.readBowlingStatsFile(dataFile);
                    break;
                case "players_matches_data_file_location":
                    this.allMatchesStats = Utils.readMatchStatsFile(dataFile);
                    break;
            }
        }

        this.playerId = getIntent().getStringExtra("player_id");
        this.player = this.allPlayers.get(this.playerId);
        this.battingStats = this.allBattingStats.get(this.playerId);
        this.bowlingStats = this.allBowlingStats.get(this.playerId);
        this.matchStats = this.allMatchesStats.get(this.playerId);

        String previousActivity = "";
        if(getIntent().getStringExtra("previous_activity") != null) {
            previousActivity = getIntent().getStringExtra("previous_activity");
        }

        assert previousActivity != null;
        if(previousActivity.equals("edit_player_activity") || previousActivity.equals("add_new_player_activity")) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Toast.makeText(PlayerInformationActivity.this, "You cannot go back now. " +
                            "Press home instead.", Toast.LENGTH_LONG).show();
                }
            };
            getOnBackPressedDispatcher().addCallback(callback);
        }
        System.out.println("Previous Activity : " + previousActivity);

        // Player heading
        TextView textView = findViewById(R.id.player_name_pi);
        textView.setText(this.player.getFullName());

        textView = findViewById(R.id.player_information_pi);
        String playerInformation = String.format("First Name: %s    Last Name: %s\n" +
                        "Matches Played: %d    Phone No: %s\n" +
                "Email: %s\n",
                this.player.getFirstName(), this.player.getLastName(),
                this.matchStats.getMatchesPlayed(), this.player.getPhoneNumber(),
                this.player.getEmail());
        textView.setText(playerInformation);

        textView = findViewById(R.id.batting_statistics_pi);
        String battingStatsText = String.format("Innings: %d    Runs: %d    Strike Rate: %.2f\n" +
                        "Average: %.2f    Balls Played: %d\n" +
                        "Best: %d    4s: %d    6s: %d    0s: %d\n" +
                        "20s: %d    30s: %d    50s: %d    Out Count: %d",
                this.battingStats.getInningsPlayed(), this.battingStats.getRuns(), this.battingStats.getStrikeRate(),
                this.battingStats.getBattingAverage(), this.battingStats.getBallsPlayed(),
                this.battingStats.getBestScore(), this.battingStats.getFours(), this.battingStats.getSixes(), this.battingStats.getDots(),
                this.battingStats.getTwenties(), this.battingStats.getThirties(), this.battingStats.getFifties(), this.battingStats.getOutCount());
        textView.setText(battingStatsText);


        textView = findViewById(R.id.bowling_statistics_pi);
        String bowlingStatsText = String.format("Innings Bowled: %d    Wickets: %d    Economy: %.2f\n" +
                        "Average: %.2f    Best: %s    Runs Conceded: %d\n" +
                        "Two Wickets: %d    Three Wickets: %d    Five Wickets: %d\n" +
                        "Bowled Wickets: %d    Fours Conceded: %d    Six Conceded: %d\n" +
                        "Dots Bowled: %d    Wides: %d    No Balls: %d\n" +
                        "Extras Conceded: %d    Overs Bowled: %d    Maidens Overs: %d\n" +
                        "Deliveries Bowled: %d    Legal Deliveries Bowled: %d",
                this.bowlingStats.getInningsBowled(), this.bowlingStats.getWickets(), this.bowlingStats.getEconomy(),
                this.bowlingStats.getAverage(), this.bowlingStats.getBestBowling(), this.bowlingStats.getRuns(),
                this.bowlingStats.getTwoFer(), this.bowlingStats.getThreeFer(), this.bowlingStats.getFiveFer(),
                this.bowlingStats.getBowledWickets(), this.bowlingStats.getFours(), this.bowlingStats.getSixes(),
                this.bowlingStats.getDots(), this.bowlingStats.getWides(), this.bowlingStats.getNos(),
                this.bowlingStats.getExtras(), this.bowlingStats.getNumberOfOvers(), this.bowlingStats.getMaidensBowled(),
                this.bowlingStats.getDeliveriesBowled(), this.bowlingStats.getLegalDeliveriesBowled());
        textView.setText(bowlingStatsText);

        textView = findViewById(R.id.fielding_statistics_pi);
        String fieldingStatsText = String.format("Catches: %d    Run Outs: %d",
                this.matchStats.getCatches(), this.matchStats.getRunOuts());
        textView.setText(fieldingStatsText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleEditClick(View view) {
        Log.d("debug", "We will open Edit Player Activity here.");
        Intent intent = new Intent(this, EditPlayerActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("player_id", player.getId());
        startActivity(intent);
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}