package ca.bcit.comp2522.termproject.idk.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


/**
 * Data source for SQL connection.
 *
 * @author Prince Chabveka
 * @version 2022
 */
public  class Datasource {
    /**
     * Connection to database to run various queries.
     */
    public static final String DB_NAME = "comp2522.db";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME;
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String COLUMN_ACHIEVEMENTS_ID = "Id";
    public static final int INDEX_ACHIEVEMENTS_ID = 1;
    public static final int INDEX_HIGH_SCORE = 2;
    public static final int INDEX_COLUMN_KILLS = 3;
    public static final int INDEX_PLAYS = 4;
    public static final String QUERY_ALL_ACHIEVEMENTS =
            "SELECT " + TABLE_ACHIEVEMENTS + '.' + COLUMN_ACHIEVEMENTS_ID + " FROM " + TABLE_ACHIEVEMENTS;
    private Connection connection;
    private PreparedStatement queryAchievements;

    /**
     * Construct data source.
     */
    public Datasource() {
    }

    /**
     * check if database is successfully connected.
     *
     * @return a boolean
     */
    public boolean open() {
        try {
            final Properties connectionProperties = new Properties();
            connectionProperties.put("user", "root");
            connectionProperties.put("password", "root");
            connection = DriverManager.getConnection(CONNECTION_STRING, connectionProperties);
            queryAchievements = connection.prepareStatement(QUERY_ALL_ACHIEVEMENTS);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close connection after database connection, when needed.
     */
    public void close() {
        try {

            if (queryAchievements != null) {
                queryAchievements.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    /**
     * Query through gamer achievements and display results.
     * @return a list
     */
    public List<GamerProfile> queryGamerAchievements() {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ACHIEVEMENTS);
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<GamerProfile> gamerAchievements = new ArrayList<>();
            while (results.next()) {
                GamerProfile gamerProfile = new GamerProfile();

//                #change set to GamerProfile methods
                gamerProfile.setId(results.getInt(INDEX_ACHIEVEMENTS_ID));
                gamerProfile.setId(results.getInt(INDEX_COLUMN_KILLS));
                gamerProfile.setId(results.getInt(INDEX_PLAYS));
                gamerProfile.setId(results.getInt(INDEX_HIGH_SCORE));
                gamerAchievements.add(gamerProfile);
            }

            return gamerAchievements;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if datasources are equal.
     * @param o
     * @return a boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Datasource that = (Datasource) o;
        return Objects.equals(connection, that.connection) && Objects.equals(queryAchievements, that.queryAchievements);
    }

    /**
     * Generate hashcode.
     * @return an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(connection, queryAchievements);
    }

    /**
     * Generate toString method.
     * @return
     */
    @Override
    public String toString() {
        return "Datasource{"
                +
                "connection="
                + connection
                +
                ", queryAchievements="
                +
                queryAchievements
                +
                '}';
    }
}
