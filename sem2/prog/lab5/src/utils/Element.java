package utils;
/**
 * Абстрактный класс ждя элементов коллеции
 */
public abstract class Element implements Comparable<Element>, Validatable {
    abstract public long getId();
}
