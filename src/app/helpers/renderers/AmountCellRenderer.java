/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.helpers.Utilities;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class AmountCellRenderer extends DefaultListCellRenderer {

    private static DecimalFormat def;
    private static final long serialVersionUID = -2783129511508919998L;

    public AmountCellRenderer() {
        def = new DecimalFormat("#0.00");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            setText(def.format(Utilities.getDoubleValue(value)));
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
