/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.helpers.Utilities;
import java.awt.Component;
import java.util.Date;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class DateComboBoxRenderer extends DefaultListCellRenderer {


    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            Date d = (Date) value;
            String str = Utilities.formatDateForSQL(d);
            setText(str);
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
