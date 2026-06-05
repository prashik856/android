package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.adapters.PlayersAdapter;
import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class PlayersActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;
    PlayersAdapter playersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_players);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.allPlayers = (HashMap<String, Player>) getIntent().getSerializableExtra("all_players_hashmap");
        this.allBattingStats = (HashMap<String, BattingStats>) getIntent().getSerializableExtra("all_batting_stats_hashmap");
        this.allBowlingStats = (HashMap<String, BowlingStats>) getIntent().getSerializableExtra("all_bowling_stats_hashmap");
        this.allMatchesStats = (HashMap<String, MatchStats>) getIntent().getSerializableExtra("all_matches_stats_hashmap");

        this.playersAdapter = new PlayersAdapter(this.allPlayers, this.allMatchesStats);
        RecyclerView recyclerView = findViewById(R.id.all_players_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playersAdapter);

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
}