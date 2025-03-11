package models;

import utils.Validatable;
import utils.ConvertibleToCSV;

public class Coordinates implements Validatable, ConvertibleToCSV {
    private float x;
    private long y;

    public Coordinates(float x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toCSV() {
        return x + "," + y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }

    public boolean validate() {
        // Например, проверяем, что координаты не выходят за допустимые пределы
        return !(Double.isNaN(x) || Double.isNaN(y));
    }

}