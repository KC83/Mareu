package kelly.chiarotti.mareu.ui.meeting_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.ui.meeting_form.FormMeetingActivity;

public class ListMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);

        List<Meeting> meetings = DI.getApiService().getMeetings();
        Context context = ListMeetingActivity.this;

        ListMeetingAdapter adapter = new ListMeetingAdapter(meetings, context);

        final RecyclerView mRecyclerView = findViewById(R.id.activity_list_meeting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent formMeetingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        startActivity(formMeetingActivityIntent);
    }
}
