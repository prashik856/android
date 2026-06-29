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

import java.util.HashMap;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    HashMap<String, Player> allPlayers;
    HashMap<String, MatchStats> allMatchStats;

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
//        System.out.println("All Players position value: " + allPlayerNames[position]);
//        System.out.println("View holder value: " + viewHolder);
//        System.out.println("Text View value: " + viewHolder.getTextView());
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
            textView = view.findViewById(R.id.single_match_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public PlayersAdapter(HashMap<String, Player> allPlayers, HashMap<String, MatchStats> allMatchStats) {
        this.allPlayers = allPlayers;
        this.allMatchStats = allMatchStats;
        allPlayerNames = new String[allPlayers.size()];
        allPlayersMatchesPlayed = new int[allMatchStats.size()];
        allPlayersIds = new String[allPlayers.size()];
        int i=0;
        for(String key: this.allPlayers.keySet()) {
            allPlayerNames[i] = allPlayers.get(key).getFirstName() + " " + allPlayers.get(key).getLastName();
            allPlayersMatchesPlayed[i] = allMatchStats.get(key).getMatchesPlayed();
            allPlayersIds[i] = key;
            i++;
        }
    }
}
