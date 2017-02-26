/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Isuru
 */
public class Utilities {

    public enum Month {

        JANUARY(0, "January"), FEBRUARY(1, "February"), MARCH(2, "March"), APRIL(3, "April"),
        MAY(4, "May"), JUNE(5, "June"), JULY(6, "July"), AUGUST(7, "August"),
        SEPTEMBER(8, "September"), OCTOBER(9, "October"), NOVEMBER(10, "November"), DECEMBER(11, "December");

        private int monthNumber;
        private String monthName;

        private Month(int monthNumber, String monthName) {
            this.monthNumber = monthNumber;
            this.monthName = monthName;
        }

        public int getMonthNumber() {
            return monthNumber;
        }

        public void setMonthNumber(int monthNumber) {
            this.monthNumber = monthNumber;
        }

        public String getMonthName() {
            return monthName;
        }

        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }

        @Override
        public String toString() {
            return this.monthName;
        }

    }

    public static ArrayList<String> explode(String string, String delimiter) {
        ArrayList<String> al = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(string, delimiter);
        while (st.hasMoreElements()) {
            String s = st.nextToken();
            al.add(s);
        }
        return al;
    }

    public static String implode(String glue, ArrayList<String> pieces) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (String s : pieces) {
            sb.append(s);
            i++;
            if (i < pieces.size()) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static String implode(String glue, Object[] peices) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (Object ob : peices) {
            sb.append(ob.toString());
            i++;
            if (i < peices.length) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static String implode(String glue, int[] peices) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (int in : peices) {
            sb.append(in);
            i++;
            if (i < peices.length) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static void attachAutomaticTextSelection() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent e) {
                if (e.getNewValue() instanceof JTextComponent) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JTextComponent compo = (JTextComponent) e.getNewValue();
                            if (compo.isEditable()) {
                                compo.selectAll();
                            }
                        }
                    });
                }
            }
        });
    }

    public static Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        Date retDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            retDate = sdf.parse(dateString);
        } catch (Exception ex) {
            return null;
        }
        return retDate;
    }

    public static Date parseDate(String formatString, String dateString) {
        if (dateString == null) {
            return null;
        }
        Date retDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        try {
            retDate = sdf.parse(dateString);
        } catch (Exception ex) {
            return null;
        }
        return retDate;
    }

    public static String formatDateForSQL(Date date) {
        if (date != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } else {
            return "";
        }
    }

    public static String formatDateForSQL(String dateFormat, Date date) {
        if (date != null) {
            return new SimpleDateFormat(dateFormat).format(date);
        } else {
            return "";
        }
    }

    public static synchronized void clearJTable(JTable table) {
        try {
            DefaultTableModel def = (DefaultTableModel) table.getModel();
            def.setRowCount(0);
        } catch (Exception e) {
        }
    }

    public static String getMonthName(int monthNo) {
        String retStr;
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        switch (monthNo) {
            case 0:
                retStr = months[0];
                break;
            case 1:
                retStr = months[1];
                break;
            case 2:
                retStr = months[2];
                break;
            case 3:
                retStr = months[3];
                break;
            case 4:
                retStr = months[4];
                break;
            case 5:
                retStr = months[5];
                break;
            case 6:
                retStr = months[6];
                break;
            case 7:
                retStr = months[7];
                break;
            case 8:
                retStr = months[8];
                break;
            case 9:
                retStr = months[9];
                break;
            case 10:
                retStr = months[10];
                break;
            case 11:
                retStr = months[11];
                break;
            default:
                retStr = months[0];
                break;
        }
        return retStr;
    }

    public static int getMonthNo(String month) {
        int mn = 0;
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            String mstr = months[i];
            if (mstr.equals(month)) {
                mn = i;
                break;
            } else {
                continue;
            }
        }
        return ++mn;
    }

    public static void centerWindow(Window centerThis, Window onThis) {
        try {
            Dimension ctd = centerThis.getSize();
            int wid_ct = ctd.width;
            int hei_ct = ctd.height;

            Dimension otd = onThis.getSize();
            int wid_ot = otd.width;
            int hei_ot = otd.height;

            Point ot_p = onThis.getLocation();
            int x_ot = ot_p.x;
            int y_ot = ot_p.y;

            int x = (wid_ot - wid_ct) / 2;
            int y = (hei_ot - hei_ct) / 2;
            centerThis.setLocation(x_ot + x, y_ot + y);
        } catch (Exception e) {
        }
    }

    public static void centerComponent(Component centerThis, Window onThis) {
        try {
            Dimension ctd = centerThis.getSize();
            int wid_ct = ctd.width;
            int hei_ct = ctd.height;

            Dimension otd = onThis.getSize();
            int wid_ot = otd.width;
            int hei_ot = otd.height;

            Point ot_p = onThis.getLocation();
            int x_ot = ot_p.x;
            int y_ot = ot_p.y;

            int x = (wid_ot - wid_ct) / 2;
            int y = (hei_ot - hei_ct) / 2;
            centerThis.setLocation(x_ot + x, y_ot + y);
            centerThis.getLocation().translate(x, y);
        } catch (Exception e) {
        }
    }

    public static void attachJTableFilter(final JTable table, final JTextField textfield) {
        textfield.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>();
                TableModel model = table.getModel();
                sorter = new TableRowSorter(model);
                table.setRowSorter(sorter);
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textfield.getText()));
                } catch (Exception exp) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"));
                }
            }

            public void removeUpdate(DocumentEvent e) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>();
                TableModel model = table.getModel();
                sorter = new TableRowSorter(model);
                table.setRowSorter(sorter);
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textfield.getText()));
                } catch (Exception exp) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"));
                }
            }

            public void changedUpdate(DocumentEvent e) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>();
                TableModel model = table.getModel();
                sorter = new TableRowSorter(model);
                table.setRowSorter(sorter);
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textfield.getText()));
                } catch (Exception exp) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"));
                }
            }
        });

    }

    public static String getHashMapToString(HashMap hm) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator it = hm.values().iterator();
            while (it.hasNext()) {
                Object object = it.next();
                sb.append(object);
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
        } catch (Exception e) {
        }
        return sb.toString();

    }

    public static int findIndexedLocation(Double d, Double[] array) {
        int loc = -1;
        try {
            int ars = array.length;
            for (int i = 0; i < ars; i++) {
                Double dob = array[i];
                if (String.valueOf(dob).equals(String.valueOf(d))) {
                    loc = i;
                    break;
                }
            }
        } catch (Exception e) {
            loc = -1;
        }
        return loc;
    }

    public static boolean registerFont(File fontFile) {
        boolean b = false;
        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
            b = true;
        } catch (FontFormatException | IOException e) {
            b = false;
        }
        return b;
    }

    public static void turnAntialiasingOn() {
//        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static String _convertMD5ToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String MD5_HEX(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return _convertMD5ToHex(md5hash);
    }

    public static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        return new String(md.digest());
    }

    @Deprecated
    public static String getMacAddress() throws IOException {
        String macaddr = "";
        InetAddress addr = InetAddress.getLocalHost();

        NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
        byte[] maca = ni.getHardwareAddress();

        for (int k = 0; k < maca.length; k++) {
            macaddr += String.format("%02X%s", maca[k], (k < maca.length - 1) ? ":" : "");
        }
        return macaddr;
    }

    public static void attachTableSelectionChangedListener(JTable jt, final Callback action) {
        jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                action.run(e);
            }
        });
    }

    public static double getDoubleValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0D;
        }
    }

    public static double getDoubleValue(Object value) {
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0.0D;
        }
    }

    public static float getFloatValue(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return 0.0F;
        }
    }

    public static float getFloatValue(Object value) {
        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            return 0.0F;
        }
    }

    public static int getIntegerValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIntegerValue(Object value) {
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getRowForValue(JTable jt, int col, Object value) {
        int rc = jt.getRowCount();
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                if (jt.getValueAt(i, col).equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void attachFocusMovementOnEnter(JComponent current, JComponent target) {
        attachFocusMoveOnKey(current, target, KeyEvent.VK_ENTER);
    }

    public static void attachFocusMoveOnKey(JComponent current, final JComponent target, final int keyCode) {
        current.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == keyCode) {
                    target.requestFocusInWindow();
                }
            }

        });
    }

    public static String getGeneratedBillIDForCustomer(String customer_id, int year, Month month) {
        String b_str = "";
        String yrstr = Integer.toString(year);
        if (yrstr.length() == 4) {
            b_str += yrstr.substring(2, yrstr.length());
        }
        if (yrstr.length() == 2) {
            b_str += yrstr;
        }
        b_str += new DecimalFormat("00").format(month.getMonthNumber() + 1);
        b_str += customer_id;
        return b_str;
    }

    public static HashMap<String, String> getGeneratedBillStructureForCustomer(String customer_id, int year, Month month) {
        HashMap<String, String> b_str = new HashMap<>();
        String yrstr = Integer.toString(year);
        if (yrstr.length() == 4) {
            b_str.put("year", yrstr.substring(2, yrstr.length()));
        }
        if (yrstr.length() == 2) {
            b_str.put("year", yrstr);
        }

        b_str.put("month", new DecimalFormat("00").format(month.getMonthNumber() + 1));
        b_str.put("customer_id", customer_id);
        return b_str;
    }

    public static void setWindowIcon(Window w) {
        w.setIconImage(new ImageIcon(w.getClass().getResource("/app/resources/shield_icon16x16.png")).getImage());
    }

    // pad with " " to the right to the given length (n)
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    // pad with " " to the left to the given length (n)
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public static void unzipFile(String filePath, String destination) throws Exception {
        FileInputStream fis;
        ZipInputStream zipIs;
        ZipEntry zEntry;
        try {
            fis = new FileInputStream(filePath);
            zipIs = new ZipInputStream(new BufferedInputStream(fis));
            while ((zEntry = zipIs.getNextEntry()) != null) {
                try {
                    byte[] tmp = new byte[4 * 1024];
                    FileOutputStream fos = null;
                    if (!destination.endsWith("/")) {
                        destination += "/";
                    }

                    String opFilePath = destination + zEntry.getName();
                    fos = new FileOutputStream(opFilePath);
                    int size;
                    while ((size = zipIs.read(tmp)) != -1) {
                        fos.write(tmp, 0, size);
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception ex) {
                }
            }
            zipIs.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void addKeyBindingAction(JFrame targetFrame, int keyCode, String inputName, String actionName, AbstractAction action) {
        InputMap inputMap = targetFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), inputName);
        ActionMap actionMap = targetFrame.getRootPane().getActionMap();
        actionMap.put(actionName, action);
    }

    public static void addKeyBindingAction(JDialog targetDialog, int keyCode, String inputName, String actionName, AbstractAction action) {
        InputMap inputMap = targetDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), inputName);
        ActionMap actionMap = targetDialog.getRootPane().getActionMap();
        actionMap.put(actionName, action);
    }

    public static void runShellCommand(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
        }
    }

    public static double getRoundedValue(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String truncateDoubleValue(double value, int places) {
        return new BigDecimal(Double.toString(value))
                .setScale(places, RoundingMode.DOWN)
                .stripTrailingZeros()
                .toString();
    }

    /**
     * Opens and reads a file, and returns the contents as one String.
     */
    public static String readFileAsString(String filename)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Opens and reads a file, and returns the contents as one String.
     */
    public static String readFileAsString(File file)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Open and read a file, and return the lines in the file as a list of
     * Strings.
     */
    public static List<String> readFileAsListOfStrings(String filename) throws Exception {
        List<String> records = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            records.add(line);
        }
        reader.close();
        return records;
    }

    /**
     * Reads a "properties" file, and returns it as a Map (a collection of
     * key/value pairs).
     *
     * @param filename
     * @param delimiter
     * @return
     * @throws Exception
     */
    public static Map<String, String> readPropertiesFileAsMap(String filename, String delimiter)
            throws Exception {
        Map<String, String> map = new HashMap();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            // assumption here is that proper lines are like "String : http://xxx.yyy.zzz/foo/bar",
            // and the ":" is the delimiter
            int delimPosition = line.indexOf(delimiter);
            String key = line.substring(0, delimPosition - 1).trim();
            String value = line.substring(delimPosition + 1).trim();
            map.put(key, value);
        }
        reader.close();
        return map;
    }

    /**
     * Read a Java properties file and return it as a Properties object.
     */
    public static java.util.Properties readPropertiesFile(String canonicalFilename)
            throws IOException {
        java.util.Properties properties = new java.util.Properties();
        properties.load(new FileInputStream(canonicalFilename));
        return properties;
    }

    /**
     * Save the given text to the given filename.
     *
     * @param canonicalFilename Like /Users/al/foo/bar.txt
     * @param text All the text you want to save to the file as one String.
     * @throws IOException
     */
    public static void writeFile(String canonicalFilename, String text) throws IOException {
        File file = new File(canonicalFilename);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(text);
        out.close();
    }

    /**
     * Write an array of bytes to a file. Presumably this is binary data; for
     * plain text use the writeFile method.
     */
    public static void writeFileAsBytes(String fullPath, byte[] bytes) throws IOException {
        OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fullPath));
        InputStream inputStream = new ByteArrayInputStream(bytes);
        int token = -1;

        while ((token = inputStream.read()) != -1) {
            bufferedOutputStream.write(token);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        inputStream.close();
    }

    public static void copyFile(File source, File destination) throws IOException {
        //if (!source.isFile() || !dest.isFile()) return false;

        byte[] buffer = new byte[100000];

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int size;
            while ((size = bufferedInputStream.read(buffer)) > -1) {
                bufferedOutputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            // TODO may want to do something more here
            throw e;
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            } catch (IOException ioe) {
                // TODO may want to do something more here
                throw ioe;
            }
        }
    }

}
