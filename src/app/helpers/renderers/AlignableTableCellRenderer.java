/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Isuru
 */
public class AlignableTableCellRenderer extends DefaultTableCellRenderer {

    private int alignment = SwingConstants.LEFT;

    public AlignableTableCellRenderer() {
        super();
    }

    public AlignableTableCellRenderer(int alignment) {
        super();
        this.alignment = alignment;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setForeground(table.getForeground());
        setBackground(table.getBackground());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }

        if (value != null) {
            setText(value.toString());
        }

        setHorizontalAlignment(alignment);
        return this;
    }

}
