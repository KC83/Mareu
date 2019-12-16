package kelly.chiarotti.mareu.ui.meeting_form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;

public class FormMeetingActivity extends AppCompatActivity implements InformationFragment.InformationFragmentListener, MeetingRoomFragment.MeetingRoomFragmentListener, ParticipantFragment.ParticipantFragmentListener {

    InformationFragment mInformationFragment;
    MeetingRoomFragment mMeetingRoomFragment;
    ParticipantFragment mParticipantFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_meeting);

        // Get meeting (format Json) - can be null if the user click on the "new Meeting" button
        Intent formMeetingActivityIntent = getIntent();
        String extraMeeting = formMeetingActivityIntent.getStringExtra(Constants.EXTRA_MEETING);

        Meeting meeting;
        int meetingPosition;
        if (extraMeeting != null) {
            meeting = new Gson().fromJson(extraMeeting, new TypeToken<Meeting>() {}.getType());
            meetingPosition = formMeetingActivityIntent.getIntExtra(Constants.EXTRA_MEETING_POSITION, 0);
        } else {
            meeting = null;
            meetingPosition = 0;
        }

        mInformationFragment = InformationFragment.newInstance(meeting, meetingPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mInformationFragment)
                .commit();

    }

    @Override
    public void onNextInformationButton(Meeting meeting, int position) {
        mMeetingRoomFragment = MeetingRoomFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .remove(mInformationFragment)
                .replace(R.id.container, mMeetingRoomFragment)
                .commit();
    }

    @Override
    public void onBackMeetingRoomButton(Meeting meeting, int position) {
        mInformationFragment = InformationFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .remove(mMeetingRoomFragment)
                .replace(R.id.container, mInformationFragment)
                .commit();
    }

    @Override
    public void onNextMeetingRoomButton(Meeting meeting, int position) {
        mParticipantFragment = ParticipantFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .remove(mMeetingRoomFragment)
                .replace(R.id.container, mParticipantFragment)
                .commit();
    }

    @Override
    public void onBackParticipantButton(Meeting meeting, int position) {
        mMeetingRoomFragment = MeetingRoomFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .remove(mParticipantFragment)
                .replace(R.id.container, mMeetingRoomFragment)
                .commit();
    }

    @Override
    public void onSaveMeeting(Meeting meeting, int position) {
        ApiService apiService = DI.getApiService();
        apiService.addMeeting(meeting);
        Toast.makeText(this, "Réunion enregistrée !", Toast.LENGTH_SHORT).show();

        Intent listMeetingActivityIntent = new Intent();
        listMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING_POSITION,position);
        setResult(Activity.RESULT_OK,listMeetingActivityIntent);
        finish();
    }
}
