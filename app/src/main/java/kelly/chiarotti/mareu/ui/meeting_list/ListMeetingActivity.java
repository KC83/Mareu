package kelly.chiarotti.mareu.ui.meeting_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.ui.meeting_form.FormMeetingActivity;

public class ListMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);

        ListMeetingAdapter adapter = new ListMeetingAdapter();

        final RecyclerView mRecyclerView = findViewById(R.id.activity_list_meeting);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent rankingActivityIntent = new Intent(ListMeetingActivity.this, FormMeetingActivity.class);
        startActivity(rankingActivityIntent);
    }
}
