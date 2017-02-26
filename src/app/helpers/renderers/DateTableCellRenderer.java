/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Isuru
 */
public class DateTableCellRenderer extends DefaultTableCellRenderer {

    private static SimpleDateFormat def;

    public DateTableCellRenderer() {
        def = new SimpleDateFormat("yyyy-MM-dd");
    }

    public DateTableCellRenderer(String format) {
        def = new SimpleDateFormat(format);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            Date d = (Date) value;
            setText(def.format(d));
        }
        setForeground(table.getForeground());
        setBackground(table.getBackground());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }
        return this;
    }

}
