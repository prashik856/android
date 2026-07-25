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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.adapters.PlayersAdapter;
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PlayersActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;
    HashMap<String, String> nameToIdMap;
    PlayersAdapter playersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_players);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            switch (s) {
                case "players_data_file_location":
                    allPlayers = Utils.readPlayersFile(dataFile);
                    break;
                case "players_batting_data_file_location":
                    allBattingStats = Utils.readBattingStatsFile(dataFile);
                    break;
                case "players_bowling_data_file_location":
                    allBowlingStats = Utils.readBowlingStatsFile(dataFile);
                    break;
                case "players_matches_data_file_location":
                    allMatchesStats = Utils.readMatchStatsFile(dataFile);
                    break;
                case "players_name_to_id_map_file_location":
                    nameToIdMap = Utils.readNameToIdMapFile(dataFile);
                    break;
            }
        }

        this.playersAdapter = new PlayersAdapter(this.allPlayers, this.allMatchesStats, this.nameToIdMap);
        RecyclerView recyclerView = findViewById(R.id.all_players_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playersAdapter);

        String previousAcvitivy = "";
        if(getIntent().getStringExtra("previous_activity") != null) {
            previousAcvitivy = getIntent().getStringExtra("previous_activity");
        }

        if(!previousAcvitivy.equals("main_activity")) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Toast.makeText(PlayersActivity.this, "You cannot go back now. " +
                            "Press home instead.", Toast.LENGTH_LONG).show();
                }
            };
            getOnBackPressedDispatcher().addCallback(callback);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleOnPlayerClick(View view) {
        TextView textView = (TextView) view;
        String playerId = (String) textView.getContentDescription();
        Intent intent = new Intent(this, PlayerInformationActivity.class);
        intent = Utils.putDataFiles(intent, dataFilesMap, allPlayers, allBattingStats, allBowlingStats, allMatchesStats);
        intent.putExtra("player_id", playerId);
        startActivity(intent);
    }

    public void handleSyncClick(View view) {
        System.out.println("Syncing name to id data.");
        Utils.syncNameToIdMapping(allPlayers, nameToIdMap, dataFilesMap);

        System.out.println("Updating match data.");
        String filesDirectory = this.dataFilesMap.get("files_directory");
        System.out.println("Files directory: " + filesDirectory);
        ArrayList<String> matchFiles = Utils.getMatchFiles(Utils.getAllFilesInDirectory(filesDirectory));
        System.out.println("All Match Files: " + matchFiles);
        for(String matchfile: matchFiles) {
            String fileToRead = filesDirectory + "/" + matchfile;
            System.out.println("File to read: " + fileToRead);

            Match match = Utils.readMatchFile(fileToRead);
            System.out.println("Current Match Object: " + match.toString());
            if(match.isCompleted()) {
                Utils.updateGlobalRecords(dataFilesMap, match);
            }
        }

        Toast.makeText(PlayersActivity.this, "Sync complete.", Toast.LENGTH_LONG).show();

        System.out.println("Reopening activity to update the page.");
        Intent intent = new Intent(this, PlayersActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("previous_activity", "main_activity");
        startActivity(intent);
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}