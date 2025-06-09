package models;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Перечисление типов мест проведения мероприятий.
 * Определяет возможные категории мест проведения.
 */
public enum VenueType {
    BAR,
    THEATRE,
    MALL;

    /**
     * Возвращает список всех возможных типов через запятую.
     * @return строка с перечислением типов
     */
    public static String names() {
        return Arrays.stream(values())
                   .map(VenueType::name)
                   .collect(Collectors.joining(", "));
    }
}
