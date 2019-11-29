package kelly.chiarotti.mareu.service;

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
    public void deleteMeeting(Meeting meeting);
    public List<Meeting> getMeetings();
    public List<Meeting> generateMeetings();
}
