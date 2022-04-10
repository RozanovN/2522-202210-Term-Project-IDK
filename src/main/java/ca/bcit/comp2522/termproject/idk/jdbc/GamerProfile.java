package ca.bcit.comp2522.termproject.idk.jdbc;


import com.almasb.fxgl.entity.component.Component;

import java.util.Objects;

/**
 * Represents gamer's profile.
 * @author Prince Chabveka
 * @version 2022
 */
    public class GamerProfile {

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    /**
     * Gamers are the same if they have the same id, username and name.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamerProfile that = (GamerProfile) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    /**
     * Generate hashcode
     * @return an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

