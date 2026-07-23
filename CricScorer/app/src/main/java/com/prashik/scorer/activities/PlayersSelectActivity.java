package com.prashik.scorer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class PlayersSelectActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, String> nameToIdMap;
    Match match;
    boolean[] selectedPlayers;
    ArrayList<Integer> playersList = new ArrayList<>();
    String[] playersArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_select);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        assert dataFilesMap != null;
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
        }
        this.nameToIdMap = Utils.getPlayerNamesToIdMap(allPlayers);
        this.playersArray = Utils.getPlayersList(this.nameToIdMap);
        this.match = (Match) getIntent().getSerializableExtra("match_object");

        TextView textView = findViewById(R.id.select_playing_players);
        selectedPlayers = new boolean[allPlayers.size()];

        textView.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayersSelectActivity.this);
            TextView showAllPlayersTextView = findViewById(R.id.show_all_playing_players_sp);
            builder.setTitle("Select Playing Players");
            builder.setCancelable(false);

            // setup on click
            builder.setMultiChoiceItems(playersArray, selectedPlayers,
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
                    stringBuilder.append(playersArray[playersList.get(i)]);
                    if(i != playersList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                showAllPlayersTextView.setText(stringBuilder.toString());
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear All", (dialog, which) -> {
                for(int i=0; i<selectedPlayers.length; i++) {
                    selectedPlayers[i] = false;
                    playersList.clear();
                    showAllPlayersTextView.setText("");
                }
            });

            builder.show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleNextClick(View view) {
        // all selected players are playersList (it contains indexes)
        if(playersList.isEmpty()) {
            Toast.makeText(this, "Players playing cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if(playersList.size() < 2) {
            Toast.makeText(this, "Players playing cannot be less than 2", Toast.LENGTH_LONG).show();
            return;
        }

        String[] matchPlayers = {};
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<playersList.size(); i++) {
            // index is index of playersArray String
            int index = playersList.get(i);
            String playerName = playersArray[index];
            temp.add(playerName);
        }

        this.match.setMatchPlayers(temp);
        System.out.println("Match Players are: " + this.match.getMatchPlayers().toString());

        matchPlayers = temp.toArray(new String[0]);
        Arrays.sort(matchPlayers);
        System.out.println("Match Players: " + Arrays.toString(matchPlayers));
        // Got my match players
        Intent intent = new Intent(this, SelectCaptainsActivity.class);
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("match_object", this.match);
        intent.putExtra("match_players", matchPlayers);
        intent.putExtra("name_to_id_map", this.nameToIdMap);
        startActivity(intent);
    }
}