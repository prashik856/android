package com.prashik.scorer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

        // Player Info
        TextView textView = findViewById(R.id.player_name_text);
        textView.setText(String.format("Name: %s %s", this.player.getFirstName(), this.player.getLastName()));

        textView = findViewById(R.id.matches_played_text);
        textView.setText(String.format("Matches: %d", this.matchStats.getMatchesPlayed()));

        textView = findViewById(R.id.email_text_pi);
        textView.setText(String.format("Email: %s", this.player.getEmail()));

        textView = findViewById(R.id.phone_text_pi);
        textView.setText(String.format("No: %s", this.player.getPhoneNumber()));

        // Batting info
        textView = findViewById(R.id.runs_text);
        textView.setText(String.format("Runs: %d", this.battingStats.getRuns()));

        textView = findViewById(R.id.average_text);
        textView.setText(String.format("Average: %f", this.battingStats.getBattingAverage()));

        textView = findViewById(R.id.strike_rate_text);
        textView.setText(String.format("Strike Rate: %f", this.battingStats.getStrikeRate()));

        textView = findViewById(R.id.innings_text);
        textView.setText(String.format("Innings: %d", this.battingStats.getInningsPlayed()));

        textView = findViewById(R.id.best_score_text);
        textView.setText(String.format("Best: %d", this.battingStats.getBestScore()));

        textView = findViewById(R.id.fours_text);
        textView.setText(String.format("Fours: %d", this.battingStats.getFours()));

        textView = findViewById(R.id.sixes_text);
        textView.setText(String.format("Sixes: %d", this.battingStats.getSixes()));

        textView = findViewById(R.id.dots_text);
        textView.setText(String.format("Dots: %d", this.battingStats.getDots()));

        textView = findViewById(R.id.twenties_text);
        textView.setText(String.format("Twenties: %d", this.battingStats.getTwenties()));

        textView = findViewById(R.id.thirties_text);
        textView.setText(String.format("Thirties: %d", this.battingStats.getThirties()));

        textView = findViewById(R.id.fifties_text);
        textView.setText(String.format("Fifties: %d", this.battingStats.getFifties()));

        // Bowling info
        textView = findViewById(R.id.wickets_text);
        textView.setText(String.format("Wickets: %d", this.bowlingStats.getWickets()));

        textView = findViewById(R.id.bowling_average_text_pi);
        textView.setText(String.format("Average: %f", this.bowlingStats.getAverage()));

        textView = findViewById(R.id.economy_text);
        textView.setText(String.format("Economy: %f", this.bowlingStats.getEconomy()));

        textView = findViewById(R.id.best_bowling_text);
        textView.setText(String.format("Best: %s", this.bowlingStats.getBestBowling()));

        textView = findViewById(R.id.twofer_text);
        textView.setText(String.format("Twofer: %d", this.bowlingStats.getTwoFer()));

        textView = findViewById(R.id.threefer_text);
        textView.setText(String.format("Threefer: %d", this.bowlingStats.getThreeFer()));

        textView = findViewById(R.id.fivefer_text);
        textView.setText(String.format("Fivefer: %d", this.bowlingStats.getFiveFer()));

        textView = findViewById(R.id.fours_text_pi);
        textView.setText(String.format("Fours: %d", this.bowlingStats.getFours()));

        textView = findViewById(R.id.sixes_text_pi);
        textView.setText(String.format("Sixes: %d", this.bowlingStats.getSixes()));

        textView = findViewById(R.id.dots_pi);
        textView.setText(String.format("Dots: %d", this.bowlingStats.getDots()));

        textView = findViewById(R.id.wides_text);
        textView.setText(String.format("Wides: %d", this.bowlingStats.getWides()));

        textView = findViewById(R.id.no_ball_text);
        textView.setText(String.format("No Balls: %d", this.bowlingStats.getNos()));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleEditClick(View view) {
        Log.d("debug", "We will open Edit Player Activity here.");
        Intent intent = new Intent(this, EditPlayerActivity.class);
        intent = Utils.putDataFiles(intent, dataFilesMap, allPlayers, allBattingStats, allBowlingStats, allMatchesStats);
        intent.putExtra("player_id", player.getId());
        startActivity(intent);
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}