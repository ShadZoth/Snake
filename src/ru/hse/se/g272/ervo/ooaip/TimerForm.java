package ru.hse.se.g272.ervo.ooaip;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Форма, выполняющая что-либо по таймеру.
 *
 * @author Эрво Виктор
 * @since 18.03.14
 */
public class TimerForm extends Form {
    /**
     * Default delay for timer.
     */
    private static final int DEFAULT_DELAY = 200;
    /**
     * Delay of timer.
     */
    private int delay = DEFAULT_DELAY;
    /**
     * Is working.
     */
    private boolean actioning = false;
    /**
     * Special timer.
     */
    private Timer timer;
    /**
     * Action listener of timer.
     */
    private ActionListener actionListener = actionEvent -> {
    };

    /**
     * Checks if timer is started.
     * @return state of timer
     */
    public final boolean isActioning() {
        return actioning;
    }

    /**
     * Starts or stops timer.
     * @param b state of timer
     */
    public final void setActioning(final boolean b) {
        if (b) {
            if (isActioning()) {
                timer.stop();
            }
            timer = new Timer(getDelay(), getActionListener());
            timer.start();
        } else if (timer != null) {
            timer.stop();
        }
        this.actioning = b;
    }

    /**
     * Delay of timer.
     * @return delay of timer
     */
    public final int getDelay() {
        return delay;
    }

    /**
     * Sets delay of timer.
     * @param i delay of timer
     */
    public final void setDelay(final int i) {
        this.delay = i;
        setActioning(isActioning());
    }

    /**
     * Action that form performs when actioning.
     * @return action listener
     */
    public final ActionListener getActionListener() {
        return actionListener;
    }

    /**
     * Sets action that form performs when actioning.
     * @param listener action listener
     */
    public final void setActionListener(final ActionListener listener) {
        this.actionListener = listener;
        setActioning(isActioning());
    }
}
