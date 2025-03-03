package models;

import utils.Validatable;

public abstract class Element implements Comparable<Element>, Validatable {
    abstract public int getId();
}