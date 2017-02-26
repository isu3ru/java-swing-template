/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers.renderers;

import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author Isuru
 */
public class ReportSelectionListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -3249595690531498163L;

    private String icon_resource;
    private String icon_resource_selected;

    public ReportSelectionListCellRenderer() {
    }

    public ReportSelectionListCellRenderer(String icon_resource, String icon_resource_selected) {
        this.icon_resource = icon_resource;
        this.icon_resource_selected = icon_resource_selected;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            setText(value.toString());
            ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/app/resources/" + icon_resource));
            setIcon(imageIcon);
            setFont(getFont().deriveFont(Font.PLAIN));
        }

        setForeground(list.getForeground());
        setBackground(list.getBackground());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/app/resources/" + icon_resource_selected));
            setIcon(imageIcon);
            setFont(getFont().deriveFont(Font.BOLD));
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
