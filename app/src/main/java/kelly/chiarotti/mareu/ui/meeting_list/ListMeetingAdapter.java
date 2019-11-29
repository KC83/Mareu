package kelly.chiarotti.mareu.ui.meeting_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.Participant;
import kelly.chiarotti.mareu.service.ApiService;

public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder> {

    private List<Meeting> mMeetings;

    public ListMeetingAdapter() {
        super();
        ApiService apiService = DI.getApiService();
        mMeetings = apiService.getMeetings();
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = mMeetings.get(position);

        holder.mColor.setBackgroundColor(meeting.getMeetingRoom().getColor());
        holder.mText.setText(meeting.getSubject());

        String participants = "";
        List<Participant> participantsList = meeting.getParticipants();

        int i = 0;

        for (Participant participant : participantsList) {
            i++;

            participants += participant.getEmail();
            if (i != participantsList.size()) {
                participants += ",";
            }
        }
        holder.mParticipants.setText(participants);


        holder.mDeleteButton.setOnClickListener(v -> {
            ApiService apiService = DI.getApiService();
            apiService.deleteMeeting(meeting);

            System.out.println("Delete : "+meeting.getSubject());
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_color)
        public ImageView mColor;
        @BindView(R.id.item_list_text)
        public TextView mText;
        @BindView(R.id.item_list_participants)
        public TextView mParticipants;
        @BindView(R.id.item_list_delete_button)
        public ImageView mDeleteButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
