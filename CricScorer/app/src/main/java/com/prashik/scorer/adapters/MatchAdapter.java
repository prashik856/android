package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder>{

    String[] dates;
    String[] teams;
    String[] statuses;
    String[] captains;
    String[] ids;
    String[] results;

    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_match, viewGroup, false);
        return new MatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(String.format("%s  |  %s  |  %s  \n%s\n%s",
                dates[position], teams[position], statuses[position], captains[position], results[position]));
        viewHolder.getTextView().setContentDescription(ids[position]);
    }

    @Override
    public int getItemCount() {
        return ids.length;
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

    public MatchAdapter(String[] datesArray, String[] teamsArray, String[] matchStatusArray,
                        String[] captainsArray, String[] idsArray, String[] resultsArray) {
        System.out.println("Creating match adapter object.");
        this.dates = datesArray;
        this.teams = teamsArray;
        this.statuses = matchStatusArray;
        this.captains = captainsArray;
        this.ids = idsArray;
        this.results = resultsArray;
    }
}
