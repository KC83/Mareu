package kelly.chiarotti.mareu.ui.meeting_form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    String mFragment = "InformationFragment";

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

        if (savedInstanceState != null) {
            mFragment = savedInstanceState.getString(Constants.EXTRA_FORM_MEETING_FRAGMENT);
        }

        if (mFragment != null) {
            switch (mFragment) {
                case Constants.NAME_INFORMATION_FRAGMENT:
                    mInformationFragment = InformationFragment.newInstance(meeting, meetingPosition);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, mInformationFragment)
                            .commit();
                    break;
                case Constants.NAME_MEETING_ROOM_FRAGMENT:
                    mMeetingRoomFragment = MeetingRoomFragment.newInstance(meeting, meetingPosition);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, mMeetingRoomFragment)
                            .commit();
                    break;
                case Constants.NAME_PARTICIPANT_FRAGMENT:
                    mParticipantFragment = ParticipantFragment.newInstance(meeting, meetingPosition);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, mParticipantFragment)
                            .commit();
                    break;
            }
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constants.EXTRA_FORM_MEETING_FRAGMENT, mFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNextInformationButton(Meeting meeting, int position) {
        mFragment = Constants.NAME_MEETING_ROOM_FRAGMENT;

        mMeetingRoomFragment = MeetingRoomFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mMeetingRoomFragment)
                .commit();
    }

    @Override
    public void onBackMeetingRoomButton(Meeting meeting, int position) {
        mFragment = Constants.NAME_INFORMATION_FRAGMENT;

        mInformationFragment = InformationFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mInformationFragment)
                .commit();
    }

    @Override
    public void onNextMeetingRoomButton(Meeting meeting, int position) {
        mFragment = Constants.NAME_PARTICIPANT_FRAGMENT;

        mParticipantFragment = ParticipantFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mParticipantFragment)
                .commit();
    }

    @Override
    public void onBackParticipantButton(Meeting meeting, int position) {
        mFragment = Constants.NAME_MEETING_ROOM_FRAGMENT;

        mMeetingRoomFragment = MeetingRoomFragment.newInstance(meeting, position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mMeetingRoomFragment)
                .commit();
    }

    @Override
    public void onSaveMeeting(Meeting meeting, int position) {
        ApiService apiService = DI.getApiService();

        if (apiService.getMeetings().size() > position) {
            apiService.updateMeeting(meeting, position);
        } else {
            apiService.addMeeting(meeting);
        }

        Toast.makeText(this, "Réunion enregistrée !", Toast.LENGTH_SHORT).show();

        Intent listMeetingActivityIntent = new Intent();
        setResult(Activity.RESULT_OK,listMeetingActivityIntent);
        finish();
    }
}
