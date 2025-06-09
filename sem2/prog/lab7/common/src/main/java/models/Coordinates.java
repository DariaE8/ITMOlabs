package models;

import utils.Validatable;
import utils.ConvertibleToCSV;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий географические координаты.
 * Содержит координаты x (широта) и y (долгота) и методы для работы с ними.
 */
public class Coordinates implements Serializable, Validatable, ConvertibleToCSV {
    private static final float MIN_X = -180.0f;
    private static final float MAX_X = 180.0f;
    private static final long MIN_Y = -90;
    private static final long MAX_Y = 90;

    private final float x; // Широта (диапазон [-180..180])
    private final long y;  // Долгота (диапазон [-90..90])

    /**
     * Создает новый объект координат.
     *
     * @param x широта (должна быть в диапазоне [-180..180])
     * @param y долгота (должна быть в диапазоне [-90..90])
     * @throws IllegalArgumentException если координаты выходят за допустимые пределы
     */
    public Coordinates(float x, long y) {
        if (!isValidX(x)) {
            throw new IllegalArgumentException(
                String.format("Широта должна быть в диапазоне [%.1f..%.1f]", MIN_X, MAX_X));
        }
        if (!isValidY(y)) {
            throw new IllegalArgumentException(
                String.format("Долгота должна быть в диапазоне [%d..%d]", MIN_Y, MAX_Y));
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Проверяет валидность координаты x.
     */
    private static boolean isValidX(float x) {
        return !Float.isNaN(x) && x >= MIN_X && x <= MAX_X;
    }

    /**
     * Проверяет валидность координаты y.
     */
    private static boolean isValidY(long y) {
        return y >= MIN_Y && y <= MAX_Y;
    }

    /**
     * Возвращает строковое представление координат в CSV формате.
     *
     * @return строка в формате "x,y"
     */
    @Override
    public String toCSV() {
        return String.format("%f,%d", x, y);
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строка в формате "x;y"
     */
    @Override
    public String toString() {
        return String.format("%.6f;%d", x, y);
    }

    /**
     * Проверяет валидность координат.
     *
     * @return true если координаты валидны
     */
    @Override
    public boolean validate() {
        return isValidX(x) && isValidY(y);
    }

    /**
     * Возвращает заголовок CSV для координат.
     *
     * @return массив названий полей
     */
    public static String[] getCSVHeader() {
        return new String[]{"x", "y"};
    }

    // Геттеры
    public float getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Float.compare(that.x, x) == 0 && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}