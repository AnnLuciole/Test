package org.example.homework9;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainApp {

    public static void main(String[] args) {
        try {
            Processor.connect();
            StudentDAO student = new StudentDAO("Lexi", 70);
            Processor.createTable(student);
            Processor.addDataInTable(student);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             Processor.disconnect();
        }

    }
}
