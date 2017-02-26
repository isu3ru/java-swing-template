/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.helpers.Utilities;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Isuru
 */
public class ProgressBarCellRenderer extends DefaultTableCellRenderer {

    JProgressBar prog;

    public ProgressBarCellRenderer() {
        prog = new JProgressBar();
        prog.setMinimum(0);
        prog.setMaximum(100);
        prog.setValue(0);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setForeground(table.getForeground());
        setBackground(table.getBackground());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }

        prog.setValue(0);

        if (value != null) {
            prog.setStringPainted(true);
            prog.setValue(Utilities.getIntegerValue(value));
        }

        return prog;
    }

}
