package org.example.homework9;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainApp {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        try {
            connect();
            StudentDAO student = new StudentDAO("Lexi", 70);
            createTable(student);
            addDataInTable(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             disconnect();
        }

    }

    public static void addDataInTable(Object o) throws Exception {
        if (!o.getClass().isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("This object hasn't annotated like Table.");
        }
        String sqlRequest = "INSERT INTO " + o.getClass().getAnnotation(Table.class).title()
                + " (" + getStringWithAllFields(o) + ") VALUES (" +
                getAllQuestionMarkForStatement(o) + ");";
        preparedStatement = connection.prepareStatement(sqlRequest);
        List <Method> methods = Arrays.stream(o.getClass().getDeclaredMethods())
                .filter(x -> x.isAnnotationPresent(Getter.class))
                .toList();
        for (int i = 0; i < methods.size(); i++) {
            preparedStatement.setObject(i + 1, methods.get(i).invoke(o));
        }
        preparedStatement.executeUpdate();
    }

    public static void createTable(Object o) throws Exception {
        if (!o.getClass().isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("This object hasn't annotated like Table.");
        }
        String sqlRequest = "CREATE TABLE " + o.getClass().getAnnotation(Table.class).title()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + getStringWithAllFieldsAndFieldTypes(o) + ");";
        preparedStatement = connection.prepareStatement(sqlRequest);
        preparedStatement.executeUpdate();
    }

    private static String getAllQuestionMarkForStatement(Object o) {
        return Arrays.stream(o.getClass().getDeclaredFields())
                .filter(x -> x.isAnnotationPresent(Column.class))
                .map(x -> "?")
                .collect(Collectors.joining(", "));
    }

    private static String getStringWithAllFieldsAndFieldTypes(Object o) {
        return Arrays.stream(o.getClass().getDeclaredFields())
                .filter(x -> x.isAnnotationPresent(Column.class))
                .map(x -> x.getName() + " " + getDataTypeForDataBase(x.getType().getSimpleName()))
                .collect(Collectors.joining(", "));
    }

    private static String getStringWithAllFields(Object o) {
        return Arrays.stream(o.getClass().getDeclaredFields())
                .filter(x -> x.isAnnotationPresent(Column.class))
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private static String getDataTypeForDataBase(String dataType) {
        return switch (dataType) {
            case "String" -> "TEXT";
            case "int" -> "INTEGER";
            case "double" -> "Numeric";
            case "float" -> "REAL";
            default -> "BLOB";
        };
    }

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Unable to connect");
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
