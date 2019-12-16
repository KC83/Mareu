package kelly.chiarotti.mareu.ui.meeting_form;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.Participant;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;

public class ParticipantFragment extends Fragment {
    private ParticipantFragmentListener mListener;
    private Meeting mMeeting;
    private Integer mMeetingPosition;
    private List<Participant> mParticipantList;

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
        View view = inflater.inflate(R.layout.fragment_participant, container, false);

        ApiService apiService = DI.getApiService();
        Context context = getContext();

        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autocomplete_participant);
        TextView textView = view.findViewById(R.id.text_view_participant);
        Button buttonBack = view.findViewById(R.id.btn_back);

        if (getArguments() != null) {
            Meeting meeting = new Gson().fromJson(getArguments().getString(Constants.EXTRA_MEETING), new TypeToken<Meeting>() {}.getType());

            if (meeting != null) {
                mMeeting = meeting;
                mMeetingPosition = getArguments().getInt(Constants.EXTRA_MEETING_POSITION);
            }
        }

        if (context != null) {
            ArrayList<Participant> participants = new ArrayList<>(apiService.getParticipants());
            ArrayAdapter<Participant> adapter = new ArrayAdapter<Participant>(context, android.R.layout.simple_dropdown_item_1line, participants);
            autoCompleteTextView.setAdapter(adapter);
        }

        textView.setText("TEST TEXTE");

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Participant> participants = new ArrayList<>(apiService.getParticipants());

                Participant selected = (Participant) parent.getAdapter().getItem(position);
                for (Participant participant : participants) {
                    if (participant.equals(selected)) {

                        Participant selectedParticipant = new Participant(participant.getId(), participant.getEmail(), participant.getNom());
                        //mParticipantList.add(selectedParticipant);
                        textView.setText(String.format("%s\n%s (%s)", textView.getText(), participant.getNom(), participant.getEmail()));
                        break;
                    }
                }
            }
        });

        buttonBack.setOnClickListener(v -> {
            Meeting newMeeting = new Meeting(mMeeting.getId(), mMeeting.getDate(), mMeeting.getTime(), mMeeting.getSubject(), mMeeting.getMeetingRoom(), null);
            mListener.onBackParticipantButton(newMeeting, mMeetingPosition);
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ParticipantFragment.ParticipantFragmentListener) {
            mListener = (ParticipantFragment.ParticipantFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement ParticipantFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
