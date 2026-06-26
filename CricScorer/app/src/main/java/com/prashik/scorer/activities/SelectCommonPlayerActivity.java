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

        // Add team a players
        for(int i=0; i<playerList.size(); i++) {
            String name = this.playersWithoutCaptain[playerList.get(i)];
            MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

            if(!this.match.getTeamA().getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                match.getTeamA().getTeamPlayers().add(matchPlayer);
                match.getTeamA().getPlayerNames().add(matchPlayer.getPlayer().getFullName());
            }
        }

        System.out.println("Team A Players after adding all normal players: " + this.match.getTeamA().getPlayerNames());

        // Add common player to team a
        MatchPlayer commonPlayer = Utils.getMatchPlayer(this.chosenAnswer, this.nameToIdMap, this.allPlayers);
        if(!this.match.getTeamA().getPlayerNames().contains(commonPlayer.getPlayer().getFullName())) {
            match.getTeamA().getTeamPlayers().add(commonPlayer);
            match.getTeamA().getPlayerNames().add(commonPlayer.getPlayer().getFullName());
        }
        System.out.println("Team A Players after adding common player: " + this.match.getTeamA().getPlayerNames());

        // Add captain player to team a
        MatchPlayer captainPlayer = Utils.getMatchPlayer(this.match.getTeamA().getCaptainName(), this.nameToIdMap, this.allPlayers);
        if(!this.match.getTeamA().getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
            match.getTeamA().getTeamPlayers().add(captainPlayer);
            match.getTeamA().getPlayerNames().add(captainPlayer.getPlayer().getFullName());
        }
        System.out.println("Team A Players after adding captain player: " + this.match.getTeamA().getPlayerNames());

        // Add players to team b
        for(int i=0; i<remainingPlayersList.size(); i++) {
            String name = this.matchPlayers[remainingPlayersList.get(i)];
            MatchPlayer matchPlayer = Utils.getMatchPlayer(name, this.nameToIdMap, this.allPlayers);

            if(!this.match.getTeamB().getPlayerNames().contains(matchPlayer.getPlayer().getFullName())) {
                match.getTeamB().getTeamPlayers().add(matchPlayer);
                match.getTeamB().getPlayerNames().add(matchPlayer.getPlayer().getFullName());
            }
        }
        System.out.println("Team B Players after adding normal players: " + this.match.getTeamB().getPlayerNames());

        // Add common player to team b
        if(!this.match.getTeamB().getPlayerNames().contains(commonPlayer.getPlayer().getFullName())) {
            match.getTeamB().getTeamPlayers().add(commonPlayer);
            match.getTeamB().getPlayerNames().add(commonPlayer.getPlayer().getFullName());
        }
        System.out.println("Team B Players after adding common player: " + this.match.getTeamB().getPlayerNames());

        // add captain player to team b
        captainPlayer = Utils.getMatchPlayer(this.match.getTeamB().getCaptainName(), this.nameToIdMap, this.allPlayers);
        if(!this.match.getTeamB().getPlayerNames().contains(captainPlayer.getPlayer().getFullName())) {
            match.getTeamB().getTeamPlayers().add(captainPlayer);
            match.getTeamB().getPlayerNames().add(captainPlayer.getPlayer().getFullName());
        }
        System.out.println("Team B Players after adding captain player: " + this.match.getTeamB().getPlayerNames());

        this.match.getTeamA().setTeamSize(this.match.getTeamA().getTeamPlayers().size());
        this.match.getTeamB().setTeamSize(this.match.getTeamB().getTeamPlayers().size());

        if(this.match.getTeamA().getTeamSize() != this.match.getTeamB().getTeamSize()) {
            Toast.makeText(this, "Team size needs to be same.", Toast.LENGTH_LONG).show();
            return;
        }

        this.match.getTeamA().setMaxWickets(this.match.getTeamA().getTeamSize() - 1);
        this.match.getTeamB().setMaxWickets(this.match.getTeamB().getTeamSize() - 1);

        Intent intent = new Intent(this, SelectTossWinningTeam.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", this.match);
        intent.putExtra("name_to_id_map", this.nameToIdMap);
        startActivity(intent);
    }
}