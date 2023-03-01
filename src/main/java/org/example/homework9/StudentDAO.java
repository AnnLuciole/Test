package org.example.homework9;

@Table(title = "students")
public class StudentDAO {

    @Column(columnName = "name")
    private String name;

    @Column(columnName = "score")
    private int score;

    public StudentDAO(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Getter
    public String getName() {
        return name;
    }

    @Getter
    public int getScore() {
        return score;
    }
}
