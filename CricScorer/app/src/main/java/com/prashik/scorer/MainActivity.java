package com.prashik.scorer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.activities.AddNewPlayerActivity;
import com.prashik.scorer.activities.NewMatchActivity;
import com.prashik.scorer.activities.PlayersActivity;
import com.prashik.scorer.activities.PreviousMatchesActivity;
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap = new HashMap<>();
    HashMap<String, Player> allPlayers = new HashMap<>();
    HashMap<String, BattingStats> allBattingStats = new HashMap<>();
    HashMap<String, BowlingStats> allBowlingStats = new HashMap<>();
    HashMap<String, MatchStats> allMatchesStats = new HashMap<>();
    HashMap<String, String> nameToIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        dataFilesMap.put("players_data_file_location",
                getFilesDir().toString() + "/" + getString(R.string.all_players_data_file));
        dataFilesMap.put("players_batting_data_file_location",
                getFilesDir().toString() + "/" + getString(R.string.batting_data_file));
        dataFilesMap.put("players_bowling_data_file_location",
                getFilesDir().toString() + "/" + getString(R.string.bowling_data_file));
        dataFilesMap.put("players_matches_data_file_location",
                getFilesDir().toString() + "/" + getString(R.string.matches_data_file));
        dataFilesMap.put("players_name_to_id_map_file_location",
                getFilesDir().toString() + "/" + getString(R.string.name_to_id_file_name));
        dataFilesMap.put("files_directory", getFilesDir().toString());

        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            boolean fileCreated;
            switch (s) {
                case "players_data_file_location":
                    fileCreated = Utils.createFile(dataFile);
                    if(fileCreated) {
                        Utils.syncPlayersData(dataFile, allPlayers);
                    } else {
                        allPlayers = Utils.readPlayersFile(dataFile);
                    }
                    break;
                case "players_batting_data_file_location":
                    fileCreated = Utils.createFile(dataFile);
                    if (fileCreated) {
                        Utils.syncBattingStatsData(dataFile, allBattingStats);
                    } else {
                        allBattingStats = Utils.readBattingStatsFile(dataFile);
                    }
                    break;
                case "players_bowling_data_file_location":
                    fileCreated = Utils.createFile(dataFile);
                    if(fileCreated) {
                        Utils.syncBowlingStatsData(dataFile, allBowlingStats);
                    } else {
                        allBowlingStats = Utils.readBowlingStatsFile(dataFile);
                    }
                    break;
                case "players_matches_data_file_location":
                    fileCreated = Utils.createFile(dataFile);
                    if(fileCreated) {
                        Utils.syncMatchStatsData(dataFile, allMatchesStats);
                    } else {
                        allMatchesStats = Utils.readMatchStatsFile(dataFile);
                    }
                    break;
                case "players_name_to_id_map_file_location":
                    fileCreated = Utils.createFile(dataFile);
                    if(fileCreated) {
                        Utils.syncNameToIdMapData(dataFile, nameToIdMap);
                    } else {
                        nameToIdMap = Utils.readNameToIdMapFile(dataFile);
                    }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleAddNewPlayerClick(View view) {
        Intent intent = new Intent(this, AddNewPlayerActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        startActivity(intent);
    }

    public void handlePlayersClick(View view) {
        Intent intent = new Intent(this, PlayersActivity.class);
        intent = Utils.putDataFiles(intent, dataFilesMap, allPlayers, allBattingStats, allBowlingStats, allMatchesStats);
        startActivity(intent);
    }

    public void handlePreviousMatchesClick(View view) {
        Intent intent = new Intent(this, PreviousMatchesActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        startActivity(intent);
    }

    public void handleNewMatchClick(View view) {
        Intent intent = new Intent(this, NewMatchActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        startActivity(intent);
    }
}