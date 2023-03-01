package org.example.homework9;

@Table(title = "cats")
public class CatDAO {
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "weight")
    private double weight;
    @Column(columnName = "colour")
    private String colour;

    public CatDAO(String name, double weight, String colour) {
        this.name = name;
        this.weight = weight;
        this.colour = colour;
    }

    @Getter
    public String getName() {
        return name;
    }

    @Getter
    public double getWeight() {
        return weight;
    }

    @Getter
    public String getColour() {
        return colour;
    }
}
