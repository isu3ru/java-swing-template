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
public interface Callback {

    public void run();
    
    public void run(Object obj);

    public void run(Event e);

    public void run(ListSelectionEvent e);
}
