package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;

public class MatchActivityAdapter extends RecyclerView.Adapter<MatchActivityAdapter.ViewHolder>{
    String[] matchActivities;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_match_activity, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(String.format("%s", matchActivities[position]));
    }

    @Override
    public int getItemCount() {
        return matchActivities.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.single_match_activity_item);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public MatchActivityAdapter(String[] activitiesArray) {
        this.matchActivities = activitiesArray;
    }
}
