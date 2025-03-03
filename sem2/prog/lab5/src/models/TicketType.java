package models;

public enum TicketType {
    VIP,
    USUAL,
    BUDGETARY;

    public static String names() {
            StringBuilder nameList = new StringBuilder();
            for (var dragonType : values()) {
                nameList.append(dragonType.name()).append(", ");
            }
            return nameList.substring(0, nameList.length()-2);
        }
}
