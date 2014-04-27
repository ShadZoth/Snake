package ru.hse.se.g272.ervo.ooaip.snake;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * игра "Змейка".
 *
 * @author Эрво Виктор
 * @since 10.03.14
 */
public class SnakeGame extends JComponent {
    /**
     * Процент окна, занимаемый игровым полем.
     */
    public static final double BOUND_MULTIPLIER = 0.95;

    /**
     * Половина.
     */
    public static final double HALF = 0.5;

    /**
     * Яблоко.
     */
    private Apple apple;

    /**
     * Змейка.
     */
    private List<SnakeBlock> snakeAsList;

    /**
     * Напрвление движения змейки.
     */
    private SnakeDirection direction;

    /**
     * Верхняя граница.
     */
    private int upBound;

    /**
     * Нижняя граница.
     */
    private int downBound;

    /**
     * Правая граница.
     */
    private int rightBound;

    /**
     * Левая граница.
     */
    private int leftBound;

    /**
     * Количество очков.
     */
    private int score;

    /**
     * Необходимость увеличивать змею.
     */
    private boolean growthNeeded;

    /**
     * Создаёт игру.
     */
    public SnakeGame() {
        die(true);
        direction = SnakeDirection.UP;
    }

    /**
     * имитирует смерть змеи.
     * @param first {@code false}, если смерть
     */
    private void die(final boolean first) {
        if (!first) {
            JOptionPane.showMessageDialog(this, "Your score: " + getScore());
        } else {
            JOptionPane.showMessageDialog(this, "Управление стрелочками. "
                    + "Пауза - ESC");
        }
        snakeAsList = new LinkedList<SnakeBlock>();
        snakeAsList.add(new SnakeBlock(0, 0));
        snakeAsList.add(new SnakeBlock(0, SnakeBlock.SIZE));
        setScore(0);
    }

    @Override
    protected final void paintComponent(final Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.translate(getWidth() / 2, getHeight() / 2);
        double xBound = BOUND_MULTIPLIER * (getWidth() / 2);
        double yBound = BOUND_MULTIPLIER * (getHeight() / 2);
        Rectangle2D rectangle2D = new Rectangle2D.Double(-xBound,
                -yBound, BOUND_MULTIPLIER * getWidth(),
                BOUND_MULTIPLIER * getHeight());
        g.draw(rectangle2D);
        setUpBound((int) (-yBound + HALF * SnakeBlock.SIZE));
        setDownBound((int) (yBound - HALF * SnakeBlock.SIZE));
        setRightBound((int) (xBound - HALF * SnakeBlock.SIZE));
        setLeftBound((int) (-xBound + HALF * SnakeBlock.SIZE));
        drawSnake(g);
        drawApple(g);
    }

    /**
     * Рисует яблоко.
     * @param g Параметр графики
     */
    private void drawApple(final Graphics2D g) {
        g.setPaint(Color.RED);
        if (apple == null) {
            apple = new Apple();
        }
        g.fillRect((int) (apple.getX() - SnakeBlock.SIZE / 2),
                (int) (apple.getY() - SnakeBlock.SIZE / 2),
                SnakeBlock.SIZE, SnakeBlock.SIZE);
        g.setPaint(Color.BLACK);
    }

    /**
     * Рисует змею.
     * @param g Параметр графики
     */
    private void drawSnake(final Graphics2D g) {
        for (SnakeBlock block : getSnakeAsList()) {
            drawBlock(block, g);
        }
    }

    /**
     * Рисует блок.
     * @param block Блок, подлежащий рисованию
     * @param g Параметр графики
     */
    private void drawBlock(final SnakeBlock block, final Graphics2D g) {
        Rectangle2D blockRect = new Rectangle2D.Double(
                block.getX() - SnakeBlock.SIZE / 2,
                block.getY() - SnakeBlock.SIZE / 2,
                        SnakeBlock.SIZE,
                        SnakeBlock.SIZE);
        g.draw(blockRect);
    }

    /**
     * Змейка.
     * @return Змейка
     */
    public final List<SnakeBlock> getSnakeAsList() {
        return snakeAsList;
    }

    /**
     * Перемещает змею.
     */
    public final void moveSnake() {
        SnakeBlock previous = getHead();
        SnakeBlock potentialNewTail = new SnakeBlock(getTail().getX(),
                getTail().getY());
        for (SnakeBlock block: getSnakeAsList()) {
            if (block != getHead()) {
                double x = block.getX();
                double y = block.getY();
                block.setX(previous.getX());
                block.setY(previous.getY());
                previous = new SnakeBlock(x, y);
            }
        }
        if (isGrowthNeeded()) {
            getSnakeAsList().add(potentialNewTail);
            setGrowthNeeded(false);
        }
        moveBlock(getHead());
        checkDeath();
        checkEat();
    }

    /**
     * Проверяет ест ли змейка яблоко.
     */
    private void checkEat() {
        if (canEatApple()) {
            eat();
        }
    }

    /**
     * имитирует процесс поедания яблоко змеёй.
     */
    private void eat() {
        apple = null;
        setScore(getScore() + 1);
        setGrowthNeeded(true);
    }

    /**
     * Проверяет, может ли змея съесть яблоко.
     * @return {@code true}, если может
     */
    private boolean canEatApple() {
        SnakeBlock ateElement = getApple();
        return canEat(ateElement);
    }

    /**
     * Проверяет, может ли змея съесть элемент.
     * @param ateElement Проверяемый элемент
     * @return {@code true}, если может
     */
    private boolean canEat(final SnakeBlock ateElement) {
        return inInterval(ateElement.getX(),
                getHead().getX() - SnakeBlock.SIZE / 2, getHead().getX()
                + SnakeBlock.SIZE / 2) && inInterval(ateElement.getY(),
                getHead().getY() - SnakeBlock.SIZE / 2, getHead().getY()
                + SnakeBlock.SIZE / 2);
    }

    /**
     * Проверяет, входит ли значение в интервал.
     * @param x Проверяемое значение
     * @param leftbound Левая граница интервала
     * @param rightbound Правая граница интервала
     * @return {@code true}, если входит
     */
    private boolean inInterval(final double x,
                               final double leftbound,
                               final double rightbound) {
        return x > leftbound && x < rightbound;
    }

    /**
     * Сдвигает блок.
     * @param block Сдвигаемый блок
     */
    private void moveBlock(final SnakeBlock block) {
        if (getDirection() == SnakeDirection.UP) {
            block.setY(block.getY() - SnakeBlock.SIZE);
        } else if (getDirection() == SnakeDirection.DOWN) {
            block.setY(block.getY() + SnakeBlock.SIZE);
        } else if (getDirection() == SnakeDirection.LEFT) {
            block.setX(block.getX() - SnakeBlock.SIZE);
        } else if (getDirection() == SnakeDirection.RIGHT) {
            block.setX(block.getX() + SnakeBlock.SIZE);
        }
    }

    /**
     * Проверяет, жива ли змейка.
     */
    private void checkDeath() {
        checkBounds();
        checkSnake();
    }

    /**
     * Проверяет, не откусила ли змейка кусок самой себя.
     */
    private void checkSnake() {
        for (SnakeBlock snakeBlock: getSnakeAsList()) {
            if (snakeBlock != getHead()) {
                if (canEat(snakeBlock)) {
                    die(false);
                }
            }
        }
    }

    /**
     * Проверяет, не врезалась ли змейка в границы поля.
     */
    private void checkBounds() {
        if (getHead().getX() > getRightBound()
                || getHead().getX() < getLeftBound()
                || getHead().getY() > getDownBound()
                || getHead().getY() < getUpBound()) {
            die(false);
        }
    }

    /**
     * Направление змейки.
     * @return Направление змейки.
     */
    public final SnakeDirection getDirection() {
        return direction;
    }

    /**
     * Устанавливает направление змейки.
     * @param snakeDirection Направление змейки.
     */
    public final void setDirection(final SnakeDirection snakeDirection) {
        if (!this.direction.oppositeTo(snakeDirection)) {
            this.direction = snakeDirection;
        }
    }

    /**
     * Устанавливает верхнюю границу.
     * @param upbound Верхняя граница
     */
    public final void setUpBound(final int upbound) {
        this.upBound = upbound;
    }

    /**
     * Верхняя граница.
     * @return Верхняя граница.
     */
    public final int getUpBound() {
        return upBound;
    }

    /**
     * Устанавливает нижнюю границу.
     * @param downbound Нижняя граница.
     */
    public final void setDownBound(final int downbound) {
        this.downBound = downbound;
    }

    /**
     * Нижняя граница.
     * @return Нижняя граница.
     */
    public final int getDownBound() {
        return downBound;
    }

    /**
     * Устанавливает правую границу.
     * @param rightbound Правая граница.
     */
    public final void setRightBound(final int rightbound) {
        this.rightBound = rightbound;
    }

    /**
     * Правая граница.
     * @return Правая граница
     */
    public final int getRightBound() {
        return rightBound;
    }

    /**
     * Устанавливает левую границу.
     * @param leftbound Левая граница
     */
    public final void setLeftBound(final int leftbound) {
        this.leftBound = leftbound;
    }

    /**
     * Левая граница.
     * @return Левая граница
     */
    public final int getLeftBound() {
        return leftBound;
    }

    /**
     * Голова змеи.
     * @return Голова змеи
     */
    public final SnakeBlock getHead() {
        return getSnakeAsList().get(0);
    }

    /**
     * Яблоко.
     * @return Яблоко
     */
    public final Apple getApple() {
        return apple;
    }

    /**
     * Хвост змеи.
     * @return Хвость змеи
     */
    public final SnakeBlock getTail() {
        return getSnakeAsList().get(getSnakeAsList().size() - 1);
    }

    /**
     * Количество набранных очков.
     * @return Количество набранных очков.
     */
    public final int getScore() {
        return score;
    }

    /**
     * Устанавливает количество набранных очков.
     * @param i Количество набранных очков
     */
    public final void setScore(final int i) {
        this.score = i;
    }

    /**
     * Устанавливает необходимость роста.
     * @param growthneeded Необходимость роста.
     */
    public final void setGrowthNeeded(final boolean growthneeded) {
        this.growthNeeded = growthneeded;
    }

    /**
     * Необходмость роста.
     * @return Необходимость роста.
     */
    public final boolean isGrowthNeeded() {
        return growthNeeded;
    }

    /**
     * Блок змеи.
     */
    private class SnakeBlock {
        /**
         * Размер блока.
         */
        public static final int SIZE = 10;

        /**
         * Координата x блока.
         */
        private double x;

        /**
         * Координата y блока.
         */
        private double y;

        /**
         * Создаёт блок змеи.
         * @param xCoord Координата x
         * @param yCoord Координата y
         */
        public SnakeBlock(final double xCoord, final double yCoord) {
            this.x = xCoord;
            this.y = yCoord;
        }

        /**
         * Координата x блока.
         * @return Координата x блока.
         */
        public double getX() {
            return x;
        }

        /**
         * Устанавливает координату x блока.
         * @param xCoord Координата x блока
         */
        public void setX(final double xCoord) {
            this.x = xCoord;
        }

        /**
         * Координата y блока.
         * @return Координата y блока.
         */
        public double getY() {
            return y;
        }

        /**
         * Устанавливает координату y блока.
         * @param yCoord Координата y блока
         */
        public void setY(final double yCoord) {
            this.y = yCoord;
        }
    }

    /**
     * Яблоко.
     */
    private class Apple extends SnakeBlock {

        /**
         * Генерирует яблоко в случайном месте.
         */
        public Apple() {
            super(0, 0);
            Random random = new Random();
            do {
                setX((getFromInterval(random,
                        (getLeftBound()),
                        (getRightBound())) / SIZE) * SIZE);
                setY((getFromInterval(random,
                        getUpBound(), getDownBound()) / SIZE) * SIZE);
            } while (isInSnake());
        }

        /**
         * Проверяет, находится ли яблоко внитри змеи.
         * @return {@code true}, если находится
         */
        private boolean isInSnake() {
            for (SnakeBlock block: getSnakeAsList()) {
                if (block.getX() == getX() && block.getY() == getY()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Получает значение из интервала.
         * @param random Генератор случайных чисел
         * @param leftbound Левая граница
         * @param rightbound Правая граница
         * @return Случайное значение из заданного интервала
         */
        private int getFromInterval(final Random random,
                                       final int leftbound,
                                       final int rightbound) {
            return leftbound + random.nextInt(rightbound - leftbound);
        }
    }
}
