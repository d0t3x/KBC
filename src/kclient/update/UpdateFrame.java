package kclient.update;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author SeBi
 */
public class UpdateFrame extends JFrame {
    private final JTextArea log;
    private final JLabel currentFile;
    private final JProgressBar currentBar, updateBar;
    
    public UpdateFrame(int revision, int fileCount) {
        super("KClient Revision " + revision);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(screenSize.width / 2 - (super.getWidth() / 2), 
            screenSize.height / 2 - (super.getHeight() / 2));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        this.currentFile = new JLabel("-");
        this.currentFile.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
                
        JPanel currentFilePanel = new JPanel(new GridLayout(2, 2));
        currentFilePanel.add(new JLabel("<html><b>Aktuelle Datei</b></html>"));
        currentFilePanel.add(this.currentFile);
        panel.add(currentFilePanel, BorderLayout.NORTH);
        
        this.log = new JTextArea();
        this.log.setEditable(false);
        this.log.append("Starte Update\r\n"
                + "--------------------------------------------------------------------------------------------------\r\n");
        
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        logPanel.add(new JLabel("<html><b>Log</b></html>"), BorderLayout.NORTH);
        logPanel.add(new JScrollPane(this.log));
        
        panel.add(logPanel, BorderLayout.CENTER);
        
        this.currentBar = new JProgressBar();
        this.currentBar.setMinimum(0);
        
        this.updateBar = new JProgressBar();
        this.updateBar.setMinimum(0);
        this.updateBar.setMaximum(fileCount);
        
        JPanel statePanel = new JPanel(new GridLayout(4, 2));
        statePanel.add(new JLabel("<html><b>Aktuelle Datei</b></html>"));
        statePanel.add(this.currentBar);
        statePanel.add(new JLabel("<html><b>Gesamtes Update</b></html>"));
        statePanel.add(this.updateBar);
        
        panel.add(statePanel, BorderLayout.SOUTH);
        
        super.add(panel);
        super.setResizable(false);
        super.setSize(450, 300);
        super.setVisible(true);
    }
    
    public void setFile(String name) {
        this.currentFile.setText(name);
    }
    public void setPercent(int curr, int max) {
        this.currentBar.setMaximum(max);
        this.currentBar.setValue(curr);
    }
    public void setAllPercent(int c) {
        this.updateBar.setValue(c);
    }
    
    public void addLog(String str) {
        this.log.append(str + "\r\n");
        this.log.setCaretPosition(this.log.getText().length());
    }
    
}
