package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.HashMap;

public class MatchScoreActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    Match match;
    HashMap<String, String> nameToIdMap;
    HashMap<String, Player> allPlayers;
    String filesDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_score);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.filesDirectory = this.dataFilesMap.get("files_directory");

        // Check if this match already exists
        boolean alreadyExists = Utils.isMatchAlreadyExists(this.filesDirectory, this.match);
        if(alreadyExists) {
            Toast.makeText(this, "The match with similar details already exists. Please edit the existing match or delete that match first.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            String matchDataFile = this.match.getDataFileName(this.filesDirectory);
            System.out.println("Match Data File Name: " + matchDataFile);
            // match file name is always going to be unique due to uuid
            Utils.createFile(matchDataFile);
        }

        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            if (s.equals("players_data_file_location")) {
                this.allPlayers = Utils.readPlayersFile(dataFile);
            }
            if (s.equals("players_name_to_id_map_file_location")) {
                this.nameToIdMap = Utils.readNameToIdMapFile(dataFile);
            }
        }

        System.out.println("Match Object: ");
        System.out.println(this.match);

        // With these properties, we will now create a new file and store our match object in it.
        TextView teamNameText = findViewById(R.id.team_name_ms);
        TextView runsAndWicketsText = findViewById(R.id.score_value_ms);
        TextView oversText = findViewById(R.id.overs_value_ms);
        TextView runRateText = findViewById(R.id.run_rate_ms);
        String name = "";
        float runRate = 0;
        String score = "";
        String overs = "";
        String overDetails = "";
        if(this.match.isTeamABatFirst()) {
            name = this.match.getTeamA().getName();
            score = Utils.getScore(this.match.getTeamA());
            overs = Utils.getOvers(this.match.getTeamA(), this.match);
            overDetails = Utils.getOverDetails(this.match.getTeamA());
        } else {
            name = this.match.getTeamB().getName();
            score = Utils.getScore(this.match.getTeamB());
            overs = Utils.getOvers(this.match.getTeamB(), this.match);
            overDetails = Utils.getOverDetails(this.match.getTeamB());
        }
        teamNameText.setText(name);
        runsAndWicketsText.setText(score);
        System.out.println("Score value: " + score);
        oversText.setText(overs);
        System.out.println("Overs value: " + overs);
        runRateText.setText(String.format("RR: %.2f", runRate));

        TextView strikerName = findViewById(R.id.striker_name_ms);
        String[] strikePlayerSplit = this.match.getStrikerBatsman().split(" ");
        String strikerNameToDisplay = strikePlayerSplit[0] + " " + "0(0)";
        strikerName.setText(strikerNameToDisplay);

        String[] nonStrikePlayerSplit = this.match.getNonStrikeBatsman().split(" ");
        String nonStrikerNameToDisplay = nonStrikePlayerSplit[0] + " " + "0(0)";
        TextView nonStrikerName = findViewById(R.id.non_striker_name_ms);
        nonStrikerName.setText(nonStrikerNameToDisplay);

        TextView bowlerNameText = findViewById(R.id.current_bowler_ms);
        String temp = "Bowler Name\n" + this.match.getCurrentBowler().split(" ")[0];
        bowlerNameText.setText(temp);

        TextView overDetailsText = findViewById(R.id.current_over_details_ms);
        overDetailsText.setText(overDetails);

        Utils.syncMatchData(this.match.getDataFileName(this.filesDirectory), this.match);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleHomeClick(View view) {
        Utils.syncMatchData(this.match.getDataFileName(this.filesDirectory), this.match);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}