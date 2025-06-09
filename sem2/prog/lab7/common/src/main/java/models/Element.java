package models;

import java.io.Serializable;

import utils.Validatable;

/**
 * Абстрактный базовый класс для всех элементов коллекции.
 * Определяет общий интерфейс и базовую функциональность элементов.
 */
public abstract class Element implements Comparable<Element>, Validatable, Serializable {
    abstract public int getId();
}