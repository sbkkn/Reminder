package com.bakaikin.sergey.reminder.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakaikin.sergey.reminder.MyApplication;
import com.bakaikin.sergey.reminder.R;
import com.bakaikin.sergey.reminder.adapter.DoneTaskAdapter;
import com.bakaikin.sergey.reminder.database.DBHelper;
import com.bakaikin.sergey.reminder.model.ModelTask;
import com.bakaikin.sergey.reminder.model.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }

    OnTaskRestoreListner onTaskRestoreListner;

    public interface OnTaskRestoreListner {
        void onTaskRestore(Task task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskRestoreListner = (OnTaskRestoreListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "Must implement OnTaskRestoreListner ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

@Override
    public void findTasks(String title) {
        checkAdapter();
        adapter.removeAllItems();
        List<Task> tasks = new ArrayList<>();
//        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND "
//                + DBHelper.SELECTION_STATUS, new String[]{"%" + title + "%",
//                Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));

        tasks.addAll( ((MyApplication)getActivity().getApplication()).getDatabase().taskDao().getAll() );


    for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
 public void checkAdapter() {
        if (adapter == null) {
            adapter = new DoneTaskAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        checkAdapter();
        adapter.removeAllItems();
        List<Task> tasks = new ArrayList<>();
//        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS,
//                new String[]{Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));
        tasks.addAll( ((MyApplication)getActivity().getApplication()).getDatabase().taskDao().getAll() );

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
 }


    @Override
    public void addTask(Task newTask, boolean saveToDB) {
        int position = -1;
        checkAdapter();

        for (int i = 0; i < adapter.getItemCount(); i ++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.date < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            adapter.addItem(position, newTask);
        } else {
            adapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }
    }

    @Override
    public void moveTask(Task task) {
        if (task.date!=0)
        {
            alarmHelper.setAlarm(task);
        }
        onTaskRestoreListner.onTaskRestore(task);
    }
}
