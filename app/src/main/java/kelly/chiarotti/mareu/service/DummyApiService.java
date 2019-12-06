package kelly.chiarotti.mareu.service;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.MeetingRoom;
import kelly.chiarotti.mareu.model.Participant;

public class DummyApiService implements ApiService {
    private List<Participant> mParticipants = generateParticipants();
    private List<MeetingRoom> mMeetingRooms = generateMeetingRooms();
    private List<Meeting> mMeetings = generateMeetings(); // Ne doit pas appeler generateMeetings()

    @Override
    public List<Participant> generateParticipants() {
        List<Participant> dummyParticipants  = Arrays.asList(
                new Participant(1, "jean@gmail.com"),
                new Participant(2, "marc@gmail.com"),
                new Participant(3, "laura@gmail.com"),
                new Participant(4, "john@gmail.com"),
                new Participant(5, "lola@gmail.com")
        );
        return new ArrayList<>(dummyParticipants);
    }

    @Override
    public List<Participant> getParticipants() {
        return mParticipants;
    }

    @Override
    public List<MeetingRoom> generateMeetingRooms() {
        List<MeetingRoom> dummyMeetingRooms  = Arrays.asList(
                new MeetingRoom(1, "Salle A", Color.rgb(128,203,196)),
                new MeetingRoom(2, "Salle B", Color.rgb(255,171,145)),
                new MeetingRoom(3, "Salle C", Color.rgb(156,204,101)),
                new MeetingRoom(4, "Salle D", Color.rgb(92,107,192)),
                new MeetingRoom(5, "Salle E", Color.rgb(236,64,122)),
                new MeetingRoom(6, "Salle A1", Color.rgb(128,203,196)),
                new MeetingRoom(7, "Salle B1", Color.rgb(255,171,145)),
                new MeetingRoom(8, "Salle C1", Color.rgb(156,204,101)),
                new MeetingRoom(9, "Salle D1", Color.rgb(92,107,192)),
                new MeetingRoom(10, "Salle E1", Color.rgb(236,64,122))
        );
        return new ArrayList<>(dummyMeetingRooms);
    }

    @Override
    public List<MeetingRoom> getMeetingRooms() {
        return mMeetingRooms;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        mMeetings.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting)  {
        mMeetings.remove(meeting);
    }

    @Override
    public List<Meeting> getMeetings() {
        return mMeetings;
    }

    @Override
    public List<Meeting> generateMeetings() {
        List<Meeting> dummyMeetings  = Arrays.asList(
                new Meeting(1, new Date(), new Date(), "Sujet", getMeetingRooms().get(0), getParticipants()),
                new Meeting(2, new Date(), new Date(), "Sujet 1", getMeetingRooms().get(2), getParticipants())
        );
        return new ArrayList<>(dummyMeetings);
    }
}
