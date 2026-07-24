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

import java.util.HashMap;

public class EditPlayerActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;
    HashMap<String, String> nameToIdMap;

    String playerId;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_player);

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
                case "players_name_to_id_map_file_location":
                    nameToIdMap = Utils.readNameToIdMapFile(dataFile);
                    break;
            }
        }

        this.playerId = getIntent().getStringExtra("player_id");
        this.player = this.allPlayers.get(this.playerId);

        EditText editText = findViewById(R.id.first_name_edit);
        editText.setText(this.player.getFirstName());

        editText = findViewById(R.id.last_name_edit);
        editText.setText(this.player.getLastName());

        editText = findViewById(R.id.email_edit);
        editText.setText(this.player.getEmail());

        editText = findViewById(R.id.phone_number_edit);
        editText.setText(this.player.getPhoneNumber());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleSubmitClick(View view) {
        EditText editText = findViewById(R.id.first_name_edit);
        String firstName = editText.getText().toString();
        if (firstName.isEmpty()) {
            Toast.makeText(this, "First Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        this.player.setFirstName(firstName);

        editText = findViewById(R.id.last_name_edit);
        String lastName = editText.getText().toString();
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        this.player.setLastName(lastName);

        editText = findViewById(R.id.email_edit);
        String email = editText.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        this.player.setEmail(email);

        editText = findViewById(R.id.phone_number_edit);
        String phoneNumber = editText.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone Number cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        this.player.setPhoneNumber(phoneNumber);

        // Add checks if player already exists
        if(Utils.playerAlreadyExistsEdit(allPlayers, player)) {
            Toast.makeText(this, "The Player with same information already exists.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Email and Phone number of player needs to be unique.", Toast.LENGTH_LONG).show();
            return;
        }

        // Sync the current hashset
        allPlayers.remove(player.getId());
        allPlayers.put(player.getId(), player);
        nameToIdMap.put(player.getFullName(), player.getId());

        // Sync data in files
        Utils.syncPlayersData(dataFilesMap.get("players_data_file_location"), allPlayers);
        Utils.syncNameToIdMapData(dataFilesMap.get("players_name_to_id_map_file_location"), nameToIdMap);

        // Start player info activity
        Intent intent = new Intent(this, PlayerInformationActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("player_id", player.getId());
        intent.putExtra("previous_activity", "edit_player_activity");
        startActivity(intent);
    }
}