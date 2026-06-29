package com.prashik.scorer.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class PreviousMatchesActivity extends AppCompatActivity {

    HashMap<String, String> dataFilesMap;
    String filesDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_previous_matches);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        assert this.dataFilesMap != null;
        this.filesDirectory = this.dataFilesMap.get("files_directory");
        ArrayList<String> matchFiles = Utils.getMatchFiles(Utils.getAllFilesInDirectory(this.filesDirectory));
        System.out.println("All Match Files: " + matchFiles);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}