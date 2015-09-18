package com.bakaikin.sergey.reminder.adapter;

import android.support.v7.widget.RecyclerView;

import com.bakaikin.sergey.reminder.fragment.TaskFragment;
import com.bakaikin.sergey.reminder.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 18.09.2015.
 */
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Item> items;
    TaskFragment taskFragment;
    public TaskAdapter(TaskFragment taskFragment){
        this.taskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int location){
        if (location>=0 && location <= getItemCount()-1);
        items.remove(location);
        notifyItemRemoved(location);
    }

}
