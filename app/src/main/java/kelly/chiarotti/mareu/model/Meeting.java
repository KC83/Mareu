package kelly.chiarotti.mareu.model;

import java.util.Date;
import java.util.List;

public class Meeting {
    private Integer mId;
    private Date mDate;
    private Date mTime;
    private String mSubject;
    private MeetingRoom mMeetingRoom;
    private List<Participant> mParticipants;

    /**
     * Constructor
     * @param id
     * @param date
     * @param subject
     */
    public Meeting(Integer id, Date date, Date time, String subject, MeetingRoom meetingRoom, List<Participant> participants) {
        this.mId = id;
        this.mDate = date;
        this.mTime = time;
        this.mSubject = subject;
        this.mMeetingRoom = meetingRoom;
        this.mParticipants = participants;
    }

    /**
     * Get value of mId
     * @return mId
     */
    public Integer getId() {
        return mId;
    }

    /**
     * Set value of mId
     * @param id
     */
    public void setId(Integer id) {
        mId = id;
    }

    /**
     * Get value of mDate
     * @return mDate
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Set value of mDate
     * @param date
     */
    public void setDate(Date date) {
        mDate = date;
    }

    /**
     * Get value of mTime
     * @return mTime
     */
    public Date getTime() {
        return mTime;
    }

    /**
     * Set value of mTime
     * @param time
     */
    public void setTime(Date time) {
        mTime = time;
    }

    /**
     * Get value of mSubject
     * @return mSubject
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * Set value of mSubject
     * @param subject
     */
    public void setSubject(String subject) {
        mSubject = subject;
    }

    /**
     * Get value of mMeetingRoom
     * @return mMeetingRoom
     */
    public MeetingRoom getMeetingRoom() {
        return mMeetingRoom;
    }

    /**
     * Set value of mMeetingRoom
     * @param meetingRoom
     */
    public void setMeetingRoom(MeetingRoom meetingRoom) {
        mMeetingRoom = meetingRoom;
    }

    /**
     * Get list of participants
     * @return mParticipants
     */
    public List<Participant> getParticipants() {
        return mParticipants;
    }

    /**
     * Set list of participants
     * @param participants
     */
    public void setParticipants(List<Participant> participants) {
        mParticipants = participants;
    }
}
