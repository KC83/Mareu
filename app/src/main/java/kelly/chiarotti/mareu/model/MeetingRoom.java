package kelly.chiarotti.mareu.model;

import java.util.Objects;

public class MeetingRoom {
    private Integer mId;
    private String mName;
    private Integer mColor;

    /**
     * Constructor
     * @param id
     * @param name
     */
    public MeetingRoom(Integer id, String name, Integer color) {
        mId = id;
        mName = name;
        mColor = color;
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
     * Get value of mName
     * @return mName
     */
    public String getName() {
        return mName;
    }

    /**
     * Set value of mName
     * @param name
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Get value of mColor
     * @return
     */
    public Integer getColor() {
        return mColor;
    }

    /**
     * Set value of mColor
     * @param color
     */
    public void setColor(Integer color) {
        mColor = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingRoom meetingRoom = (MeetingRoom) o;
        return Objects.equals(mId, meetingRoom.mId);
    }
}
