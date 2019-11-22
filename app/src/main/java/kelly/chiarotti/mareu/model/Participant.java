package kelly.chiarotti.mareu.model;

public class Participant {
    private Integer mId;
    private String mEmail;

    /**
     * Constructor
     * @param id
     * @param email
     */
    public Participant(Integer id, String email) {
        mId = id;
        mEmail = email;
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
     * Get value of mEmail
     * @return mEmail
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Set value of mEmail
     * @param email
     */
    public void setEmail(String email) {
        mEmail = email;
    }
}
