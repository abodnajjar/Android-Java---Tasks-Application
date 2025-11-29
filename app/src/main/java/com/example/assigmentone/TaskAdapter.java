package com.example.assigmentone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable  {

    private ArrayList<Task> tasks;
    private Activity activity;
    private ArrayList<Task> fullList;

    public TaskAdapter(Activity activity, ArrayList<Task> tasks) {
        this.activity = activity;
        this.tasks = tasks;
        this.fullList = new ArrayList<>(tasks);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText("Title :"+task.getTitle());
        holder.date.setText("Date :"+task.getDate());
        holder.priority.setText("Priority :"+String.valueOf(task.getPriority()));
        holder.state.setChecked(task.isState());
        holder.state.setEnabled(false);
        holder.state.setFocusable(false);
        if (task.isState()) {
            holder.tvDoneStatus.setText("Done");
        } else {
            holder.tvDoneStatus.setText("Not Done");
        }
        if (task.getImageID() != null) {
            holder.icon.setImageURI(Uri.parse(task.getImageID()));
        } else {
            holder.icon.setImageResource(R.drawable.task);
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TaskDetails.class);
            intent.putExtra("task", task);
            intent.putExtra("position", holder.getAdapterPosition());
            activity.startActivityForResult(intent, 200);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public void updateList(ArrayList<Task> newList) {
        this.tasks = newList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }
    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Task> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Task task : fullList) {
                    if (task.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(task);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tasks.clear();
            tasks.addAll((ArrayList<Task>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, tvDoneStatus,description, date, priority;
        CheckBox state;
        ImageView icon;
        CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            title = cardView.findViewById(R.id.tvTitle);
            date = cardView.findViewById(R.id.tvDate);
            priority = cardView.findViewById(R.id.tvPriority);
            state = cardView.findViewById(R.id.checkDone);
            tvDoneStatus=cardView.findViewById(R.id.tvDoneStatus);
            icon = cardView.findViewById(R.id.imgIcon);
        }
    }
}
