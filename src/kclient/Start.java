package kclient;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import kclient.tools.Logger;
import kclient.update.Updater;

/**
 *
 * @author SeBi
 */
public class Start {
    public static final int REVISION = 0;
    public static final String VERSION = "1.0";
    
    public static void main(String[] args) {
        Logger.get().info("Starting KClient " + Start.VERSION + "." + Start.REVISION);
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("nimbusOrange", new ColorUIResource(Color.green.darker()));
                    break;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
            }
        }
        
        Updater updater = new Updater();
        if (updater.start()) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
            
            Logger.get().info("  Initializing Gui");
            
        }
        Logger.get().info("Client exited");
    }    
}
