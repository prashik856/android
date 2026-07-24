package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    HashMap<String, Player> allPlayers;
    HashMap<String, MatchStats> allMatchStats;
    HashMap<String, String> nameToIdMap;
    String[] allPlayerNames;
    String[] allPlayersIds;
    int[] allPlayersMatchesPlayed;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(String.format("Name: %s | Matches Played: %d",
                allPlayerNames[position],
                allPlayersMatchesPlayed[position]));
        viewHolder.getTextView().setContentDescription(allPlayersIds[position]);
    }

    @Override
    public int getItemCount() {
        return allPlayerNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.single_player_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public PlayersAdapter(HashMap<String, Player> allPlayers,
                          HashMap<String, MatchStats> allMatchStats,
                          HashMap<String, String> nameToIdMapObject) {
        this.allPlayers = allPlayers;
        this.allMatchStats = allMatchStats;
        this.nameToIdMap = nameToIdMapObject;
        allPlayerNames = new String[allPlayers.size()];
        allPlayersMatchesPlayed = new int[allMatchStats.size()];
        allPlayersIds = new String[allPlayers.size()];

        // Create all player names array and sort them
        int i=0;
        for(String key: this.allPlayers.keySet()) {
            allPlayerNames[i] = Objects.requireNonNull(allPlayers.get(key)).getFullName();
            i++;
        }
        Arrays.sort(allPlayerNames);
        System.out.println("All player names: " + Arrays.toString(allPlayerNames));

        System.out.println("All Match Stats: " + this.allMatchStats.toString());
        System.out.println("Names to Id Map: " + this.nameToIdMap.toString());

        i=0;
        for(String key: allPlayerNames) {
            String id = this.nameToIdMap.get(key);
            System.out.println("player id: " + id);
            allPlayersMatchesPlayed[i] = allMatchStats.get(id).getMatchesPlayed();
                    ;
            allPlayersIds[i] = id;
            i++;
        }
    }
}
