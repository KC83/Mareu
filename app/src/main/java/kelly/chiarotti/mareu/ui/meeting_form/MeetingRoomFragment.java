package kelly.chiarotti.mareu.ui.meeting_form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.MeetingRoom;
import kelly.chiarotti.mareu.model.Participant;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;

public class MeetingRoomFragment extends Fragment {

    private ApiService mApiService;
    private MeetingRoomFragmentListener mListener;

    private Meeting mMeeting;
    private Integer mMeetingPosition;
    private MeetingRoom mMeetingRoom = null;
    private List<Participant> mParticipants = null;

    static MeetingRoomFragment newInstance(Meeting meeting, int position) {
        MeetingRoomFragment fragment = new MeetingRoomFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
        args.putInt(Constants.EXTRA_MEETING_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface MeetingRoomFragmentListener {
        void onBackMeetingRoomButton(Meeting meeting, int position);
        void onNextMeetingRoomButton(Meeting meeting, int position);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_room, container, false);

        mApiService = DI.getApiService();

        RadioGroup radioGroup = view.findViewById(R.id.radio_group_meeting_room);
        Button buttonBack = view.findViewById(R.id.btn_back);
        Button buttonNext = view.findViewById(R.id.btn_next);

        if (getArguments() != null) {
            Meeting meeting = new Gson().fromJson(getArguments().getString(Constants.EXTRA_MEETING), new TypeToken<Meeting>() {}.getType());

            if (meeting != null) {
                mMeeting = meeting;
                mMeetingPosition = getArguments().getInt(Constants.EXTRA_MEETING_POSITION);
                mMeetingRoom = mMeeting.getMeetingRoom();
                mParticipants = mMeeting.getParticipants();
            }
        }

        createRadioButton(radioGroup, mMeetingRoom);

        buttonBack.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() > 0) {
                mMeetingRoom = mApiService.getMeetingRooms().get(radioGroup.getCheckedRadioButtonId());
            }

            Meeting newMeeting = new Meeting(mMeeting.getId(), mMeeting.getDate(), mMeeting.getTime(), mMeeting.getSubject(), mMeetingRoom, mParticipants);
            mListener.onBackMeetingRoomButton(newMeeting, mMeetingPosition);
        });

        buttonNext.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() > 0) {
                mMeetingRoom = mApiService.getMeetingRooms().get(radioGroup.getCheckedRadioButtonId());

                Meeting newMeeting = new Meeting(mMeeting.getId(), mMeeting.getDate(), mMeeting.getTime(), mMeeting.getSubject(), mMeetingRoom, mParticipants);
                mListener.onNextMeetingRoomButton(newMeeting, mMeetingPosition);
            } else {
                Toast.makeText(getContext(), "Aucune salle de réunion n'est sélectionnée", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void createRadioButton(RadioGroup radioGroup, MeetingRoom meetingRoom) {
        Context context = getContext();
        ApiService apiService = DI.getApiService();
        List<MeetingRoom> meetingRoomList = apiService.getMeetingRooms();

        RadioButton[] radioButton = new RadioButton[meetingRoomList.size()];

        for(int i = 0; i < meetingRoomList.size(); i++) {
            radioButton[i] = new RadioButton(context);
            radioButton[i].setText(meetingRoomList.get(i).getName());
            radioButton[i].setId(i);

            if (meetingRoom != null && meetingRoom.equals(meetingRoomList.get(i))) {
                radioButton[i].setChecked(true);
            }

            radioGroup.addView(radioButton[i]);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MeetingRoomFragment.MeetingRoomFragmentListener) {
            mListener = (MeetingRoomFragment.MeetingRoomFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement MeetingRoomFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}


