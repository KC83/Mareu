package kelly.chiarotti.mareu.ui.meeting_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.MeetingRoom;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;
import kelly.chiarotti.mareu.ui.meeting_form.FormMeetingActivity;

public class ListMeetingActivity extends AppCompatActivity implements View.OnClickListener, ListMeetingAdapter.ListMeetingListener {

    private final ApiService mApiService = DI.getNewInstanceApiService();
    private ListMeetingAdapter mAdapter;
    private List<MeetingRoom> mMeetingRooms;
    private Calendar mCalendar = Calendar.getInstance();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);

        mMeetingRooms = new ArrayList<>();
        mCalendar.setTime(new Date());
        mRecyclerView = findViewById(R.id.activity_list_meeting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<Meeting> meetings = mApiService.getMeetings();
        mAdapter = new ListMeetingAdapter(meetings, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_date:
                filterMeetingByDate();
                return true;
            case R.id.filter_meeting_room:
                filterMeetingByMeetingRoom();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CODE_REQUEST_FORM_MEETING) {
            if (resultCode == Activity.RESULT_OK) {
                initRecyclerView();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // Button new meeting
        Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        ListMeetingActivity.this.startActivityForResult(formMeetingActivityIntent, Constants.CODE_REQUEST_FORM_MEETING);
    }

    @Override
    public void onClick(Meeting meeting, int position) {
        // onClick on one of the meeting
        Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
        formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING_POSITION, position);
        ListMeetingActivity.this.startActivityForResult(formMeetingActivityIntent, Constants.CODE_REQUEST_FORM_MEETING);
    }

    @Override
    public void onDelete(Meeting meeting, int position) {
        // onDelete on one of the meeting
        mApiService.deleteMeeting(meeting);
        mAdapter.notifyItemRemoved(position); // RELOAD THE LIST
    }

    public void filterMeetingByDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DatePicker picker = new DatePicker(this);

        picker.updateDate(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        builder.setView(picker);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = picker.getMonth() + 1;
                String date = String.format(Locale.FRANCE,"%02d", picker.getDayOfMonth())+"/"+String.format(Locale.FRANCE,"%02d", month)+"/"+picker.getYear();

                List<Meeting> meetings = mApiService.getMeetingsByDate(date);
                mAdapter = new ListMeetingAdapter(meetings, ListMeetingActivity.this);
                mCalendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

                final RecyclerView mRecyclerView = findViewById(R.id.activity_list_meeting);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ListMeetingActivity.this));
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        builder.setNegativeButton("Annuler", null);

        builder.create();
        builder.show();
    }

    public void filterMeetingByMeetingRoom() {
        CharSequence[] meetingRooms = new CharSequence[mApiService.getMeetingRooms().size()];
        boolean[] meetingRoomsChecked = new boolean[mApiService.getMeetingRooms().size()];

        for (int i = 0; i < mApiService.getMeetingRooms().size(); i++) {
            meetingRooms[i] = mApiService.getMeetingRooms().get(i).getName();
            meetingRoomsChecked[i] = mMeetingRooms.contains(mApiService.getMeetingRooms().get(i));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrer pas salle de réunion");
        builder.setMultiChoiceItems(meetingRooms, meetingRoomsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                MeetingRoom meetingRoom = mApiService.getMeetingRooms().get(which);

                if (isChecked) {
                    mMeetingRooms.add(meetingRoom);
                } else {
                    mMeetingRooms.remove(meetingRoom);
                }

                List<Meeting> meetings = mApiService.getMeetingsByMeetingRoom(mMeetingRooms);
                mAdapter = new ListMeetingAdapter(meetings, ListMeetingActivity.this);

                final RecyclerView mRecyclerView = findViewById(R.id.activity_list_meeting);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ListMeetingActivity.this));
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        builder.setNegativeButton("FERMER", null);

        builder.create();
        builder.show();
    }
}
