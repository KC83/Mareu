package kelly.chiarotti.mareu.model;

public class MeetingRoom {
    private Integer mId;
    private String mName;

    /**
     * Constructor
     * @param id
     * @param name
     */
    public MeetingRoom(Integer id, String name) {
        mId = id;
        mName = name;
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
}
