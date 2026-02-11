package pkm_data_installer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

public class App {
    private final static Yaml yaml = new Yaml();
    private final static String URL = "jdbc:mariadb://localhost:3306/pkmdb";
    private final static String USER = "root";
    private final static String PW = "root_password";
    private final static Map<String, Integer> releaseIdLookupTable = new HashMap<>();
    private final static Map<String, Integer> gameIdLookupTable = new HashMap<>();
    private final static Map<String, Integer> typeIdLookupTable = new HashMap<>();

    public static Map<String, Object> readYaml(String resourcePathString) {
        try(InputStream in = App.class.getClassLoader().getResourceAsStream(resourcePathString)){
            return yaml.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null; // required to compile, but should never be reached
        }
    }

    private static Set<Integer> getGenerationsFromReleases(Map<String,Object> yamlMap) {
        Set<Integer> set = new HashSet<>();
        for (String key : yamlMap.keySet()) {
            Map<String, Object> release = (Map<String, Object>)yamlMap.get(key);
            set.add((Integer)release.get("gen"));
        }
        return set;
    }

    private static void pushGenerations(Statement statement) throws SQLException {
        Map<String, Object> yamlMap = readYaml("releases.yaml");
        Set<Integer> generations = getGenerationsFromReleases(yamlMap);
        int rows_affected = 0;
        for (Integer gen_id : generations) {
            String insert_sql = "INSERT INTO generations (id) VALUES (" + gen_id + ");";
            rows_affected += statement.executeUpdate(insert_sql);
        }
        System.out.println("pushGenerations rows affected: " + rows_affected);
    }

    private static void pushData(
        Connection conn, 
        String filePath, 
        String preparedSQL, 
        Map<String, Integer> idTable,
        TablePushLogic pushLogic
    ) throws SQLException {
        Map<String, Object> yamlMap = 
            readYaml(filePath);
        int rows_affected = 0;
        int nextId = 1;
        for (String key : yamlMap.keySet()) {
            Map<String, Object> dataSource = (Map<String, Object>)yamlMap.get(key);
            try (PreparedStatement statement = conn.prepareStatement(preparedSQL)) {
                pushLogic.configure(statement, dataSource);
                int added = statement.executeUpdate();
                if (added > 0) {
                    idTable.put(key, nextId);
                    nextId += added;
                }
                rows_affected += added;
            } catch (Exception e) {
                throw e;
            }
        }
        System.out.println(filePath + "'s table rows affected: " + rows_affected);
    }

    private static void pushReleases(Connection connection) throws SQLException {
        String filePath = "releases.yaml";
        String sql = "INSERT INTO releases (name, gen_id) VALUES (?, ?);";
        pushData(connection, filePath, sql, releaseIdLookupTable, (statement, release) -> {
            String name = (String)release.get("name");
            int gen = (Integer)release.get("gen");
            statement.setString(1, name);
            statement.setInt(2, gen);
        });
    }

    private static void pushGames(Connection connection) throws SQLException {
        String filePath = "games.yaml";
        String sql = "INSERT INTO games (name, release_id) VALUES (?, ?);";
        pushData(connection, filePath, sql, gameIdLookupTable, (statement, game) -> {
            String name = (String)game.get("name");
            int release = releaseIdLookupTable.get((String)game.get("release"));
            statement.setString(1, name);
            statement.setInt(2, release);
        });
    }

    private static void pushTypes(Connection connection) throws SQLException {
        String filePath = "types.yaml";
        String sql = "INSERT INTO types (name, gen_id) VALUES (?, ?)";
        pushData(connection, filePath, sql, typeIdLookupTable, (statement, type) -> {
            String name = (String)type.get("name");
            int gen = (Integer)type.get("gen");
            statement.setString(1, name);
            statement.setInt(2, gen);
        });
    } 

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        }
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PW);
            Statement stmt  = conn.createStatement();
        ) {
            pushGenerations(stmt);
            pushReleases(conn);
            pushGames(conn);
            pushTypes(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(3);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(4);
        }
    }
}
