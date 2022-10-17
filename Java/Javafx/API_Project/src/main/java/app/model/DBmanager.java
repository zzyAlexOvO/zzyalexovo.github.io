package app.model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBmanager {
    private static final String dbName = "breadcrumbs.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            // If we get here that means no exception raised from getConnection - meaning it worked
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
            }
        } else {
            System.out.println("No existing DB file.");
        }
    }

    public void setupDB() {
        String createUserTableSQL =
                """
                CREATE TABLE IF NOT EXISTS breadcrumbs (
                id integer not null,
                type text not null,
                item text not null);
                """;

        String createCacheTableSQL =
                """
                CREATE TABLE IF NOT EXISTS caches (
                type text,
                id integer,
                name text,
                info text,
                PRIMARY KEY(type,id));
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createUserTableSQL);
            statement.execute(createCacheTableSQL);
            System.out.println("Created tables");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public List<String[]> listItems(){
        String newLine = System.getProperty("line.separator");
        String loginUserSQL =
                """
                SELECT id,type,item
                FROM breadcrumbs;
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(loginUserSQL)) {
            ResultSet results =  preparedStatement.executeQuery();
            List<String[]> list = new ArrayList<>();
            while (results.next()) {
                list.add(new String[]{results.getString("item"),results.getString("type"),results.getString("id")});
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public void insertItem(String type,String item, int id){
        String loginUserSQL = "INSERT INTO breadcrumbs(type, item, id) VALUES (?, ?, ?); ";
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(loginUserSQL)) {
            preparedStatement.setString(1,type);
            preparedStatement.setString(2,item);
            preparedStatement.setInt(3,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public String checkCache(String type, int id){
        String sql = """
                SELECT info
                FROM caches
                WHERE type = ? and id = ?;
                """;
        System.out.println(sql);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,type);
            preparedStatement.setInt(2,id);
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()){
                return results.getString("info");
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void updateCache(String type, int id, String name, String info){
        String sql = """
               UPDATE caches
               SET info = ?
               WHERE type = ? and id = ? and name = ?;""";
        System.out.println(sql);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,info);
            preparedStatement.setString(2,type);
            preparedStatement.setInt(3,id);
            preparedStatement.setString(4,name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void insertCache(String type, int id, String name, String info){
        String sql = """
               INSERT INTO caches (type, id, name, info)
               VALUES(?,?,?,?);""";
        System.out.println(sql);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,type);
            preparedStatement.setString(3,name);
            preparedStatement.setInt(2,id);
            preparedStatement.setString(4,info);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    public String checkCache(String type, String name){
        String sql = """
                SELECT info
                FROM caches
                WHERE type = ? and name = ?;
                """;
        System.out.println(sql);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,type);
            preparedStatement.setString(2,name);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                return results.getString("info");
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String clearCache(){
        String sql = """
                DELETE FROM caches
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

