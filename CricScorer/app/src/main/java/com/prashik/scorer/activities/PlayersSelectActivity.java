package com.prashik.scorer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlayersSelectActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;
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
        this.allPlayers = (HashMap<String, Player>) getIntent().getSerializableExtra("all_players_hashmap");
        this.allBattingStats = (HashMap<String, BattingStats>) getIntent().getSerializableExtra("all_batting_stats_hashmap");
        this.allBowlingStats = (HashMap<String, BowlingStats>) getIntent().getSerializableExtra("all_bowling_stats_hashmap");
        this.allMatchesStats = (HashMap<String, MatchStats>) getIntent().getSerializableExtra("all_matches_stats_hashmap");
        this.nameToIdMap = Utils.getPlayerNamesToIdMap(allPlayers);
        this.playersArray = Utils.getPlayersList(this.nameToIdMap);
        this.match = (Match) getIntent().getSerializableExtra("match_object");

        TextView textView = findViewById(R.id.select_playing_players);
        selectedPlayers = new boolean[allPlayers.size()];

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayersSelectActivity.this);
                TextView showAllPlayersTextView = findViewById(R.id.show_all_playing_players_sp);
                builder.setTitle("Select Playing Players");
                builder.setCancelable(false);

                // setup on click
                builder.setMultiChoiceItems(playersArray, selectedPlayers, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
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
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i=0; i<playersList.size(); i++) {
                            stringBuilder.append(playersArray[playersList.get(i)]);
                            if(i != playersList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        showAllPlayersTextView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0; i<selectedPlayers.length; i++) {
                            selectedPlayers[i] = false;
                            playersList.clear();
                            showAllPlayersTextView.setText("");
                        }
                    }
                });

                builder.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}