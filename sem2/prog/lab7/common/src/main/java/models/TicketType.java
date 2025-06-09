package models;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Перечисление типов билетов.
 * Определяет возможные категории билетов.
 */
public enum TicketType {
    VIP,
    USUAL,
    BUDGETARY;

    /**
     * Возвращает список всех возможных типов билетов через запятую.
     * @return строка с перечислением типов
     */
    public static String names() {
        return Arrays.stream(values())
                   .map(TicketType::name)
                   .collect(Collectors.joining(", "));
    }
}
