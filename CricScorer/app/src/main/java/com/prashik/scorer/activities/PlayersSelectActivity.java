package com.prashik.scorer.activities;

import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;

public class PlayersSelectActivity extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    HashMap<String, Player> allPlayers;
    HashMap<String, BattingStats> allBattingStats;
    HashMap<String, BowlingStats> allBowlingStats;
    HashMap<String, MatchStats> allMatchesStats;

    Match match;

    boolean[] selectedPlayers;

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
        this.match = (Match) getIntent().getSerializableExtra("match_object");

        TextView textView = findViewById(R.id.select_playing_players);
        selectedPlayers = new boolean[allPlayers.size()];

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayersSelectActivity.this);
                builder.setTitle("Select Playing Players");
                builder.setCancelable(false);
//                builder.setMultiChoiceItems()
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}