package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.io.IOException;
import java.util.HashMap;

public class AddNewPlayerActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_player);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.allPlayers = (HashMap<String, Player>) getIntent().getSerializableExtra("all_players_hashmap");
        this.allBattingStats = (HashMap<String, BattingStats>) getIntent().getSerializableExtra("all_batting_stats_hashmap");
        this.allBowlingStats = (HashMap<String, BowlingStats>) getIntent().getSerializableExtra("all_bowling_stats_hashmap");
        this.allMatchesStats = (HashMap<String, MatchStats>) getIntent().getSerializableExtra("all_matches_stats_hashmap");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleSubmitClick(View view) {
        EditText editText = findViewById(R.id.first_name_text);
        String firstName = editText.getText().toString();
        if (firstName.isEmpty()) {
            Toast.makeText(this, "First Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        editText = findViewById(R.id.last_name_text);
        String lastName = editText.getText().toString();
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        editText = findViewById(R.id.email_text);
        String email = editText.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        editText = findViewById(R.id.phone_number_text);
        String phoneNumber = editText.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone Number cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        Player player = new Player(firstName, lastName, email, phoneNumber);
        BattingStats battingStats = new BattingStats(player.getId());
        BowlingStats bowlingStats = new BowlingStats(player.getId());
        MatchStats matchStats = new MatchStats(player.getId());

        // Add checks if player already exists
        if(Utils.playerAlreadyExists(allPlayers, player)) {
            Toast.makeText(this, "The Player with same information already exists.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Email and Phone number of player needs to be unique.", Toast.LENGTH_LONG).show();
            return;
        }

        // object data sync
        allPlayers.put(player.getId(), player);
        allBattingStats.put(battingStats.getPlayerId(), battingStats);
        allBowlingStats.put(bowlingStats.getPlayerId(), bowlingStats);
        allMatchesStats.put(matchStats.getPlayerId(), matchStats);

        // Sync data in files
        Utils.syncPlayersData(dataFilesMap.get("players_data_file_location"), allPlayers);
        Utils.syncBattingStatsData(dataFilesMap.get("players_batting_data_file_location"), allBattingStats);
        Utils.syncBowlingStatsData(dataFilesMap.get("players_bowling_data_file_location"), allBowlingStats);
        Utils.syncMatchStatsData(dataFilesMap.get("players_matches_data_file_location"), allMatchesStats);

        // Start player info activity
        Intent intent = new Intent(this, PlayerInformationActivity.class);
        intent = Utils.putDataFiles(intent, dataFilesMap, allPlayers, allBattingStats, allBowlingStats, allMatchesStats);
        intent.putExtra("player_id", player.getId());
        startActivity(intent);
    }
}