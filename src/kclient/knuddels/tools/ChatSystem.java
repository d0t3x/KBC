package kclient.knuddels.tools;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import kclient.knuddels.reflection.tools.ManipulationData;
import kclient.tools.Logger;
import kclient.tools.Parameter;

/**
 *
 * @author SeBi
 */
public enum ChatSystem {
    DE("de", "Knuddels.de"),
    AT("at", "Knuddels.at"),
    COM("com", "Knuddels.com"),
    DEV("dev", "Knuddels App Developer"),
    TEST("test", "Knuddels Testserver");
    
    private final String id;
    private final String name;
    private final Parameter params;
    private final ManipulationData manipulationData;
    private final String currentVersion;
    private final String cacheVersion;
    
    private ChatSystem(String id, String name) {
        this.id = id;
        this.name = name;
        this.params = new Parameter("chatsystem" + File.separator + this.id);
        this.manipulationData = new ManipulationData(this.id);
        this.cacheVersion = this.params.get("version");
        this.currentVersion = this.getVersion(this.params.get("version_url"));
    }
    
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getCurrentVersion() {
        return this.currentVersion;
    }
    public String getCacheVersion() {
        return this.cacheVersion;
    }
    public ManipulationData getManipulation() {
        return this.manipulationData;
    }
    
    private String getVersion(String siteUrl) {
        try {
            URL url = new URL(siteUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            byte[] buffer = new byte[con.getContentLength()];
            int pos = 0;
            while (pos < buffer.length)
                    pos += con.getInputStream().read(buffer, pos, buffer.length - pos);

            String content = new String(buffer);
            int ind = content.indexOf("appVersion");
            if (ind > 0) {
                content = content.substring(ind + 24);
            } else {
                ind = content.indexOf("<jar href=\"knuddels");
                if (ind > 0) {
                    content = content.substring(ind + 19);
                } else {
                    ind = content.indexOf("?v=");
                    if (ind < 0) {
                        ind = content.indexOf("vn='");
                        if (ind > 0)
                            content = content.substring(ind + 4);
                        else {
                            ind = content.indexOf("archive=\"knuddels");
                            content = content.substring(ind + 17);
                        }
                    }
                }
            }
            String v = content.substring(0, 6);
            return (v.startsWith("k") || v.startsWith("m")) ? v : getCacheVersion();
        } catch (Exception e) {
            Logger.get().error(e);
        }
        return null;
    }
    
    public static ChatSystem fromName(String name) {
        for (ChatSystem chatSystem : ChatSystem.values())
            if (chatSystem.getName().equals(name))
                return chatSystem;
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("[ChatSystem - %s, CacheVersion: %s, CurrentVersion: %s]",
                    this.getName(),
                    this.getCacheVersion(),
                    this.getCurrentVersion()
                );
    }
}
