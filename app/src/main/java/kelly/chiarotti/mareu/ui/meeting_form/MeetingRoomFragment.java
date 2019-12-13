package kelly.chiarotti.mareu.ui.meeting_form;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.service.Constants;

public class MeetingRoomFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting_room, container, false);
    }
}


