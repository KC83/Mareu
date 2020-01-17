package kelly.chiarotti.mareu;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kelly.chiarotti.mareu.di.DI;
import kelly.chiarotti.mareu.model.Meeting;
import kelly.chiarotti.mareu.model.MeetingRoom;
import kelly.chiarotti.mareu.service.ApiService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiServiceTest {
    private ApiService mApiService;

    @Before
    public void setApiService() {
        mApiService = DI.getNewInstanceApiService();
    }

    // Check if the list of meetings is empty
    @Test
    public void getMeetings_isEmpty() {
        assertEquals(0, mApiService.getMeetings().size());
    }

    // Check if the list of meeting rooms is not empty
    @Test
    public void getMeetingRooms_isNotEmpty() {
        assertNotNull(mApiService.getMeetingRooms());
    }

    // Check if the list of participants is not empty
    @Test
    public void getParticipants_isNotEmpty() {
        assertNotNull(mApiService.getParticipants());
    }

    // Add a new meeting
    @Test
    public void addMeeting() {
        Meeting meeting = new Meeting(1, new Date(), new Date(), "First meeting", mApiService.getMeetingRooms().get(0), mApiService.getParticipants());
        mApiService.addMeeting(meeting);
        assertTrue(mApiService.getMeetings().contains(meeting));
        assertEquals(1, mApiService.getMeetings().size());
    }

    // Update a meeting
    @Test
    public void updateMeeting() {
        mApiService.addMeeting(new Meeting(1, new Date(), new Date(), "First meeting", mApiService.getMeetingRooms().get(0), mApiService.getParticipants()));

        Meeting meeting = mApiService.getMeetings().get(0);
        assertEquals(mApiService.getMeetings().get(0).getSubject(), "First meeting");


        meeting.setSubject("First meeting has been updated");
        // TODO : Pour chaque attributs ?
        mApiService.updateMeeting(meeting, 0);

        assertEquals(mApiService.getMeetings().get(0).getSubject(), "First meeting has been updated");
    }

    // Delete a meeting
    @Test
    public void deleteMeeting() {
        mApiService.addMeeting(new Meeting(1, new Date(), new Date(), "First meeting", mApiService.getMeetingRooms().get(0), mApiService.getParticipants()));

        Meeting meeting = mApiService.getMeetings().get(0);
        mApiService.deleteMeeting(meeting);
        assertFalse(mApiService.getMeetings().contains(meeting));
        assertEquals(0, mApiService.getMeetings().size());
    }

    // Filter by date
    @Test
    public void filterByDate() throws ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2020");

        mApiService.addMeeting(new Meeting(1, date, new Date(), "Meeting n°1", mApiService.getMeetingRooms().get(0), mApiService.getParticipants()));
        mApiService.addMeeting(new Meeting(2, date, new Date(), "Meeting n°2", mApiService.getMeetingRooms().get(1), mApiService.getParticipants()));

        date = new SimpleDateFormat("dd/MM/yyyy").parse("25/02/2020");
        mApiService.addMeeting(new Meeting(3, date, new Date(), "Meeting n°3", mApiService.getMeetingRooms().get(1), mApiService.getParticipants()));

        List<Meeting> meetings = mApiService.getMeetingsByDate("15/01/2020");
        assertEquals(2, meetings.size());
        assertEquals("Meeting n°1", meetings.get(0).getSubject());
        assertEquals("Meeting n°2", meetings.get(1).getSubject());

        meetings = mApiService.getMeetingsByDate("25/02/2020");
        assertEquals(1, meetings.size());
        assertEquals("Meeting n°3", meetings.get(0).getSubject());
    }

    // Filter by meeting room
    @Test
    public void filterByMeetingRoom() {
        mApiService.addMeeting(new Meeting(1, new Date(), new Date(), "Meeting n°1", mApiService.getMeetingRooms().get(0), mApiService.getParticipants()));
        mApiService.addMeeting(new Meeting(2, new Date(), new Date(), "Meeting n°2", mApiService.getMeetingRooms().get(0), mApiService.getParticipants()));

        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingRooms.add(mApiService.getMeetingRooms().get(0));

        Meeting meeting_inList = mApiService.getMeetings().get(0);
        meeting_inList.setMeetingRoom(mApiService.getMeetingRooms().get(0));

        Meeting meeting_notInList = mApiService.getMeetings().get(1);
        meeting_notInList.setMeetingRoom(mApiService.getMeetingRooms().get(2));

        List<Meeting> meetings = mApiService.getMeetingsByMeetingRoom(meetingRooms);
        assertTrue(meetings.contains(meeting_inList));
        assertFalse(meetings.contains(meeting_notInList));
    }
}
