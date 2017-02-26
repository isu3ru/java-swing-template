/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import app.obj.Privilege;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class SystemPrivilegesRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -3249595690531498163L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            Privilege ul = (Privilege) value;
            setText(ul.getPrvDisplayName().toUpperCase());
            ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/app/resources/key.png"));
            setIcon(imageIcon);
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
