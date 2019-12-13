package kelly.chiarotti.mareu.service;

import androidx.annotation.Nullable;

import java.util.List;

import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.MeetingRoom;
import kelly.chiarotti.mareu.model.Participant;

public interface ApiService {
    public List<Participant> generateParticipants();
    public List<Participant> getParticipants();

    public List<MeetingRoom> generateMeetingRooms();
    public List<MeetingRoom> getMeetingRooms();

    public void addMeeting(Meeting meeting);
    public void deleteMeeting(@Nullable Meeting meeting, @Nullable Integer position);
    public List<Meeting> getMeetings();
    public List<Meeting> generateMeetings();
}
