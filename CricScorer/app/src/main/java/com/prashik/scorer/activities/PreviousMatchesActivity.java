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
import com.prashik.scorer.adapters.MatchAdapter;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class PreviousMatchesActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    String filesDirectory;
    MatchAdapter matchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_previous_matches);
        System.out.println("In previous matches activity");

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        assert this.dataFilesMap != null;
        this.filesDirectory = this.dataFilesMap.get("files_directory");
        System.out.println("Files directory: " + this.filesDirectory);
        ArrayList<String> matchFiles = Utils.getMatchFiles(Utils.getAllFilesInDirectory(this.filesDirectory));
        System.out.println("All Match Files: " + matchFiles);

        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> teams = new ArrayList<>();
        ArrayList<String> statuses = new ArrayList<>();
        ArrayList<String> captains = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        for(String matchfile: matchFiles) {
            String fileToRead = this.filesDirectory + "/" + matchfile;
            System.out.println("File to read: " + fileToRead);
            Match match = Utils.readMatchFile(fileToRead);
            System.out.println("Current Match Object: " + match.toString());

            String date = match.getDate();
            String team = String.format("%s VS %s", match.getTeamA().getName(), match.getTeamB().getName());
            String status = "Incomplete";
            if(match.isCompleted()) {
                status = "Completed";
            }
            String captain = String.format("%s(C), %s(C)", match.getTeamA().getCaptainName(), match.getTeamB().getCaptainName());
            dates.add(date);
            teams.add(team);
            statuses.add(status);
            captains.add(captain);
            fileNames.add(matchfile);
        }
        System.out.println("Dates: " + dates);
        System.out.println("Teams: " + teams);
        System.out.println("Status: " + statuses);
        System.out.println("Captains: " + captains);
        System.out.println("FileNames: " + fileNames);

        this.matchAdapter = new MatchAdapter(dates.toArray(new String[0]),
                teams.toArray(new String[0]),
                statuses.toArray(new String[0]),
                captains.toArray(new String[0]),
                fileNames.toArray(new String[0]));
        System.out.println("Getting recycler view.");
        RecyclerView recyclerView = findViewById(R.id.all_matches_list);
        System.out.println("Setting layout manager.");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Setting adapter.");
        recyclerView.setAdapter(this.matchAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleOnMatchClick(View view) {
        // read match file
        System.out.println("Clicked on match.");
        TextView textView = (TextView) view;
        String matchFile = textView.getContentDescription().toString();
        System.out.println("Match file: " + matchFile);
        String matchFileLocation = this.filesDirectory + "/" + matchFile;
        System.out.println("Match file location: " + matchFileLocation);
        Match match = Utils.readMatchFile(matchFileLocation);

        // push match object
        Intent intent = new Intent(this, MatchInformationActivity.class);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("match_object", match);
        startActivity(intent);
    }
}