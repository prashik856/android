package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SelectTeamAPlayersActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, String> nameToIdMap;
    Match match;
    String[] matchPlayers;
    String[] playersWithoutCaptain;
    boolean[] selectedPlayers;
    ArrayList<Integer> playersList;
    int teamMaxPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_team_aplayers);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        assert dataFilesMap != null;
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
        }
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.matchPlayers = (String[]) getIntent().getSerializableExtra("match_players");
        this.nameToIdMap = (HashMap<String, String>) getIntent().getSerializableExtra("name_to_id_map");
        this.teamMaxPlayers = matchPlayers.length / 2;
        this.playersList = new ArrayList<>();

        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<this.matchPlayers.length; i++) {
            String player = this.matchPlayers[i];
            if(player.equals(this.match.getTeamA().getCaptainName()) || player.equals(this.match.getTeamB().getCaptainName())) {
                continue;
            }
            temp.add(player);
        }
        playersWithoutCaptain = temp.toArray(new String[0]);

        String teamAName = this.match.getTeamA().getName();

        TextView textView = findViewById(R.id.team_a_name_stp);
        textView.setText(String.format("Select %s Players", teamAName));

        selectedPlayers = new boolean[playersWithoutCaptain.length];
        TextView textView1 = findViewById(R.id.select_team_a_players);
        textView1.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectTeamAPlayersActivity.this);
            TextView showAllTeamAPlayers = findViewById(R.id.show_all_team_a_players);
            builder.setTitle("Select Team Players");
            builder.setCancelable(false);

            builder.setMultiChoiceItems(playersWithoutCaptain, selectedPlayers,
                    (dialog, which, isChecked) -> {
                        // Check condition
                        if(isChecked) {
                            // when this checkbox is selected, we will add this in our players list
                            if(!playersList.contains(which)) {
                                playersList.add(which);
                                // we sort our array list
                                Collections.sort(playersList);
                            }
                        } else {
                            // when unselected, remove position from our list
                            playersList.remove(Integer.valueOf(which));
                        }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<playersList.size(); i++) {
                    stringBuilder.append(playersWithoutCaptain[playersList.get(i)]);
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
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleNextClickButton(View view) {
        boolean evenPlayers = Utils.isEven(this.playersWithoutCaptain.length);
        if (playersList.isEmpty()) {
            Toast.makeText(this, "Team cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        // Max team size needed to be maintained
        if(playersList.size() + 1 != teamMaxPlayers) {
            Toast.makeText(this, "Team size needs to be " + this.teamMaxPlayers, Toast.LENGTH_LONG).show();
            return;
        }

        // Now our playersList have players in teamA
        ArrayList<Integer> remainingPlayers = new ArrayList<>();
        for(int i=0; i<playersWithoutCaptain.length; i++) {
            if(!playersList.contains(i)) {
                remainingPlayers.add(i);
            }
        }

        System.out.println("Players List: " + playersList.toString());
        System.out.println("Remaining Players List: " + remainingPlayers);
        System.out.println("Even players: " + evenPlayers);

        // playersList -> team A
        if(evenPlayers) {
            // Create both team players
            // playersList -> teamA players
            System.out.println("We have even players.");
            for(int i=0; i<playersList.size(); i++) {
                String name = this.playersWithoutCaptain[playersList.get(i)];
                MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

                if(!match.getTeamA().getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                    System.out.println("Adding player " + matchPlayer.getPlayer().getFullName() + " to team A");
                    match.getTeamA().getPlayerNames().add(matchPlayer.getPlayer().getFullName());
                    match.getTeamA().getTeamPlayers().add(matchPlayer);
                }
            }

            // Add captain
            MatchPlayer captainPlayer = Utils.getMatchPlayer(this.match.getTeamA().getCaptainName(), this.nameToIdMap, this.allPlayers);
            if(!match.getTeamA().getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
                System.out.println("Adding player " + captainPlayer.getPlayer().getFullName() + " to team A");
                match.getTeamA().getPlayerNames().add(captainPlayer.getPlayer().getFullName());
                match.getTeamA().getTeamPlayers().add(captainPlayer);
            }

            // remainingPlayers -> teamB players
            for(int i=0; i<remainingPlayers.size(); i++) {
                String name = this.playersWithoutCaptain[remainingPlayers.get(i)];
                MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

                if(!match.getTeamB().getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                    System.out.println("Adding player " + matchPlayer.getPlayer().getFullName() + " to team B");
                    match.getTeamB().getPlayerNames().add(matchPlayer.getPlayer().getFullName());
                    match.getTeamB().getTeamPlayers().add(matchPlayer);
                }
            }

            // add captain
            captainPlayer = Utils.getMatchPlayer(this.match.getTeamB().getCaptainName(), this.nameToIdMap, this.allPlayers);
            if(!match.getTeamB().getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
                System.out.println("Adding player " + captainPlayer.getPlayer().getFullName() + " to team B");
                match.getTeamB().getPlayerNames().add(captainPlayer.getPlayer().getFullName());
                match.getTeamB().getTeamPlayers().add(captainPlayer);
            }

            System.out.println("Team A Players: " + this.match.getTeamA().getPlayerNames().toString());
            System.out.println("Team B Players: " + this.match.getTeamB().getPlayerNames().toString());

            this.match.getTeamA().setTeamSize(this.match.getTeamA().getTeamPlayers().size());
            this.match.getTeamB().setTeamSize(this.match.getTeamB().getTeamPlayers().size());

            if(this.match.getTeamA().getTeamSize() != this.match.getTeamB().getTeamSize()) {
                Toast.makeText(this, "Team size needs to be same.", Toast.LENGTH_LONG).show();
                return;
            }

            this.match.getTeamA().setMaxWickets(this.match.getTeamA().getTeamSize() - 1);
            this.match.getTeamB().setMaxWickets(this.match.getTeamB().getTeamSize() - 1);

            Intent intent = new Intent(this, SelectTossWinningTeam.class);
            intent.putExtra("data_files_hashmap", dataFilesMap);
            intent.putExtra("match_object", this.match);
            intent.putExtra("name_to_id_map", this.nameToIdMap);
            startActivity(intent);
        } else {
            System.out.println("We don't have even players.");
            // Select Common
            Intent intent = new Intent(this, SelectCommonPlayerActivity.class);
            intent.putExtra("data_files_hashmap", dataFilesMap);
            intent.putExtra("match_object", this.match);
            intent.putExtra("name_to_id_map", this.nameToIdMap);
            intent.putExtra("players_list", this.playersList);
            intent.putExtra("remaining_players_list", remainingPlayers);
            intent.putExtra("match_players", this.matchPlayers);
            intent.putExtra("players_without_captain", this.playersWithoutCaptain);
            startActivity(intent);
        }
    }
}