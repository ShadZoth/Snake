package ru.hse.se.g272.ervo.ooaip.snake;

/**
 * Направление движения змеи.
 *
 * @author Эрво Виктор
 * @since 10.03.14
 */
public enum SnakeDirection {
    /**
     * Движение вниз.
     */
    DOWN(40),

    /**
     * Движение слева.
     */
    LEFT(37),

    /**
     * Движение вправо.
     */
    RIGHT(39),

    /**
     * Движение вверх.
     */
    UP(38);

    /**
     * Номер кнопки, задающей данное направление.
     */
    private final int value;

    /**
     * Создаёт направление движения.
     * @param i Номер кнопки, задающей данное направление.
     */
    SnakeDirection(final int i) {
        value = i;
    }

    /**
     * Получает Номер кнопки, задающей данное направление.
     * @return Номер кнопки, задающей данное направление.
     */
    public int getValue() {
        return value;
    }

    /**
     * Проверяет, является ли данное направление противоположным
     * направлению-аргументу.
     * @param direction Направление, с которым сравнивается данное
     * @return {@code true}, если данное направление противоположно
     * направлению-аргументу.
     */
    public boolean oppositeTo(final SnakeDirection direction) {
        return (this == UP && direction == DOWN)
                || (this == DOWN && direction == UP)
                || (this == LEFT && direction == RIGHT)
                || (this == RIGHT && direction == LEFT);
    }
}
