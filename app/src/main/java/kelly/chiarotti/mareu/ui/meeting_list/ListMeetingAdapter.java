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
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.Participant;

public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder> {

    private List<Meeting> mMeetings;
    private ListMeetingListener mListMeetingListener;

    interface ListMeetingListener {
        void onClick(Meeting meeting, int position);
        void onDelete(Meeting meeting, int position);
    }

    ListMeetingAdapter(List<Meeting> items, ListMeetingListener listener) {
        super();
        mMeetings = items;
        mListMeetingListener = listener;
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
        Meeting meeting = mMeetings.get(holder.getAdapterPosition());

        //holder.mColor.setBackgroundColor(meeting.getMeetingRoom().getColor());
        holder.mColor.setColorFilter(meeting.getMeetingRoom().getColor());
        holder.mText.setText(meeting.getSubject());

        StringBuilder participants = new StringBuilder();
        List<Participant> participantsList = meeting.getParticipants();

        int i = 0;
        for (Participant participant : participantsList) {
            i++;

            participants.append(participant.getEmail());
            if (i != participantsList.size()) {
                participants.append(",");
            }
        }
        holder.mParticipants.setText(participants.toString());


        // Action to update the meeting
        holder.itemView.setOnClickListener(v -> {
            mListMeetingListener.onClick(meeting, holder.getAdapterPosition()); // CALLBACK
        });

        // Action to delete a meeting
        holder.mDeleteButton.setOnClickListener(v -> {
            mListMeetingListener.onDelete(meeting, holder.getAdapterPosition()); // CALLBACK
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
