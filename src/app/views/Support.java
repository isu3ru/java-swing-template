/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.views;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

/**
 *
 * @author Isuru
 */
public class Support extends javax.swing.JDialog {

    String attachedFile = "";

    /**
     * Creates new form Support
     */
    public Support(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        date_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        system_user.setText(System.getProperty("username"));
    }

    public boolean sendSupportEmail() {
        try {
            String senderName = sender_name.getText();
            String senderEmail = sender_email.getText();
            String sendingTime = date_time.getText();
            String systemUser = system_user.getText();

            String message = messge_content.getText();

            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You haven't entered your message. Please enter the message and try again.");
            }

            // Create the email message
            HtmlEmail email = new HtmlEmail();
            email.setHostName("host url");
            email.addTo("sender email", "Sender Name");
            email.setFrom("from email", "From Name");
            email.setAuthentication("username", "password");
            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.addReplyTo("support@company.com", "Support Service - Company");
            email.setSmtpPort(465);

            email.setSubject("New Support Request from Application");

            // embed the image and get the content id
            URL url = getClass().getResource("/app/resources/shield_icon16x16.png");
            String cid = email.embed(url, "Application Logo");

            URL template = getClass().getResource("/app/support/email_template_20161101_isuru.emailtemplate");

            byte[] encoded = Files.readAllBytes(Paths.get(template.toURI()));
            String htmlMessage = new String(encoded, StandardCharsets.UTF_8);

            htmlMessage = htmlMessage.replace("_EP1_", "cid:" + cid);
            htmlMessage = htmlMessage.replace("_EP2_", systemUser);
            htmlMessage = htmlMessage.replace("_EP3_", senderName + "(" + senderEmail + ")");
            htmlMessage = htmlMessage.replace("_EP4_", sendingTime);
            htmlMessage = htmlMessage.replace("_EP5_", message.replaceAll("\\r?\\n", "<br />"));

            // set the html message
            email.setHtmlMsg(htmlMessage);

            String textMessage = "Application - Support Request\n"
                    + "---------------------------------------------------------------------\n"
                    + "New Support Request from P1\n"
                    + "Sent by P2 on P3\n"
                    + "---------------------------------------------------------------------\n"
                    + "\n"
                    + "Message: \nP4\n"
                    + "\n"
                    + "---------------------------------------------------------------------\n"
                    + "This is an automatically generated email sent from Application.\n"
                    + "\n"
                    + "© All Rights Reserved - Management Development Co-operative Co. Ltd.";

            textMessage = textMessage.replace("P1", systemUser);
            textMessage = textMessage.replace("P2", senderName + "(" + senderEmail + ")");
            textMessage = textMessage.replace("P3", sendingTime);
            textMessage = textMessage.replace("P4", message);

            // set the alternative message
            email.setTextMsg(textMessage);

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot send email.\nError:" + e.getLocalizedMessage(), "Sending failure", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean sendSupportRequest() {
        try {
            String senderName = sender_name.getText();
            String senderEmail = sender_email.getText();
            String sendingTime = date_time.getText();
            String systemUser = system_user.getText();

            String message = messge_content.getText();

            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You haven't entered your message. Please enter the message and try again.");
            }

            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName("mail.mdcc.lk");
            email.addTo("viraj@mdcc.lk", "Viraj @ MDCC");
            email.addTo("isuru@mdcc.lk", "Isuru @ MDCC");
            email.setFrom(senderEmail, senderName);
            email.setAuthentication("support@mdcc.lk", "mdccmail123");
            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.setSmtpPort(465);

//            email.setHostName("mail.mdcc.lk");
//            email.addTo("isu3ru@gmail.com", "Isuru Ranawaka");
//            email.setFrom("isuru@mdcc.lk", "Isuru Ranawaka from MDCC");
//            email.setSubject("Test email with inline image");
//            email.setAuthentication("isuru@mdcc.lk", "=-88isuru");
//            email.setSSLOnConnect(false);
//            email.setSmtpPort(25);
//            email.setDebug(true);
            email.setSubject("New Support Request from Application");

            String textMessage = "Application - Support Request\n"
                    + "---------------------------------------------------------------------\n"
                    + "New Support Request from _P1_\n"
                    + "Sent by _P2_ on _P3_\n"
                    + "---------------------------------------------------------------------\n"
                    + "\n"
                    + "Message: \n_P4_\n"
                    + "\n"
                    + "---------------------------------------------------------------------\n"
                    + "This email was sent from Application. \n"
                    + "Please note that this is an automatically generated email and \n"
                    + "your replies will go nowhere.\n"
                    + "\n"
                    + "© All Rights Reserved - Management Development Co-operative Co. Ltd.";

            textMessage = textMessage.replace("_P1_", systemUser);
            textMessage = textMessage.replace("_P2_", senderName + "(" + senderEmail + ")");
            textMessage = textMessage.replace("_P3_", sendingTime);
            textMessage = textMessage.replace("_P4_", message);

            //attach file
            if (!attachedFile.trim().isEmpty()) {
                email.attach(new File(attachedFile));
            }
            
            // set the alternative message
            email.setMsg(textMessage);

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot send email.\nError:" + e.getLocalizedMessage(), "Sending failure", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        date_time = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        system_user = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messge_content = new javax.swing.JEditorPane();
        jButton2 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        sender_name = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        sender_email = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        filepath = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Request Support");

        jLabel3.setBackground(new java.awt.Color(36, 114, 191));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/resources/shield_icon16x16.png"))); // NOI18N
        jLabel3.setText("Request Support for Application");
        jLabel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(242, 130, 27)), javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 10)));
        jLabel3.setIconTextGap(11);
        jLabel3.setOpaque(true);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("<html>Your support request will be sent by E-Mail to the development team and this feature <strong>requires an active Internet connection</strong>.");
        jLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Date & Time : ");

        date_time.setText("{{DATE_TIME}}");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("System User : ");

        system_user.setText("{{CURRENT_USER}}");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Message : ");

        jScrollPane1.setViewportView(messge_content);

        jButton2.setText("Send Request");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Your Details : (Optional)");

        jLabel9.setText("Name : ");

        sender_name.setEditable(false);
        sender_name.setText("Municipal Council - Nuwaraeliya");

        jLabel10.setText("E-Mail : ");

        sender_email.setEditable(false);
        sender_email.setText("nuwaraeliyamc@sltnet.lk");

        jLabel4.setText("Attach File : ");

        jButton3.setText("Browse");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sender_name)
                    .addComponent(sender_email))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date_time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(system_user)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filepath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(date_time)
                    .addComponent(jLabel5)
                    .addComponent(system_user))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(sender_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(sender_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(filepath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                jProgressBar1.setIndeterminate(true);
                messge_content.setEditable(false);
                jButton2.setEnabled(false);
                boolean sent = sendSupportRequest();
                jButton2.setEnabled(true);
                messge_content.setEditable(true);
                jProgressBar1.setIndeterminate(false);
                if (sent) {
                    JOptionPane.showMessageDialog(Support.this, "Successfully submitted your request.\nThank you for your co-operation.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Support.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(Support.this, "Failed to submit your request. Please try again.\nThank you for your co-operation.", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        th.start();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "All Files", "jpg", "gif");
//        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            attachedFile = chooser.getSelectedFile().getAbsolutePath();
            filepath.setText(attachedFile);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date_time;
    private javax.swing.JTextField filepath;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane messge_content;
    private javax.swing.JTextField sender_email;
    private javax.swing.JTextField sender_name;
    private javax.swing.JLabel system_user;
    // End of variables declaration//GEN-END:variables
}
