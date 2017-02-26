package app;

import app.config.Config;
import app.helpers.Utilities;
import app.views.Login;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import javax.swing.UIManager;

public class Main {

    private static File getFontFile(String fileName) throws Exception {
        URL url = Main.class.getResource("/app/fonts/" + fileName);
        return new File(url.toURI());
    }

    public Font getFont(String fileName) throws Exception {
        String path = "/xyz/isururanawaka/wb/fonts/" + fileName;
        URL url = getClass().getResource(path);
        return Font.createFont(Font.TRUETYPE_FONT, new File(url.toURI()));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Utilities.attachAutomaticTextSelection();
            if (!Config.isValidConfigurationFound()) {
                Config.createConfig();
                System.exit(0);
            }
            new Login().setVisible(true);
        } catch (Exception e) {
        }
    }

}
