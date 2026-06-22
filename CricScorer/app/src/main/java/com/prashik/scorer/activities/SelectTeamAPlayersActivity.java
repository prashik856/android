package com.prashik.scorer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SelectTeamAPlayersActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, String> nameToIdMap;
    Match match;
    String[] matchPlayers;
    boolean[] selectedPlayers;
    ArrayList<Integer> playersList = new ArrayList<>();
    int teamMaxPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_team_aplayers);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.matchPlayers = (String[]) getIntent().getSerializableExtra("match_players");
        this.nameToIdMap = (HashMap<String, String>) getIntent().getSerializableExtra("name_to_id_map");
        this.teamMaxPlayers = matchPlayers.length / 2;

        String teamAName = this.match.getTeamA().getName();

        TextView textView = findViewById(R.id.team_a_name_stp);
        textView.setText(String.format("Select %s Players", teamAName));

        selectedPlayers = new boolean[matchPlayers.length];
        TextView textView1 = findViewById(R.id.select_team_a_players);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectTeamAPlayersActivity.this);
                TextView showAllTeamAPlayers = findViewById(R.id.show_all_team_a_players);
                builder.setTitle("Select Team Players");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(matchPlayers, selectedPlayers,
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
                        stringBuilder.append(matchPlayers[playersList.get(i)]);
                        if(i != playersList.size() - 1) {
                            stringBuilder.append(", ");
                        }
                    }
                    showAllTeamAPlayers.setText(stringBuilder.toString());
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.setNeutralButton("Clear All", (dialog, which) -> {
                    for(int i=0; i<selectedPlayers.length; i++) {
                        selectedPlayers[i] = false;
                        playersList.clear();
                        showAllTeamAPlayers.setText("");
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

    public void handleNextClickButton(View view) {
        boolean evenPlayers = Utils.isEven(matchPlayers.length);
        if (playersList.isEmpty()) {
            Toast.makeText(this, "Team cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        // Max team size needed to be maintained
        if(playersList.size() != teamMaxPlayers) {
            Toast.makeText(this, "Team size cannot be exceed " + this.teamMaxPlayers, Toast.LENGTH_LONG).show();
            return;
        }

        // Now our playersList have players in teamA
        ArrayList<Integer> remainingPlayers = new ArrayList<>();
        for(int i=0; i<matchPlayers.length; i++) {
            if(!playersList.contains(i)) {
                remainingPlayers.add(i);
            }
        }

        // playersList -> team A
        if(evenPlayers) {

        }
    }
}