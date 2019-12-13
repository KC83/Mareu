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

public class ParticipantFragment extends Fragment {
    static ParticipantFragment newInstance(Meeting meeting, int position) {
        ParticipantFragment fragment = new ParticipantFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
        args.putInt(Constants.EXTRA_MEETING_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ParticipantFragmentListener {
        void onBackParticipantButton(Meeting meeting, int position);
        void onSaveMeeting(Meeting meeting, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_participant, container, false);
    }
}
