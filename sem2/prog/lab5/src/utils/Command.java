package utils;

/**
 * Абстрактная команда с именем и описанием
 */
public abstract class Command {
	private final String name;
	private final String description;

	public Command(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return Название и использование команды.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Описание команды.
	 */
	public String getDescription() {
		return description;
	}

	public abstract void run(String[] args);

	@Override
	public String toString() {
		return "Command{" +
		"name='" + name + '\'' +
		", description='" + description + '\'' +
		'}';
	}
}