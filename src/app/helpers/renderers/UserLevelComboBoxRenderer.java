/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.obj.UserLevel;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class UserLevelComboBoxRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -2783129511508919998L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            UserLevel ul = (UserLevel) value;
            setText(ul.getLevelCode() + " - " + ul.getLevelDisplayName());
        }

        setForeground(list.getForeground());
        setBackground(list.getBackground());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }

        return this;
    }

}
