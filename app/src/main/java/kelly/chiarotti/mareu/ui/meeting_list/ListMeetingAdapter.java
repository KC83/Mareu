package kelly.chiarotti.mareu.ui.meeting_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.Participant;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;
import kelly.chiarotti.mareu.ui.meeting_form.FormMeetingActivity;

public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder> {

    private ApiService mApiService;
    private List<Meeting> mMeetings;
    private Context mContext;

    ListMeetingAdapter(List<Meeting> items, Context context) {
        super();
        mApiService = DI.getApiService();
        mMeetings = items;
        mContext = context;
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

        // Action to delete a meeting
        holder.mDeleteButton.setOnClickListener(v -> {
            mApiService.deleteMeeting(meeting);
            notifyItemRemoved(position); // RELOAD THE LIST
        });

        // Action to update the meeting
        holder.itemView.setOnClickListener(v -> {
            Intent formMeetingActivityIntent = new Intent(mContext, FormMeetingActivity.class);
            formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
            formMeetingActivityIntent.putExtra(Constants.EXTRA_MEETING_POSITION, position);
            mContext.startActivity(formMeetingActivityIntent);
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
