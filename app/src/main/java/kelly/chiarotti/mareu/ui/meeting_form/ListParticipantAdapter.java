package kelly.chiarotti.mareu.ui.meeting_form;

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
import kelly.chiarotti.mareu.model.Participant;

public class ListParticipantAdapter extends RecyclerView.Adapter<ListParticipantAdapter.ViewHolder> {

    private List<Participant> mParticipants;
    private ListParticipantListener mListParticipantListener;

    interface ListParticipantListener {
        void onDelete(Participant participant, int position);
    }

    ListParticipantAdapter(List<Participant> items, ListParticipantListener listener) {
        super();
        mParticipants = items;
        mListParticipantListener = listener;
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participant participant = mParticipants.get(holder.getAdapterPosition());
        holder.mParticipant.setText(String.format("%s <%s>", participant.getNom(), participant.getEmail()));
        holder.mDeleteButton.setOnClickListener(v -> {
            mListParticipantListener.onDelete(participant, holder.getAdapterPosition());
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_participant)
        public TextView mParticipant;
        @BindView(R.id.item_list_delete_button)
        public ImageView mDeleteButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
