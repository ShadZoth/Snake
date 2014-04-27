package ru.hse.se.g272.ervo.ooaip.snake;

import ru.hse.se.g272.ervo.ooaip.Form;
import ru.hse.se.g272.ervo.ooaip.TimerForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Форма для игры в "змейку".
 *
 * @author Эрво Виктор
 * @since 11 марта 2014
 */
public class SnakeForm extends TimerForm {

    /**
     * Код клавиши ESCAPE.
     */
    public static final int ESC = 27;

    /**
     * игра "Змейка".
     */
    private SnakeGame snakeGame;

    /**
     * Запрос на изменение направления.
     */
    private ChangeDirectionQuery changeDirectionQuery
            = new ChangeDirectionQuery();

    /**
     * Запрос на паузу игры.
     */
    private PauseQuery pauseQuery = new PauseQuery();

    /**
     * Создаёт форму для игры в змейку.
     */
    public SnakeForm() {
        setTitle("Snake");
        setResizable(false);
        snakeGame = new SnakeGame();
        add(snakeGame);
        KeyListener keyListener = new KeyListener() {
            private void handleThis(final KeyEvent keyEvent) {
                if (keyEvent.getExtendedKeyCode() == ESC) {
                    getPauseQuery().setPauseNeeded(true);
                } else {
                    try {
                        getChangeDirectionQuery().setDirection(
                                directionFromEvent(keyEvent));
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage()
                                + ", но это не страшно");
                    }
                }
            }

            private SnakeDirection directionFromEvent(final KeyEvent keyEvent) {
                int extendedKeyCode = keyEvent.getExtendedKeyCode();
                if (extendedKeyCode == SnakeDirection.DOWN.getValue()) {
                    System.out.println("DOWN");
                    return SnakeDirection.DOWN;
                } else if (extendedKeyCode == SnakeDirection.LEFT.getValue()) {
                    System.out.println("LEFT");
                    return SnakeDirection.LEFT;
                } else if (extendedKeyCode == SnakeDirection.RIGHT.getValue()) {
                    System.out.println("RIGHT");
                    return SnakeDirection.RIGHT;
                } else if (extendedKeyCode == SnakeDirection.UP.getValue()) {
                    System.out.println("UP");
                    return SnakeDirection.UP;
                } else {
                    throw new IllegalArgumentException(
                            "Клавиша не задаёт направление");
                }
            }

            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(final KeyEvent keyEvent) {
                handleThis(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
        addKeyListener(keyListener);
    }

    /**
     * Метод, вызываемый при запуске программы.
     * @param args Аргументы командной строки
     */
    public static void main(final String[] args) {
        final SnakeForm form = new SnakeForm();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.pack();
        form.setVisible(true);
        form.setDefaultSize(Form.HALFSCREEN);
        form.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                if (form.getPauseQuery().isPauseNeeded()) {
                    JOptionPane.showMessageDialog(null, "Пауза");
                    form.getPauseQuery().setPauseNeeded(false);
                }
                form.getSnakeGame().moveSnake();
                form.setTitle("Snake. Score: "
                        + form.getSnakeGame().getScore());
                form.getSnakeGame()
                        .setDirection(form
                                .getChangeDirectionQuery()
                                .getDesiredDirection());
                form.repaint();
            }
        });
        form.setActioning(true);
    }

    /**
     * игра "Змейка".
     * @return игра "Змейка"
     */
    public final SnakeGame getSnakeGame() {
        return snakeGame;
    }

    /**
     * Запрос на изменение направления.
     * @return Запрос на изменение направления.
     */
    public final ChangeDirectionQuery getChangeDirectionQuery() {
        return changeDirectionQuery;
    }

    /**
     * Запрос на паузу.
     * @return Запрос на паузу.
     */
    public final PauseQuery getPauseQuery() {
        return pauseQuery;
    }

    /**
     * Запрос на изменение направления.
     */
    private class ChangeDirectionQuery {
        /**
         * Необходимое направление.
         */
        private SnakeDirection desiredDirection = SnakeDirection.UP;

        /**
         * Устанавливает необходимое направление.
         * @param snakeDirection Необходимое направление
         */
        public void setDirection(final SnakeDirection snakeDirection) {
            this.desiredDirection = snakeDirection;
        }

        /**
         * Необходимое направление.
         * @return Необходимое направление.
         */
        public SnakeDirection getDesiredDirection() {
            return desiredDirection;
        }

    }

    /**
     * Запрос на паузу.
     */
    private class PauseQuery {
        /**
         * Необходимость паузы.
         */
        private boolean pauseNeeded;

        /**
         * Устанавливает необходимость паузы.
         * @param pauseneeded Необходимость паузы
         */
        public void setPauseNeeded(final boolean pauseneeded) {
            this.pauseNeeded = pauseneeded;
        }

        /**
         * Необходимлсть паузы.
         * @return Необходимлсть паузы.
         */
        public boolean isPauseNeeded() {
            return pauseNeeded;
        }
    }
}
