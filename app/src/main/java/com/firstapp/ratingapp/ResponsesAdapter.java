package com.firstapp.ratingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResponsesAdapter extends RecyclerView.Adapter<ResponsesAdapter.ResponseViewHolder> {
    private List<String> responseList;

    public ResponsesAdapter(List<String> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_item, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        String response = responseList.get(position);
        holder.responseTextView.setText(response);
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {
        TextView responseTextView;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
        }
    }
}
