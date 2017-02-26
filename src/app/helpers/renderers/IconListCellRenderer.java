/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class IconListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -3249595690531498163L;

    private String icon_resource;

    public IconListCellRenderer() {
    }

    public IconListCellRenderer(String icon_resource) {
        this.icon_resource = icon_resource;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            setText(value.toString());
            ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/app/resources/" + icon_resource));
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

    public String getIcon_resource() {
        return icon_resource;
    }

    public void setIcon_resource(String icon_resource) {
        this.icon_resource = icon_resource;
    }

}
