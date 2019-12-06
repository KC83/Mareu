package kelly.chiarotti.mareu.ui.meeting_form;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;
import kelly.chiarotti.mareu.ui.meeting_list.ListMeetingActivity;

public class FormMeetingActivity extends AppCompatActivity {

    private ApiService mApiService;
    private Meeting mMeeting;
    private Integer mMeetingPosition;

    private EditText mDate;
    private EditText mTime;
    private EditText mSubject;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_meeting);

        // Get ApiService
        mApiService = DI.getApiService();

        // Init layout id
        mDate = findViewById(R.id.item_form_date);
        mDate.setInputType(InputType.TYPE_NULL);

        mTime = findViewById(R.id.item_form_time);
        mTime.setInputType(InputType.TYPE_NULL);

        mSubject = findViewById(R.id.item_form_subject);
        Button btnSave = findViewById(R.id.btn_form_save);

        // Get meeting (format Json) - can be null if the user click on the "new Meeting" button
        Intent formMeetingActivityIntent = getIntent();
        String extraMeeting = formMeetingActivityIntent.getStringExtra(Constants.EXTRA_MEETING);

        // Set information if not new meeting
        if (extraMeeting != null) {
            mMeeting = new Gson().fromJson(extraMeeting, new TypeToken<Meeting>(){}.getType());
            mMeetingPosition = formMeetingActivityIntent.getIntExtra(Constants.EXTRA_MEETING_POSITION, 0);

            mDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(mMeeting.getDate()));
            mTime.setText(new SimpleDateFormat("HH:mm").format(mMeeting.getTime()));
            mSubject.setText(mMeeting.getSubject());
        }

        // DATEPICKER
        mDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            @SuppressLint("SetTextI18n")
            DatePickerDialog picker = new DatePickerDialog(FormMeetingActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> mDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        // TIMEPICKER
        mTime.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            @SuppressLint("SetTextI18n") TimePickerDialog picker = new TimePickerDialog(FormMeetingActivity.this,
                    (tp, sHour, sMinute) -> mTime.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
        });

        // Action on save button
        btnSave.setOnClickListener(v -> {
            saveMeeting();
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void saveMeeting() {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(mDate.getText().toString());
            Date time = new SimpleDateFormat("HH:mm").parse(mTime.getText().toString());

            Meeting newMeeting = new Meeting(null, null, null, null,null,null);
            if (mMeeting != null) {
                newMeeting.setId(mMeeting.getId());
                newMeeting.setDate(date);
                newMeeting.setTime(time);
                newMeeting.setSubject(mSubject.getText().toString());
                newMeeting.setMeetingRoom(mMeeting.getMeetingRoom());
                newMeeting.setParticipants(mMeeting.getParticipants());

                mApiService.deleteMeeting(mMeeting);
            } else {
                int id = mApiService.getMeetings().size()+1;
                newMeeting.setId(id);
                newMeeting.setDate(date);
                newMeeting.setTime(time);
                newMeeting.setSubject(mSubject.getText().toString());
            }


            if (mMeeting != null) {
                //System.out.println(mApiService.getMeetings());
                //mApiService.deleteMeeting(mMeetingPosition);
                //System.out.println(mApiService.getMeetings());
            }


            mApiService.addMeeting(newMeeting);
            Toast.makeText(this, "Réunion enregistrée !", Toast.LENGTH_SHORT).show();

            Intent formMeetingActivityIntent = new Intent(FormMeetingActivity.this, ListMeetingActivity.class);
            startActivity(formMeetingActivityIntent);
        } catch (ParseException e) {
            Toast.makeText(this, "Erreur lors de la récupération de la date !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
