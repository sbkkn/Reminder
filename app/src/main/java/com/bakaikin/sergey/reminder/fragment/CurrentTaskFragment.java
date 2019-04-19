package com.bakaikin.sergey.reminder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakaikin.sergey.reminder.MyApplication;
import com.bakaikin.sergey.reminder.R;
import com.bakaikin.sergey.reminder.adapter.CurrentTasksAdapter;
import com.bakaikin.sergey.reminder.database.DBHelper;
import com.bakaikin.sergey.reminder.model.ModelSeparator;
import com.bakaikin.sergey.reminder.model.ModelTask;
import com.bakaikin.sergey.reminder.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class CurrentTaskFragment extends TaskFragment {

    @BindView(R.id.rvCurentTasks)
    RecyclerView rvCurentTasks;
    private OnTaskDoneListner onTaskDoneListner;

    @OnClick(R.id.rvCurentTasks)
    public void onViewClicked() {
    }

    public interface OnTaskDoneListner {
        void onTaskDone(Task task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskDoneListner = (OnTaskDoneListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnTaskDoneListner");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurentTasks);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CurrentTasksAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void findTasks(String title) {
        checkAdapter();
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        List<Task> tasksList = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND "
                        + DBHelper.SELECTION_STATUS + " OR " + DBHelper.SELECTION_STATUS,
                new String[]{"%" + title + "%", Integer.toString(ModelTask.STATUS_CURRENT),
                        Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        tasksList.addAll((((MyApplication)getActivity().getApplication()).getDatabase()).taskDao().getAll());
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasksList.get(i), false);
        }
    }

    @Override
    public void checkAdapter() {
        if (adapter == null) {
            adapter = new CurrentTasksAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        checkAdapter();
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();

        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        taskList.addAll((((MyApplication)getActivity().getApplication()).getDatabase()).taskDao().getAll());

        for (int i = 0; i < tasks.size(); i++) {
//            addTask(tasks.get(i), false);
            addTask(taskList.get(i), false);
        }
    }

    @Override
    public void addTask(Task newTask, boolean saveToDB) {
        int position = -1;
        ModelSeparator separator = null;
        checkAdapter();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.date < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (newTask.date != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.date);

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.status = ModelSeparator.TYPE_OVERDUE;
                if (!adapter.containsSeparatorOverdue) {
                    adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.dateStatus = ModelSeparator.TYPE_TODAY;
                if (!adapter.containsSeparatorToday) {
                    adapter.containsSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_TOMORROW;
                if (!adapter.containsSeparatorTomorrow) {
                    adapter.containsSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_TOMORROW;
                if (!adapter.containsSeparatorFuture) {
                    adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        }


        if (position != -1) {

            if (!adapter.getItem(position - 1).isTask()) {
                if (position - 2 >= 0 && adapter.getItem(position - 2).isTask()) {
                    ModelTask task = (ModelTask) adapter.getItem(position - 2);
                    if (task.getDateStatus() == newTask.dateStatus) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.date == 0) {
                    position -= 1;
                }
            }

            if (separator != null) {
                adapter.addItem(position - 1, separator);
            }

            adapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                adapter.addItem(separator);
            }
            adapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }


    }

    @Override
    public void moveTask(Task task) {
        alarmHelper.removeAlarm(task.timeStamp);
        onTaskDoneListner.onTaskDone(task);
    }
}
