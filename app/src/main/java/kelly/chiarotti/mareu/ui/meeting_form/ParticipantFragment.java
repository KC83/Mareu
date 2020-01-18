package kelly.chiarotti.mareu.ui.meeting_form;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Participant> mParticipants;

    private ListParticipantAdapter mAdapter;
    private ArrayAdapter<Participant> mArrayAdapter;
    private ArrayList<Participant> mParticipantArrayList;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ApiService apiService = DI.getApiService();
        Context context = getContext();

        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autocomplete_participant);
        RecyclerView recyclerView = view.findViewById(R.id.item_list_participants);
        Button buttonBack = view.findViewById(R.id.btn_back_participant);
        Button buttonSave = view.findViewById(R.id.btn_form_save);

        mParticipants = new ArrayList<>();
        if (getArguments() != null) {
            Meeting meeting = new Gson().fromJson(getArguments().getString(Constants.EXTRA_MEETING), new TypeToken<Meeting>() {}.getType());

            if (meeting != null) {
                mMeeting = meeting;
                mMeetingPosition = getArguments().getInt(Constants.EXTRA_MEETING_POSITION);

                if (meeting.getParticipants() != null) {
                    mParticipants = meeting.getParticipants();
                }
            }
        }
        if (context != null) {
            mParticipantArrayList = new ArrayList<>(apiService.getParticipants());
            if (mParticipants.size() > 0) {
                for (Participant participant : mParticipants) {
                    mParticipantArrayList.remove(participant);
                }
            }

            mArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mParticipantArrayList);
            autoCompleteTextView.setAdapter(mArrayAdapter);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListParticipantAdapter(mParticipants, (participant, position) -> {
            mParticipants.remove(participant);
            mAdapter.notifyItemRemoved(position); // RELOAD THE LIST

            if (context != null) {
                // Update list of participants in autocomplete (add deleted participant in the autocomplete list)
                mParticipantArrayList.add(participant);
                mArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mParticipantArrayList);
                autoCompleteTextView.setAdapter(mArrayAdapter);
            }
        });
        recyclerView.setAdapter(mAdapter);

        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            Participant selected = (Participant) parent.getAdapter().getItem(position);
            for (Participant participant : mParticipantArrayList) {
                if (participant.equals(selected)) {
                    Participant selectedParticipant = new Participant(participant.getId(), participant.getEmail(), participant.getNom());
                    mParticipants.add(selectedParticipant);

                    mAdapter.notifyDataSetChanged();
                    autoCompleteTextView.setText("");

                    if (context != null) {
                        // Update list of participants in autocomplete (remove selected participant of autocomplete list)
                        mParticipantArrayList.remove(participant);
                        mArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mParticipantArrayList);
                        autoCompleteTextView.setAdapter(mArrayAdapter);
                    }

                    break;
                }
            }
        });

        buttonBack.setOnClickListener(v -> {
            Meeting newMeeting = new Meeting(mMeeting.getId(), mMeeting.getDate(), mMeeting.getTime(), mMeeting.getSubject(), mMeeting.getMeetingRoom(), mParticipants);
            mListener.onBackParticipantButton(newMeeting, mMeetingPosition);
        });

        buttonSave.setOnClickListener(v -> {
            Meeting newMeeting = new Meeting(mMeeting.getId(), mMeeting.getDate(), mMeeting.getTime(), mMeeting.getSubject(), mMeeting.getMeetingRoom(), mParticipants);
            mListener.onSaveMeeting(newMeeting,mMeetingPosition);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_participant, container, false);
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
