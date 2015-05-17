package kclient;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import kclient.knuddels.reflection.KLoader;
import kclient.knuddels.tools.AppletCache;
import kclient.knuddels.tools.ChatSystem;
import kclient.tools.Logger;
import kclient.ui.ClientFrame;
import kclient.update.Updater;

/**
 *
 * @author SeBi
 */
public class Start {
    public static final int REVISION = 1;
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
            /*try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }*/
            Logger.get().info("Starting Client...");
            Logger.get().info("  Loading ChatSystems");
            for (ChatSystem cs : ChatSystem.values())
                Logger.get().info("    " + cs.toString());
            
            for (ChatSystem cs : ChatSystem.values()) {
                AppletCache cache = AppletCache.get().getCache(cs);
                KLoader.getLoader(cs, cache);
            }
            
            Logger.get().info("  Initializing Gui");
            
            ClientFrame.get().setVisible(true);
        }
    }    
}
