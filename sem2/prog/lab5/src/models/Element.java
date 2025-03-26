package models;

import utils.Validatable;

/**
 * Абстрактный базовый класс для всех элементов коллекции.
 * Определяет общий интерфейс и базовую функциональность элементов.
 */
public abstract class Element implements Comparable<Element>, Validatable {
    abstract public int getId();
}