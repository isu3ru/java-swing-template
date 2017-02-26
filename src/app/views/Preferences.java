/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.views;

import app.controllers.UserController;
import app.db.backup.DatabaseUtils;
import app.helpers.Updator;
import app.helpers.Utilities;
import app.helpers.renderers.SystemPrivilegesRenderer;
import app.helpers.renderers.UserLevelComboBoxRenderer;
import app.obj.Privilege;
import app.obj.User;
import app.obj.UserLevel;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Isuru
 */
public class Preferences extends javax.swing.JDialog {

    User editing_user_;

    char echoc;
    char emptyc;

    String bkpfile;
    String bkppath;

    /**
     * Creates new form Privileges
     */
    public Preferences(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ulevel_combo.setRenderer(new UserLevelComboBoxRenderer());
        cmb_user_levels.setRenderer(new UserLevelComboBoxRenderer());
        ul_available.setCellRenderer(new SystemPrivilegesRenderer());
        ul_assigned.setCellRenderer(new SystemPrivilegesRenderer());
        u_available.setCellRenderer(new SystemPrivilegesRenderer());
        u_assigned.setCellRenderer(new SystemPrivilegesRenderer());
        echoc = tf_password.getEchoChar();
        emptyc = 0;
        resetUserPanel();
        resetUserLevelPrivilegesPanel();
        resetUserPrivilegesPanel();
    }

    /*USER MODULE*/
    private void loadUserTypesCombo() {
        User.UserType ty = (User.UserType) userTypesCombo.getSelectedItem();
        User.UserType[] utypes = User.UserType.values();
        DefaultComboBoxModel def = (DefaultComboBoxModel) userTypesCombo.getModel();
        def.removeAllElements();
        for (User.UserType utype : utypes) {
            def.addElement(utype);
        }
        if (ty != null) {
            userTypesCombo.setSelectedItem(ty);
        }
    }

    private void loadUserLevelCombo() {
        UserLevel ul = (UserLevel) ulevel_combo.getSelectedItem();
        DefaultComboBoxModel def = (DefaultComboBoxModel) ulevel_combo.getModel();
        def.removeAllElements();
        List<UserLevel> userLevelsList = UserController.getUserLevelsList();
        for (UserLevel ul1 : userLevelsList) {
            def.addElement(ul1);
        }
        if (ul != null) {
            ulevel_combo.setSelectedItem(ul);
        }
    }

    private void loadUserLevelCombo_Privileges() {
        UserLevel ul = (UserLevel) cmb_user_levels.getSelectedItem();
        DefaultComboBoxModel def = (DefaultComboBoxModel) cmb_user_levels.getModel();
        def.removeAllElements();
        List<UserLevel> userLevelsList = UserController.getUserLevelsList();
        for (UserLevel ul1 : userLevelsList) {
            def.addElement(ul1);
        }
        if (ul != null) {
            cmb_user_levels.setSelectedItem(ul);
        }
    }

    private void loadUserLevelPrivilegesAvailableList() {
        UserLevel ul = (UserLevel) cmb_user_levels.getSelectedItem();
        if (ul != null) {
            ArrayList<Privilege> sps = UserController.getAvailableUserLevelPrivileges(ul);
            ul_available.setListData(sps.toArray());
        }
    }

    private void loadUserLevelPrivilegesAssignedList() {
        UserLevel ul = (UserLevel) cmb_user_levels.getSelectedItem();
        if (ul != null) {
            ArrayList<Privilege> sps = UserController.getAssignedUserLevelPrivileges(ul);
            ul_assigned.setListData(sps.toArray());
        }
    }

    private int getUserActiveStatus() {
        return rb_active_user.isSelected() ? 1 : 0;
    }

    private void setUserActiveStatus(int st) {
        if (st == 1) {
            rb_active_user.setSelected(true);
        } else {
            rb_inactive_user.setSelected(true);
        }
    }

    private void loadUsersTable() {
        ArrayList<User> usersList = UserController.getUsersList();
        if (usersList.size() > 0) {
            DefaultTableModel def = (DefaultTableModel) users_table.getModel();
            def.setNumRows(0);

            for (User user : usersList) {
                String userName = user.getUserName();
                String userDisplayName = user.getUserDisplayName();
                UserLevel userLevel = user.getUserLevel();
                Integer userType = user.getUserType();
                Integer userStatus = user.getUserStatus();

                Vector v = new Vector();
                v.add(user);
                v.add(userDisplayName);
                v.add(userLevel.getLevelDisplayName());
                v.add(userType == 1 ? "Permanent" : "Temporary");
                v.add((userStatus == 1));
                def.addRow(v);
            }
        }
    }

    private void loadUsersCombo_Privileges() {
        ArrayList<User> usersList = UserController.getUsersList();
        if (usersList.size() > 0) {
            DefaultComboBoxModel ucmod = (DefaultComboBoxModel) cmb_users.getModel();
            ucmod.removeAllElements();

            for (User usr : usersList) {
                ucmod.addElement(usr);
            }
        }
    }

    private void loadUserPrivilegesAvailableList() {
        User u = (User) cmb_users.getSelectedItem();
        if (u != null) {
            ArrayList<Privilege> sps = UserController.getAvailableUserPrivileges(u);
            u_available.setListData(sps.toArray());
        }
    }

    private void loadUserPrivilegesAssignedList() {
        User u = (User) cmb_users.getSelectedItem();
        if (u != null) {
            ArrayList<Privilege> sps = UserController.getAssignedUserPrivileges(u);
            u_assigned.setListData(sps.toArray());
        }
    }

    private void resetUserPanel() {
        loadUserLevelCombo();
        loadUserTypesCombo();
        loadUsersTable();
        tf_username.setText("");
        tf_display_name.setText("");
        setUserActiveStatus(0);
        tf_password.setText("");
        tf_password.setEnabled(true);
        showhidepass.setSelected(false);
        showhidepass.setEnabled(true);
        upd_acc_pass.setEnabled(false);
        delete_acc.setEnabled(false);
        create_acc.setText("Create User Account");
    }

    private void resetUserLevelPrivilegesPanel() {
        loadUserLevelCombo_Privileges();
        loadUserLevelPrivilegesAvailableList();
        loadUserLevelPrivilegesAssignedList();
    }

    private void resetUserPrivilegesPanel() {
        loadUsersCombo_Privileges();
        loadUserPrivilegesAvailableList();
        loadUserPrivilegesAssignedList();
    }

    private boolean isPasswordOK() {
        return (tf_new_password.getPassword().length > 0 && tf_new_password_again.getPassword().length > 0)
                && (new String(tf_new_password.getPassword()).equals(new String(tf_new_password_again.getPassword())));
    }

    /*END:USER MODULE*/

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settings_list_buttons = new javax.swing.ButtonGroup();
        user_levels_editor = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        pass_editor = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        tf_new_password = new javax.swing.JPasswordField();
        jLabel13 = new javax.swing.JLabel();
        tf_new_password_again = new javax.swing.JPasswordField();
        charges_entry_editor = new javax.swing.JPanel();
        is_unlimited = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        range_start = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        range_end = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        unit_price = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        fixed_price = new javax.swing.JFormattedTextField();
        tax_entry_editor = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        tf_tax_name = new javax.swing.JTextField();
        tf_tax_rate = new javax.swing.JFormattedTextField();
        blockingDialog = new javax.swing.JDialog();
        jLabel31 = new javax.swing.JLabel();
        jButtonBar1 = new com.l2fprod.common.swing.JButtonBar();
        general_toggle = new javax.swing.JToggleButton();
        users_toggle = new javax.swing.JToggleButton();
        cards_base = new javax.swing.JPanel();
        general_settings_card = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        current_reading_old = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jXTitledSeparator1 = new org.jdesktop.swingx.JXTitledSeparator();
        jXTitledSeparator6 = new org.jdesktop.swingx.JXTitledSeparator();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        users_settings_card = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tf_display_name = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ulevel_combo = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        tf_password = new javax.swing.JPasswordField();
        showhidepass = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        rb_active_user = new javax.swing.JRadioButton();
        rb_inactive_user = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        userTypesCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        create_acc = new javax.swing.JButton();
        delete_acc = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jXSearchField1 = new org.jdesktop.swingx.JXSearchField();
        jScrollPane1 = new javax.swing.JScrollPane();
        users_table = new org.jdesktop.swingx.JXTable();
        upd_acc_pass = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cmb_user_levels = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ul_available = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ul_assigned = new javax.swing.JList();
        jPanel7 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cmb_users = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        u_available = new javax.swing.JList();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        u_assigned = new javax.swing.JList();
        jPanel11 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();

        javax.swing.GroupLayout user_levels_editorLayout = new javax.swing.GroupLayout(user_levels_editor);
        user_levels_editor.setLayout(user_levels_editorLayout);
        user_levels_editorLayout.setHorizontalGroup(
            user_levels_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        user_levels_editorLayout.setVerticalGroup(
            user_levels_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );

        jLabel12.setText("Please enter your new password: ");

        jLabel13.setText("Please enter your password AGAIN: ");

        javax.swing.GroupLayout pass_editorLayout = new javax.swing.GroupLayout(pass_editor);
        pass_editor.setLayout(pass_editorLayout);
        pass_editorLayout.setHorizontalGroup(
            pass_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(tf_new_password)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(tf_new_password_again)
        );
        pass_editorLayout.setVerticalGroup(
            pass_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pass_editorLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_new_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_new_password_again, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        charges_entry_editor.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                charges_entry_editorAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        is_unlimited.setText("Check if an unlimited entry");

        jLabel23.setText("Range Start : ");

        range_start.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                range_startKeyPressed(evt);
            }
        });

        jLabel24.setText("Range End : ");

        jLabel25.setText("Unit Price : ");

        unit_price.setBackground(new java.awt.Color(250, 255, 208));
        unit_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        unit_price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unit_price.setText("0.00");

        jLabel26.setText("Fixed Charge : ");

        fixed_price.setBackground(new java.awt.Color(212, 255, 208));
        fixed_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        fixed_price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fixed_price.setText("0.00");

        javax.swing.GroupLayout charges_entry_editorLayout = new javax.swing.GroupLayout(charges_entry_editor);
        charges_entry_editor.setLayout(charges_entry_editorLayout);
        charges_entry_editorLayout.setHorizontalGroup(
            charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(charges_entry_editorLayout.createSequentialGroup()
                .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(charges_entry_editorLayout.createSequentialGroup()
                        .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(range_start, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unit_price, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(range_end, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fixed_price, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(is_unlimited))
                .addGap(0, 0, 0))
        );
        charges_entry_editorLayout.setVerticalGroup(
            charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(charges_entry_editorLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(is_unlimited)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(range_start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(range_end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(charges_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(unit_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(fixed_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel27.setText("Tax Name : ");

        jLabel28.setText("Tax Rate (%) : ");

        tf_tax_rate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_tax_rate.setText("0.00");

        javax.swing.GroupLayout tax_entry_editorLayout = new javax.swing.GroupLayout(tax_entry_editor);
        tax_entry_editor.setLayout(tax_entry_editorLayout);
        tax_entry_editorLayout.setHorizontalGroup(
            tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tax_entry_editorLayout.createSequentialGroup()
                .addGroup(tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_tax_name)
                    .addGroup(tax_entry_editorLayout.createSequentialGroup()
                        .addComponent(tf_tax_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 58, Short.MAX_VALUE))))
        );
        tax_entry_editorLayout.setVerticalGroup(
            tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tax_entry_editorLayout.createSequentialGroup()
                .addGroup(tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(tf_tax_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tax_entry_editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(tf_tax_rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        blockingDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        blockingDialog.setTitle("Backup");
        blockingDialog.setUndecorated(true);
        blockingDialog.setResizable(false);

        jLabel31.setBackground(new java.awt.Color(0, 0, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/loding_tea_cup.GIF"))); // NOI18N
        jLabel31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel31.setOpaque(true);

        javax.swing.GroupLayout blockingDialogLayout = new javax.swing.GroupLayout(blockingDialog.getContentPane());
        blockingDialog.getContentPane().setLayout(blockingDialogLayout);
        blockingDialogLayout.setHorizontalGroup(
            blockingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );
        blockingDialogLayout.setVerticalGroup(
            blockingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");
        setResizable(false);

        jButtonBar1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        com.l2fprod.common.swing.PercentLayout percentLayout1 = new com.l2fprod.common.swing.PercentLayout();
        percentLayout1.setGap(2);
        percentLayout1.setOrientation(1);
        jButtonBar1.setLayout(percentLayout1);

        settings_list_buttons.add(general_toggle);
        general_toggle.setSelected(true);
        general_toggle.setText("General Settings");
        general_toggle.setPreferredSize(new java.awt.Dimension(100, 50));
        general_toggle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                general_toggleItemStateChanged(evt);
            }
        });
        jButtonBar1.add(general_toggle);

        settings_list_buttons.add(users_toggle);
        users_toggle.setText("Users Settings");
        users_toggle.setPreferredSize(new java.awt.Dimension(100, 50));
        users_toggle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                users_toggleItemStateChanged(evt);
            }
        });
        jButtonBar1.add(users_toggle);

        cards_base.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        cards_base.setLayout(new java.awt.CardLayout());

        general_settings_card.setBackground(java.awt.Color.white);
        general_settings_card.setName("general_settings_card"); // NOI18N

        jLabel5.setBackground(new java.awt.Color(46, 46, 46));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("General Settings");
        jLabel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(242, 130, 27)), javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 10)));
        jLabel5.setOpaque(true);

        jLabel3.setText("Location : ");

        current_reading_old.setBackground(new java.awt.Color(245, 246, 212));
        current_reading_old.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(210, 211, 190)), javax.swing.BorderFactory.createEmptyBorder(2, 3, 2, 3)));
        current_reading_old.setOpaque(true);

        jButton9.setText("Browse");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Open Backup Location");

        jButton11.setText("Backup");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jXTitledSeparator1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jXTitledSeparator1.setTitle("Database Backups");

        jXTitledSeparator6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jXTitledSeparator6.setTitle("Software Updates");

        jButton23.setText("Check for updates");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setText("Update Settings");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout general_settings_cardLayout = new javax.swing.GroupLayout(general_settings_card);
        general_settings_card.setLayout(general_settings_cardLayout);
        general_settings_cardLayout.setHorizontalGroup(
            general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(general_settings_cardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTitledSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(general_settings_cardLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(general_settings_cardLayout.createSequentialGroup()
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton10))
                            .addGroup(general_settings_cardLayout.createSequentialGroup()
                                .addComponent(current_reading_old, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9))))
                    .addGroup(general_settings_cardLayout.createSequentialGroup()
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton24)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        general_settings_cardLayout.setVerticalGroup(
            general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(general_settings_cardLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jXTitledSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(current_reading_old)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addComponent(jXTitledSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(general_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton23)
                    .addComponent(jButton24))
                .addGap(0, 308, Short.MAX_VALUE))
        );

        general_settings_cardLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {current_reading_old, jButton9});

        cards_base.add(general_settings_card, "general_settings_card");
        general_settings_card.getAccessibleContext().setAccessibleName("general_settings_card");

        users_settings_card.setBackground(java.awt.Color.white);
        users_settings_card.setName("users_settings_card"); // NOI18N

        jLabel4.setBackground(new java.awt.Color(46, 46, 46));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Users Settings");
        jLabel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(242, 130, 27)), javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 10)));
        jLabel4.setOpaque(true);

        jLabel1.setText("Username : ");

        jLabel2.setText("Display Name : ");

        jLabel7.setText("User Level : ");

        jButton1.setText("Edit User Levels");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Password : ");

        tf_password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tf_passwordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tf_passwordFocusLost(evt);
            }
        });

        showhidepass.setText("Show Password");
        showhidepass.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showhidepassItemStateChanged(evt);
            }
        });

        jLabel10.setText("User Status : ");

        buttonGroup1.add(rb_active_user);
        rb_active_user.setSelected(true);
        rb_active_user.setText("Active");

        buttonGroup1.add(rb_inactive_user);
        rb_inactive_user.setText("Inactive");

        jLabel9.setText("User Type : ");

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jPanel1.add(filler1);

        create_acc.setText("Create User Account");
        create_acc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_accActionPerformed(evt);
            }
        });
        jPanel1.add(create_acc);

        delete_acc.setText("Delete User Account");
        delete_acc.setEnabled(false);
        delete_acc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_accActionPerformed(evt);
            }
        });
        jPanel1.add(delete_acc);

        jPanel5.setLayout(new java.awt.BorderLayout(0, 5));
        jPanel5.add(jXSearchField1, java.awt.BorderLayout.PAGE_END);

        users_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Display Name", "User Level", "User Type", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        users_table.getTableHeader().setReorderingAllowed(false);
        users_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                users_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(users_table);
        if (users_table.getColumnModel().getColumnCount() > 0) {
            users_table.getColumnModel().getColumn(4).setMinWidth(25);
            users_table.getColumnModel().getColumn(4).setPreferredWidth(25);
            users_table.getColumnModel().getColumn(4).setMaxWidth(25);
        }

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        upd_acc_pass.setText("Update Account Password");
        upd_acc_pass.setEnabled(false);
        upd_acc_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upd_acc_passActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_display_name))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(ulevel_combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rb_active_user)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rb_inactive_user)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(upd_acc_pass))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tf_password, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(showhidepass)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userTypesCombo, 0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tf_password, tf_username});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tf_display_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(ulevel_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(tf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showhidepass)
                    .addComponent(jLabel9)
                    .addComponent(userTypesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(rb_active_user)
                    .addComponent(rb_inactive_user)
                    .addComponent(upd_acc_pass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Users", jPanel2);

        jLabel14.setText("Select User Level : ");

        cmb_user_levels.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_user_levelsItemStateChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available Privileges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        ul_available.setToolTipText("<html>Use <strong>'Ctrl'</strong> key to select multiple items.");
        ul_available.setFixedCellHeight(35);
        jScrollPane2.setViewportView(ul_available);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Assigned Privileges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        ul_assigned.setToolTipText("<html>Use <strong>'Ctrl'</strong> key to select multiple items.");
        ul_assigned.setFixedCellHeight(35);
        jScrollPane3.setViewportView(ul_assigned);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/key_add.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/key_delete.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(119, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jLabel16.setBackground(new java.awt.Color(46, 46, 46));
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/lightbulb.png"))); // NOI18N
        jLabel16.setText("<html><strong>Help: </strong> Privileges that you assign for a selected user level in here, can be edited via the user privileges menu.<br /><strong>Please note that you must re-edit overridden privileges after a user level change.</strong>");
        jLabel16.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jLabel16.setOpaque(true);

        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_user_levels, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cmb_user_levels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("User Level Privileges", jPanel4);

        jLabel15.setText("Select User : ");

        cmb_users.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_usersItemStateChanged(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available Privileges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        u_available.setFixedCellHeight(35);
        jScrollPane4.setViewportView(u_available);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Assigned Privileges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        u_assigned.setFixedCellHeight(35);
        jScrollPane5.setViewportView(u_assigned);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/key_add.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/key_delete.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setBackground(new java.awt.Color(46, 46, 46));
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/lightbulb.png"))); // NOI18N
        jLabel17.setText("<html><strong>Help: </strong> Privileges that you assign for a selected user in here, will be cleared when the user level is changed.<br /> <strong>Please note that you must re-assign privileges after a user level change for a user.</strong>");
        jLabel17.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jLabel17.setOpaque(true);

        jButton8.setText("Reset");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_users, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel17))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cmb_users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("User Privileges", jPanel8);

        javax.swing.GroupLayout users_settings_cardLayout = new javax.swing.GroupLayout(users_settings_card);
        users_settings_card.setLayout(users_settings_cardLayout);
        users_settings_cardLayout.setHorizontalGroup(
            users_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(users_settings_cardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        users_settings_cardLayout.setVerticalGroup(
            users_settings_cardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(users_settings_cardLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        cards_base.add(users_settings_card, "users_settings_card");
        users_settings_card.getAccessibleContext().setAccessibleName("users_settings_card");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cards_base, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cards_base, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void users_toggleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_users_toggleItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            CardLayout cl = (CardLayout) cards_base.getLayout();
            cl.show(cards_base, "users_settings_card");
        }
    }//GEN-LAST:event_users_toggleItemStateChanged

    private void general_toggleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_general_toggleItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            CardLayout cl = (CardLayout) cards_base.getLayout();
            cl.show(cards_base, "general_settings_card");
        }
    }//GEN-LAST:event_general_toggleItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JOptionPane.showMessageDialog(this, new UserLevels(), "User Levels Editor", JOptionPane.PLAIN_MESSAGE);
        loadUserLevelCombo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void create_accActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_accActionPerformed
        if (editing_user_ == null) {
            UserLevel ul = (UserLevel) ulevel_combo.getSelectedItem();
            User.UserType ty = (User.UserType) userTypesCombo.getSelectedItem();
            User user = new User();
            user.setUserName(tf_username.getText().trim());
            user.setUserDisplayName(tf_display_name.getText().trim());
            user.setUserLevel(ul);
            user.setUserPassword(new String(tf_password.getPassword()));
            user.setUserType(ty.getUtid());
            user.setUserStatus(getUserActiveStatus());
            boolean createUser = UserController.createUser(user);
            if (createUser) {
                loadUsersTable();
                JOptionPane.showMessageDialog(this, "User account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot create user account. Please try again.", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            int i = JOptionPane.showConfirmDialog(this, "You are about to change the user " + editing_user_.getUserDisplayName() + " account.\nAre you sure you need to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == JOptionPane.YES_OPTION) {
                UserLevel ul = (UserLevel) ulevel_combo.getSelectedItem();
                User.UserType ty = (User.UserType) userTypesCombo.getSelectedItem();

                editing_user_.setUserName(tf_username.getText().trim());
                editing_user_.setUserDisplayName(tf_display_name.getText().trim());
                editing_user_.setUserLevel(ul);
                editing_user_.setUserPassword(new String(tf_password.getPassword()));
                editing_user_.setUserType(ty.getUtid());
                editing_user_.setUserStatus(getUserActiveStatus());

                boolean updateUser = UserController.updateUser(editing_user_);
                if (updateUser) {
                    loadUsersTable();
                    JOptionPane.showMessageDialog(this, "User account changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot change user account. Please try again.", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                resetUserPanel();
            }
        }
    }//GEN-LAST:event_create_accActionPerformed

    private void users_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_users_tableMouseClicked
        if (evt.getClickCount() >= 2) {
            int sr = users_table.getSelectedRow();
            if (sr > -1) {
                editing_user_ = (User) users_table.getValueAt(sr, 0);
                tf_username.setText(editing_user_.getUserName());
                tf_display_name.setText(editing_user_.getUserDisplayName());
                ulevel_combo.setSelectedItem(editing_user_.getUserLevel());
                userTypesCombo.setSelectedItem(editing_user_.getUserType() == 1 ? User.UserType.PERMANENT : User.UserType.TEMPORARY);
                setUserActiveStatus(editing_user_.getUserStatus());
                tf_password.setText("Passwords cannot be edited.");
                tf_password.setEnabled(false);
                upd_acc_pass.setEnabled(true);
                delete_acc.setEnabled(true);
                create_acc.setText("Save Changes");
            }
        }
    }//GEN-LAST:event_users_tableMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        resetUserPanel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void delete_accActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_accActionPerformed
        String curr_user = System.getProperty("userid");
        if (curr_user.equals(editing_user_.getUserId().toString())) {
            JOptionPane.showMessageDialog(this, "Editing your own account is not allowed.\nPlease use the root user account to delete the account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean del = UserController.deleteUser(editing_user_);
        if (del) {
            JOptionPane.showMessageDialog(this, "User removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, "User removal failed.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_delete_accActionPerformed

    private void upd_acc_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upd_acc_passActionPerformed
        int ii = JOptionPane.showConfirmDialog(this, pass_editor, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ii == JOptionPane.OK_OPTION) {
            if (isPasswordOK()) {
                String new_pass = new String(tf_new_password.getPassword());
                String new_pass_again = new String(tf_new_password_again.getPassword());

                boolean updateUserPassword = UserController.updateUserPassword(editing_user_, new_pass_again);
                if (updateUserPassword) {
                    JOptionPane.showMessageDialog(this, "Password for user " + editing_user_.getUserDisplayName() + " updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter valid information to process you request.", "Invalid", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_upd_acc_passActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // add to assigned list
        UserLevel ul = (UserLevel) cmb_user_levels.getSelectedItem();
        if (ul_available.getSelectedIndices().length > 0) {
            ArrayList<Privilege> svl = (ArrayList<Privilege>) ul_available.getSelectedValuesList();
            if (svl.size() > 0) {
                boolean assigned = UserController.assignUserLevelPrivileges(ul, svl);
                if (assigned) {
                    loadUserLevelPrivilegesAvailableList();
                    loadUserLevelPrivilegesAssignedList();
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // remove from assigned list
        UserLevel ul = (UserLevel) cmb_user_levels.getSelectedItem();
        if (ul_assigned.getSelectedIndices().length > 0) {
            ArrayList<Privilege> svl = (ArrayList<Privilege>) ul_assigned.getSelectedValuesList();
            if (svl.size() > 0) {
                boolean removed = UserController.removeUserLevelPrivileges(ul, svl);
                if (removed) {
                    loadUserLevelPrivilegesAvailableList();
                    loadUserLevelPrivilegesAssignedList();
                }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tf_passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tf_passwordFocusGained
        // change echo char to 0 - show as plain text
        tf_password.setEchoChar(emptyc);
    }//GEN-LAST:event_tf_passwordFocusGained

    private void tf_passwordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tf_passwordFocusLost
        // change to password echo char - hide password
        tf_password.setEchoChar(echoc);
    }//GEN-LAST:event_tf_passwordFocusLost

    private void showhidepassItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showhidepassItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            tf_password.setEchoChar(emptyc);
        } else {
            tf_password.setEchoChar(echoc);
        }
    }//GEN-LAST:event_showhidepassItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // add to assigned list
        User u = (User) cmb_users.getSelectedItem();
        if (u_available.getSelectedIndices().length > 0) {
            ArrayList<Privilege> svl = (ArrayList<Privilege>) u_available.getSelectedValuesList();
            if (svl.size() > 0) {
                boolean assigned = UserController.assignUserPrivileges(u, svl);
                if (assigned) {
                    loadUserPrivilegesAvailableList();
                    loadUserPrivilegesAssignedList();
                }
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // remove from assigned list
        User u = (User) cmb_users.getSelectedItem();
        if (u_assigned.getSelectedIndices().length > 0) {
            ArrayList<Privilege> svl = (ArrayList<Privilege>) u_assigned.getSelectedValuesList();
            if (svl.size() > 0) {
                boolean removed = UserController.removeUserPrivileges(u, svl);
                if (removed) {
                    loadUserPrivilegesAvailableList();
                    loadUserPrivilegesAssignedList();
                }
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //reset user level privileges panel
        resetUserLevelPrivilegesPanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        resetUserPrivilegesPanel();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void cmb_user_levelsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_user_levelsItemStateChanged
        loadUserLevelPrivilegesAvailableList();
        loadUserLevelPrivilegesAssignedList();
    }//GEN-LAST:event_cmb_user_levelsItemStateChanged

    private void cmb_usersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_usersItemStateChanged
        loadUserPrivilegesAvailableList();
        loadUserPrivilegesAssignedList();
    }//GEN-LAST:event_cmb_usersItemStateChanged

    private void range_startKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_range_startKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            range_end.requestFocusInWindow();
        }
    }//GEN-LAST:event_range_startKeyPressed

    private void charges_entry_editorAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_charges_entry_editorAncestorAdded
        range_start.requestFocusInWindow();
    }//GEN-LAST:event_charges_entry_editorAncestorAdded

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Application Backups", "wbk");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            bkpfile = chooser.getSelectedFile().getAbsolutePath();
            if (!bkpfile.endsWith(".wbk")) {
                bkpfile += ".wbk";
            }
            current_reading_old.setText(bkpfile);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int i = JOptionPane.showConfirmDialog(this, "Proceed with the backup process?\n\nPlease do not close the system when the backup process is running.", "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.OK_OPTION) {

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean backupDatabase = DatabaseUtils.backupDatabase(bkpfile);
                        if (backupDatabase) {
                            JOptionPane.showMessageDialog(Preferences.this, "Database backup completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Preferences.this, "Database backup failed to complete.", "Failure", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                    }
                }
            });
            th.start();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Utilities.runShellCommand(Updator.COMMAND_UPDATECHECK);
            }
        });
        th.run();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        Utilities.runShellCommand(Updator.COMMAND_CONFIGURE);
    }//GEN-LAST:event_jButton24ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Preferences.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Preferences dialog = new Preferences(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog blockingDialog;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel cards_base;
    private javax.swing.JPanel charges_entry_editor;
    private javax.swing.JComboBox cmb_user_levels;
    private javax.swing.JComboBox cmb_users;
    private javax.swing.JButton create_acc;
    private javax.swing.JLabel current_reading_old;
    private javax.swing.JButton delete_acc;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JFormattedTextField fixed_price;
    private javax.swing.JPanel general_settings_card;
    private javax.swing.JToggleButton general_toggle;
    private javax.swing.JCheckBox is_unlimited;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private com.l2fprod.common.swing.JButtonBar jButtonBar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.jdesktop.swingx.JXSearchField jXSearchField1;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator1;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator6;
    private javax.swing.JPanel pass_editor;
    private javax.swing.JSpinner range_end;
    private javax.swing.JSpinner range_start;
    private javax.swing.JRadioButton rb_active_user;
    private javax.swing.JRadioButton rb_inactive_user;
    private javax.swing.ButtonGroup settings_list_buttons;
    private javax.swing.JCheckBox showhidepass;
    private javax.swing.JPanel tax_entry_editor;
    private javax.swing.JTextField tf_display_name;
    private javax.swing.JPasswordField tf_new_password;
    private javax.swing.JPasswordField tf_new_password_again;
    private javax.swing.JPasswordField tf_password;
    private javax.swing.JTextField tf_tax_name;
    private javax.swing.JFormattedTextField tf_tax_rate;
    private javax.swing.JTextField tf_username;
    private javax.swing.JList u_assigned;
    private javax.swing.JList u_available;
    private javax.swing.JList ul_assigned;
    private javax.swing.JList ul_available;
    private javax.swing.JComboBox ulevel_combo;
    private javax.swing.JFormattedTextField unit_price;
    private javax.swing.JButton upd_acc_pass;
    private javax.swing.JComboBox userTypesCombo;
    private javax.swing.JPanel user_levels_editor;
    private javax.swing.JPanel users_settings_card;
    private org.jdesktop.swingx.JXTable users_table;
    private javax.swing.JToggleButton users_toggle;
    // End of variables declaration//GEN-END:variables
}
