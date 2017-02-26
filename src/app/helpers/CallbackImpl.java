/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

import java.awt.Event;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Isuru
 */
public abstract class CallbackImpl implements Callback {

    @Override
    public void run() {
    }

    @Override
    public void run(Object obj) {
    }

    @Override
    public void run(Event e) {
    }

    @Override
    public void run(ListSelectionEvent e) {
    }

}
