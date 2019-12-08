package kelly.chiarotti.mareu.ui.meeting_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;
import kelly.chiarotti.mareu.ui.meeting_form.FormMeetingActivity;

public class ListMeetingActivity extends AppCompatActivity implements View.OnClickListener, ListMeetingAdapter.ListMeetingListener {

    private final ApiService mApiService = DI.getApiService();
    private ListMeetingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);

        List<Meeting> meetings = DI.getApiService().getMeetings();

        /*ListMeetingAdapter.ListMeetingListener listener = new ListMeetingAdapter.ListMeetingListener() {
            @Override
            public void onClick(Meeting meeting, int position) {
                Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
                formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
                formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING_POSITION, position);
                ListMeetingActivity.this.startActivity(formMeetingActivityIntent);
            }

            @Override
            public void onDelete(Meeting meeting, int position) {
                mApiService.deleteMeeting(meeting);
                adapter.notifyItemRemoved(position); // RELOAD THE LIST
            }
        };*/

        adapter = new ListMeetingAdapter(meetings, this);

        final RecyclerView mRecyclerView = findViewById(R.id.activity_list_meeting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        startActivity(formMeetingActivityIntent);
    }

    @Override
    public void onClick(Meeting meeting, int position) {
        Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
        formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING_POSITION, position);
        ListMeetingActivity.this.startActivity(formMeetingActivityIntent);
    }

    @Override
    public void onDelete(Meeting meeting, int position) {
        mApiService.deleteMeeting(meeting);
        adapter.notifyItemRemoved(position); // RELOAD THE LIST
    }
}
