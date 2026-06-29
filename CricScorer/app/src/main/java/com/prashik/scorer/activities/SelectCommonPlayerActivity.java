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
import com.prashik.scorer.models.Team;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SelectCommonPlayerActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, String> nameToIdMap;
    Match match;
    ArrayList<Integer> playerList = new ArrayList<>();
    ArrayList<Integer> remainingPlayersList = new ArrayList<>();
    int selectedPlayer = -1;
    String[] matchPlayers;
    String[] remainingPlayers;
    String chosenAnswer;
    String[] playersWithoutCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_common_player);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
        }
        this.nameToIdMap = (HashMap<String, String>) getIntent().getSerializableExtra("name_to_id_map");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.playerList = (ArrayList<Integer>) getIntent().getSerializableExtra("players_list");
        this.remainingPlayersList = (ArrayList<Integer>) getIntent().getSerializableExtra("remaining_players_list");
        this.matchPlayers = (String[]) getIntent().getSerializableExtra("match_players");
        this.playersWithoutCaptain = (String[]) getIntent().getSerializableExtra("players_without_captain");

        System.out.println("Team A Players List: " + this.playerList);
        System.out.println("Common to be chosen from these players: " + this.remainingPlayersList);

        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<remainingPlayersList.size(); i++) {
            String playerName = this.playersWithoutCaptain[remainingPlayersList.get(i)];
            temp.add(playerName);
        }
        remainingPlayers = temp.toArray(new String[0]);
        System.out.println("Remaining players String Array: " + Arrays.toString(remainingPlayers));


        TextView selectCommonPlayer = findViewById(R.id.select_common_player_cp);
        selectCommonPlayer.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCommonPlayerActivity.this);
            TextView textView = (TextView) v;
            builder.setTitle("Select Common Player");
            builder.setCancelable(false);

            builder.setSingleChoiceItems(remainingPlayers, selectedPlayer, (dialog, which) -> selectedPlayer = which);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                if(selectedPlayer != -1) {
                    chosenAnswer = remainingPlayers[selectedPlayer];
                    textView.setText(chosenAnswer);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                selectedPlayer = -1;
                chosenAnswer = "";
                textView.setText("");
            });

            builder.show();

        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleClickNext(View view) {
        if(selectedPlayer == -1) {
            Toast.makeText(this, "Need to select common player before proceeding ", Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Common Player Selected: " + this.chosenAnswer);
        System.out.println("Players without captain: " + Arrays.toString(this.playersWithoutCaptain));
        System.out.println("Players list adding to team a: " + this.playerList.toString());

        Team teamA = this.match.getTeamA();
        Team teamB = this.match.getTeamB();

        // Add team a players
        for(int i=0; i<playerList.size(); i++) {
            String name = this.playersWithoutCaptain[playerList.get(i)];
            System.out.println("Adding Name to team A- " + name);
            MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

            if(!teamA.getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                teamA.getTeamPlayers().add(matchPlayer);
                teamA.getPlayerNames().add(matchPlayer.getPlayer().getFullName());
            }
        }
        System.out.println("Team A Players after adding all normal players: " + teamA.getPlayerNames());

        // Add common player to team a
        MatchPlayer commonPlayer = Utils.getMatchPlayer(this.chosenAnswer, this.nameToIdMap, this.allPlayers);
        System.out.println("Adding Name to team A- " + this.chosenAnswer);
        if(!teamA.getPlayerNames().contains(commonPlayer.getPlayer().getFullName())) {
            teamA.getTeamPlayers().add(commonPlayer);
            teamA.getPlayerNames().add(commonPlayer.getPlayer().getFullName());
        }
        System.out.println("Team A Players after adding common player: " + teamA.getPlayerNames());

        // Add captain player to team a
        System.out.println("Adding Name to team A- " + teamA.getCaptainName());
        MatchPlayer captainPlayer = Utils.getMatchPlayer(teamA.getCaptainName(), this.nameToIdMap, this.allPlayers);
        if(!teamA.getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
            teamA.getTeamPlayers().add(captainPlayer);
            teamA.getPlayerNames().add(captainPlayer.getPlayer().getFullName());
        }
        System.out.println("Team A Players after adding captain player: " + teamA.getPlayerNames());

        System.out.println("Players list adding to team b: " + this.remainingPlayersList.toString());
        // Add players to team b
        // Remaining players list already has common
        for(int i=0; i<remainingPlayersList.size(); i++) {
            String name = this.playersWithoutCaptain[remainingPlayersList.get(i)];
            System.out.println("Adding Name to team b - " + name);
            MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

            if(!teamB.getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                teamB.getTeamPlayers().add(matchPlayer);
                teamB.getPlayerNames().add(matchPlayer.getPlayer().getFullName());
            }
        }
        System.out.println("Team B Players after adding normal players: " + teamB.getPlayerNames());

        // Add common player to team b
        System.out.println("Adding Name to team b - " + commonPlayer.getPlayer().getFullName());
        if(!teamB.getPlayerNames().contains(commonPlayer.getPlayer().getFullName())) {
            teamB.getTeamPlayers().add(commonPlayer);
            teamB.getPlayerNames().add(commonPlayer.getPlayer().getFullName());
        }
        System.out.println("Team B Players after adding common player: " + teamB.getPlayerNames());

        // add captain player to team b
        System.out.println("Adding Name to team b - " + teamB.getCaptainName());
        captainPlayer = Utils.getMatchPlayer(teamB.getCaptainName(), this.nameToIdMap, this.allPlayers);
        if(!teamB.getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
            teamB.getTeamPlayers().add(captainPlayer);
            teamB.getPlayerNames().add(captainPlayer.getPlayer().getFullName());
        }
        System.out.println("Team B Players after adding captain player: " + teamB.getPlayerNames());

        teamA.setTeamSize(teamA.getTeamPlayers().size());
        teamB.setTeamSize(teamB.getTeamPlayers().size());

        if(teamA.getTeamSize() != teamB.getTeamSize()) {
            Toast.makeText(this, "Team size needs to be same.", Toast.LENGTH_LONG).show();
            return;
        }

        teamA.setMaxWickets(teamA.getTeamSize() - 1);
        teamB.setMaxWickets(teamB.getTeamSize() - 1);

        System.out.println("Update match object.");
        this.match.setTeamA(teamA);
        this.match.setTeamB(teamB);

        Intent intent = new Intent(this, SelectTossWinningTeam.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", this.match);
        intent.putExtra("name_to_id_map", this.nameToIdMap);
        startActivity(intent);
    }
}