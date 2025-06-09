package utils;

/**
 * Абстрактный базовый класс для всех команд приложения.
 * Определяет общий интерфейс и базовую функциональность команд.
 */
public abstract class Command {
  private final String name;
  private final String description;

  /**
   * Создает новую команду с указанными параметрами.
   *
   * @param name        название команды
   * @param description краткое описание команды
   * @throws IllegalArgumentException если name или description равны null или
   *                                  пустые
   */
  public Command(String name, String description) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Название команды не может быть пустым");
    }
    if (description == null || description.trim().isEmpty()) {
      throw new IllegalArgumentException("Описание команды не может быть пустым");
    }

    this.name = name.trim();
    this.description = description.trim();
  }

  /**
   * Возвращает название команды.
   *
   * @return название команды
   */
  public String getName() {
    return name;
  }

  public abstract void execute(String[] args) throws CommandException;
  /**
   * Возвращает описание команды.
   *
   * @return описание команды
   */
  public String getDescription() {
    return description;
  }

  /**
   * Возвращает строковое представление команды.
   *
   * @return строковое представление в формате "Команда[name='...',
   *         description='...']"
   */
  @Override
  public String toString() {
    return "Команда[name='" + name + "', description='" + description + "']";
  }

  /**
   * Возвращает информацию о команде для справки.
   *
   * @return форматированная строка с именем и описанием команды
   */
  public String getHelpInfo() {
    return String.format("%-20s - %s", name, description);
  }
}
