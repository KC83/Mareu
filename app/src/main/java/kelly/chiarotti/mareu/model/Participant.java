package kelly.chiarotti.mareu.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Participant {
    private Integer mId;
    private String mEmail;
    private String mNom;

    /**
     * Constructor
     * @param id
     * @param email
     */
    public Participant(Integer id, String email, String nom) {
        mId = id;
        mEmail = email;
        mNom = nom;
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

    /**
     * Get value of mNom
     * @return mNom
     */
    public String getNom() {
        return mNom;
    }

    /**
     * Set volue of mNom
     * @param nom
     */
    public void setNom(String nom) {
        mNom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant participant = (Participant) o;
        return Objects.equals(mId, participant.mId);
    }

    @NonNull
    @Override
    public String toString() {
        return mNom;
    }


}
