/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.helpers.Utilities;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Isuru
 */
public class AmountTableCellRenderer extends DefaultTableCellRenderer {

    private static DecimalFormat def;

    public AmountTableCellRenderer() {
        def = new DecimalFormat("#0.00");
        setHorizontalAlignment(TRAILING);
    }

    public AmountTableCellRenderer(int alignment) {
        def = new DecimalFormat("#0.00");
        setHorizontalAlignment(alignment);
    }
    
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            setText(def.format(Utilities.getDoubleValue(value)));
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
