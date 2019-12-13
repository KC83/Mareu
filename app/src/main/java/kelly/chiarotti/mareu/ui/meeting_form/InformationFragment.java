package kelly.chiarotti.mareu.ui.meeting_form;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kelly.chiarotti.mareu.R;
import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.service.ApiService;
import kelly.chiarotti.mareu.service.Constants;

public class InformationFragment extends Fragment {

    private InformationFragmentListener mListener;
    private Integer mMeetingPosition;
    private Integer mId;

    static InformationFragment newInstance(Meeting meeting, int position) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_MEETING, new Gson().toJson(meeting));
        args.putInt(Constants.EXTRA_MEETING_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface InformationFragmentListener {
        void onNextInformationButton(Meeting meeting, int position);
    }

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        EditText date = view.findViewById(R.id.item_form_date);
        date.setInputType(InputType.TYPE_NULL);
        EditText time = view.findViewById(R.id.item_form_time);
        time.setInputType(InputType.TYPE_NULL);
        EditText subject = view.findViewById(R.id.item_form_subject);
        Button button = view.findViewById(R.id.btn_next_fragment_information);

        if (getArguments() != null) {
            Context context = getContext();
            ApiService apiService = DI.getApiService();
            Meeting meeting = new Gson().fromJson(getArguments().getString(Constants.EXTRA_MEETING), new TypeToken<Meeting>() {}.getType());

            if (meeting != null) {
                mMeetingPosition = getArguments().getInt(Constants.EXTRA_MEETING_POSITION);
                mId = meeting.getId();

                date.setText(new SimpleDateFormat("dd/MM/yyyy").format(meeting.getDate()));
                time.setText(new SimpleDateFormat("HH:mm").format(meeting.getTime()));
                subject.setText(meeting.getSubject());
            } else {
                mId = apiService.getMeetings().size()+1;
            }

            if (context != null) {
                // DATEPICKER
                date.setOnClickListener(v -> {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    @SuppressLint("SetTextI18n")
                    DatePickerDialog picker = new DatePickerDialog(context, (vw, year1, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                    picker.show();
                });
                // TIMEPICKER
                time.setOnClickListener(v -> {
                    final Calendar cldr = Calendar.getInstance();
                    int hour = cldr.get(Calendar.HOUR_OF_DAY);
                    int minutes = cldr.get(Calendar.MINUTE);
                    // time picker dialog
                    @SuppressLint("SetTextI18n") TimePickerDialog picker = new TimePickerDialog(context, (tp, sHour, sMinute) -> time.setText(sHour + ":" + sMinute), hour, minutes, true);
                    picker.show();
                });
            }

            button.setOnClickListener(v -> {
                Date dt = null;
                Date tm = null;

                try {
                    dt = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                    tm = new SimpleDateFormat("HH:mm").parse(time.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Meeting newMeeting = new Meeting(mId, dt, tm, subject.getText().toString(), null, null);
                mListener.onNextInformationButton(newMeeting, mMeetingPosition);
            });
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof InformationFragmentListener) {
            mListener = (InformationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement InformationFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
