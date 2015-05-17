package kclient.knuddels.reflection.tools;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import kclient.tools.Logger;

/**
 *
 * @author SeBi
 */

public class KContext implements AppletContext {
    @Override
    public Applet getApplet(String paramString) {
        return null;
    }

    @Override
    public Enumeration<Applet> getApplets() {
        return null;
    }

    @Override
    public AudioClip getAudioClip(URL paramURL) {
        return null;
    }

    @Override
    public Image getImage(URL paramURL) {
        return Toolkit.getDefaultToolkit().getImage(paramURL);
    }

    @Override
    public InputStream getStream(String paramString) {
        return null;
    }

    @Override
    public Iterator<String> getStreamKeys() {
        return null;
    }

    @Override
    public void setStream(String paramString, InputStream paramInputStream) {
    }

    @Override
    public void showDocument(URL paramURL) {
        showDocument(paramURL, null);
    }

    @Override
    public void showDocument(URL paramURL, String paramString) {
        if (Desktop.isDesktopSupported()) {
            Desktop localDesktop = Desktop.getDesktop();

            if (localDesktop.isSupported(Desktop.Action.BROWSE))
                try {
                    localDesktop.browse(paramURL.toURI());
                } catch (URISyntaxException | IOException e) {
                    Logger.get().error(e);
                }
        }
    }

    @Override
    public void showStatus(String paramString) {
    }
}