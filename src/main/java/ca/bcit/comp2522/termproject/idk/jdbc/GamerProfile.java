package ca.bcit.comp2522.termproject.idk.jdbc;


import java.util.Objects;

/**
 * Represents gamer's profile.
 * @author Prince Chabveka
 * @version 2022
 */
    public class GamerProfile {
        private int id;
        private String name;

    /**
     * Get user id.
     * @return an int
     */
    public int getId() {
            return id;
        }

    /**
     * set user Id.
     *
     * @param id an int
     */
    public void setId(final int id) {
            this.id = id;
        }

    /**
     * Get username.
     *
     * @return a string.
     */
    public String getName() {
            return name;
        }

    /**
     * Set user's name.
     *
     * @param name a string
     */
    public void setName(final String name) {
            this.name = name;
        }

    /**
     * Gamers are the same if they have the same id, username and name.
     * @return boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GamerProfile that = (GamerProfile) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    /**
     * Generate hashcode.
     *
     * @return an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

