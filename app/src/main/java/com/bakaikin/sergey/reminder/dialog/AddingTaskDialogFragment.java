package com.bakaikin.sergey.reminder.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.bakaikin.sergey.reminder.R;
import com.bakaikin.sergey.reminder.alarm.AlarmHelper;
import com.bakaikin.sergey.reminder.model.ModelTask;
import com.bakaikin.sergey.reminder.model.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.widget.RxAdapterView;

import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Sergey on 19.09.2015.
 */
public class AddingTaskDialogFragment extends DialogFragment {

    @BindView(R.id.tilDialogTaskTitle)
    TextInputLayout tilDialogTaskTitle;
    @BindView(R.id.tilDialogTaskDate)
    TextInputLayout tilDialogTaskDate;
    @BindView(R.id.tilDialogTaskTime)
    TextInputLayout tilDialogTaskTime;
    @BindView(R.id.spDialogTaskPriority)
    AppCompatSpinner spDialogTaskPriority;
    private Unbinder unbinder;

    public interface AddingTaskListener {
        void onTaskAdded(Task newTask);

        void onTaskAddingCancel();
    }

    private AddingTaskListener addingTaskListener;
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addingTaskListener = (AddingTaskListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingTaskListener");
        }
    }

    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        timePickerFragment = new TimePickerFragment();
        datePickerFragment = new DatePickerFragment();


        builder.setTitle(R.string.dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);

        unbinder = ButterKnife.bind(this, container);
        final EditText etTitle = tilDialogTaskTitle.getEditText();
        final EditText etDate = tilDialogTaskDate.getEditText();
        final EditText etTime = tilDialogTaskTime.getEditText();

        timePickerFragment.setEditText(etTime);
        datePickerFragment.setEditText(etDate);

        tilDialogTaskTitle.setHint(getResources().getString(R.string.task_title));
        tilDialogTaskDate.setHint(getResources().getString(R.string.task_date));
        tilDialogTaskTime.setHint(getResources().getString(R.string.task_time));

        builder.setView(container);

        final Task task = new Task();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.priority_levels));

        spDialogTaskPriority.setAdapter(priorityAdapter);
        RxAdapterView.itemSelections(spDialogTaskPriority)
                .subscribeOn(AndroidSchedulers.mainThread()) // is it needded?
                .subscribe(position -> {
                    Log.v("spinner", position.toString());
                    task.priority = position;
                });


//        spDialogTaskPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                task.setPriority(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);


//        RxTextView.textChanges(etDate)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe((text) -> {
//                    Log.v("etDate", text.toString());
//                    if (etDate.length() == 0) {
//                        etDate.setText(" ");
//                    }
//                    datePickerFragment.show(getFragmentManager(), "DatePickerFragment"); //causes : Fragment already added ???
//                });

        etDate.setOnClickListener(v -> {
            if (etDate.length() == 0) {
                etDate.setText(" ");
            }

            datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
        });

//        RxTextView.textChanges(etTime)
//                .subscribe((text) -> {
//                    Log.v("etTime", text.toString());
//                    if (etTime.length() == 0) {
//                        etTime.setText(" ");
//                    }
//                    timePickerFragment.show(getFragmentManager(), "TimePickerFragment"); //causes : Fragment already added ???
//
//                });

        etTime.setOnClickListener(v -> {
            if (etTime.length() == 0) {
                etTime.setText(" ");
            }
            timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
        });

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            task.title = etTitle.getText().toString();
            task.status = Task.STATUS_CURRENT;
            if (etDate.length() != 0 || etTime.length() != 0) {
                task.date = calendar.getTimeInMillis();

                AlarmHelper alarmHelper = AlarmHelper.getInstance();
                alarmHelper.setAlarm(task);
            }
            task.status = Task.STATUS_CURRENT;
            addingTaskListener.onTaskAdded(task);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilDialogTaskTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilDialogTaskTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
                            positiveButton.setEnabled(true);
                            tilDialogTaskTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
