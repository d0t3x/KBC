package kclient.update;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import kclient.Start;
import kclient.tools.Logger;
import kclient.tools.Parameter;

/**
 *
 * @author SeBi
 */
public class Updater {
    private final Parameter params;
    
    public Updater() {
        Logger.get().info("  Prüfe auf Updates");
        this.params = new Parameter("http://knds.sebitm.info/kclient/update.properties");
    }
    
    public boolean start() {
        int rev = this.params.getInt("revision");
        if (rev > Start.REVISION) {
            Logger.get().info("    Update Revision " + rev + " vorhanden");
            StringBuilder msgBuffer = new StringBuilder();
            Map<File, URL> repFiles = new HashMap<>();
            String[] tmpFiles = this.params.get("files").split(";;");
            
            for (String tmpFileArr : tmpFiles) {
                String[] fileData = tmpFileArr.split(";");
                try {
                    File repPath = new File(fileData[0]);
                    URL downloadURL = new URL(fileData[1]);
                    
                    String[] tmp = fileData[0].split("/");
                    String path = repPath.toString().substring(0, repPath.toString().length() - tmp[tmp.length - 1].length());
  
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                
                    repFiles.put(repPath, downloadURL);
                    msgBuffer.append("  - ").append(fileData[0]).append("\r\n");
                } catch (IOException e) {
                    Logger.get().error(e);
                }
            }
            
            Object[] buttons = new Object[] { "Ja", "Nein", "Changelog" };
            int result = JOptionPane.showOptionDialog(null, 
                    "Eine neue Version des KClient steht zur Verfügung (Revision: " + rev + ")\n\n" + 
                    "Möchtest du das Update jetzt durchführen ?\n\n" + 
                    "Folgende Dateien werden dabei ersetzt:\n" + msgBuffer.toString(), 
                    "KClient Update " + rev, 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
           
            if (result == 2) {
                JFrame frame = new JFrame("KClient Changelog | Revision: " + rev);
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                JTextPane area = new JTextPane();
                area.setContentType("text/html");
                area.setText("<html><style>body { font: 10px 'Arial'; }</style><h3>Changelog</h3>" + this.params.get("changelog") + "</html>");
                area.setEditable(false);
                frame.add(new JScrollPane(area));
                frame.setSize(400, 350);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(screenSize.width / 2 - (frame.getWidth() / 2), 
                        screenSize.height / 2 - (frame.getHeight() / 2));
                frame.setVisible(true);
                while (frame.isVisible()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
                frame.dispose();
                return this.start();
            } else if (result == 0) {
                UpdateFrame frame = new UpdateFrame(rev, repFiles.size());
                int count = 1;
                for (Map.Entry<File, URL> updateFile : repFiles.entrySet()) {
                    int fileSize = this.getFileSize(updateFile.getValue());
                    frame.setPercent(0, fileSize);
                    frame.setAllPercent(count);
                    frame.setFile(updateFile.getKey().toString());
                    
                    frame.addLog("Starte Download (" + updateFile.getKey().getName() + ") " + fileSize + "K");
                    downloadFile(updateFile.getKey(), updateFile.getValue(), fileSize, frame);
                    frame.addLog("Download beendet");
                    frame.addLog("--------------------------------------------------------------------------------------------------");
                    count++;
                }
                frame.addLog("Update beendet");
                
                frame.addLog("Bitte starte den Client nun neu.");
                return false;
            } else {
                Logger.get().info("    Update übersprungen");
            }
        } else {
            Logger.get().info("    Der Client ist auf dem aktuellsten Stand");
        }
        return true;
    }
    
    private boolean downloadFile(File save, URL url, int size, UpdateFrame frame) {
        try {
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                in = new BufferedInputStream(url.openStream());
                fout = new FileOutputStream(save);

                int fileSize = 0;
                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                    fileSize += count;
                    frame.setPercent(fileSize, size);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            }
            return save.exists();
        } catch (IOException e) {
            
        }
        return false;
    }
    
    private int getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conn.disconnect();
        }
    }
}
