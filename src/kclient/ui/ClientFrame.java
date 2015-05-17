package kclient.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import kclient.Start;
import kclient.knuddels.GroupChat;
import kclient.knuddels.reflection.KLoader;
import kclient.knuddels.tools.ChatSystem;
import kclient.tools.Logger;
import kclient.tools.Util;

/**
 *
 * @author SeBi
 */
public class ClientFrame extends JFrame implements ActionListener {
    private static final ClientFrame instance;
    
    static {
        instance = new ClientFrame();
    }
    public static ClientFrame get() {
        synchronized (instance) {
            return instance;
        }
    }
    
    private final JTabbedPane tabbedPane;
    private final JTextField proxyField, tabIdField;
    private final JProgressBar progressBar;
    private final JLabel progressLabel, cacheVersionLbl, currentVersionLbl, progressInfoLabel;
    
    private final List<GroupChat> groupChats;
    
    private TrayIcon icon;
    private MenuItem trayClient, trayChannels, close, trayBingoFrames,
            trayMauMauFrames, trayPokerFrames;
    
    public ClientFrame() {
        super("KClient " + Start.VERSION + " - Rev. " + Start.REVISION);
        
        super.setLayout(new BorderLayout());
        super.setSize(550, 320);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setOpaque(false);
        super.add(this.tabbedPane, BorderLayout.CENTER);
        
        this.proxyField = new JTextField();
        this.progressBar = new JProgressBar();
        this.progressLabel = new JLabel("0 %");
        this.progressInfoLabel = new JLabel("-");
        this.progressLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        this.cacheVersionLbl = new JLabel("-");
        this.currentVersionLbl = new JLabel("-");
        this.tabIdField = new JTextField();
        
        this.setAppletVersion(ChatSystem.DE);
        
        this.groupChats = new ArrayList<>();
        
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (GroupChat groupChat : ClientFrame.this.groupChats)
                    groupChat.stop();
                SystemTray.getSystemTray().remove(icon);
                System.exit(0);
            }
        });
        
        ArrayList<Image> icons = new ArrayList<>();
        Image favicon = null;

        try {
            favicon = ImageIO.read(getClass().getResource("/res/icon/frame_icon_knuddel2.png"));
            icons.add(favicon);
        } catch (IOException e) {
            Logger.get().error(e);
        }

        super.setIconImages(icons);

        if (SystemTray.isSupported()) {
            this.icon = new TrayIcon(favicon);
            PopupMenu menu = new PopupMenu();

            this.trayClient = new MenuItem("Hide Client");
            this.trayClient.addActionListener(this);
            menu.add(this.trayClient);

            this.trayBingoFrames = new MenuItem("Show Bingo Frames");
            this.trayBingoFrames.addActionListener(this);
            menu.add(this.trayBingoFrames);
            
            this.trayChannels = new MenuItem("Hide Channels");
            this.trayChannels.addActionListener(this);
            menu.add(this.trayChannels);

            this.trayMauMauFrames = new MenuItem("Hide MauMau Frames");
            this.trayMauMauFrames.addActionListener(this);
            menu.add(this.trayMauMauFrames);
            
            this.trayPokerFrames = new MenuItem("Hide Poker Frames");
            this.trayPokerFrames.addActionListener(this);
            menu.add(this.trayPokerFrames);
            
            this.close = new MenuItem("Close");
            this.close.addActionListener(this);
            menu.add(this.close);
            
            icon.setPopupMenu(menu);

            try {
                SystemTray.getSystemTray().add(icon);
            } catch (AWTException e) {
                Logger.get().error(e);
            }
        }
        
        this.tabbedPane.addTab("KClient", this.createKClientTab());
    }
    
    private JPanel createKClientTab() {
        JPanel root = new JPanel(new BorderLayout());
        
        JPanel panel = new JPanel(new GridLayout(7, 3, 0, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 25));
        
        // ChatSystem
        panel.add(new JLabel("<html><b>ChatSystem:</b></html>"));
        
        String[] strSystems = new String[ChatSystem.values().length];
        for (int i = 0; i < strSystems.length; i++)
            strSystems[i] = ChatSystem.values()[i].getName();
        final JComboBox chatSystemBox = new JComboBox(strSystems);
        chatSystemBox.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                String systemName = chatSystemBox.getSelectedItem().toString();
                ChatSystem chatSystem = ChatSystem.fromName(systemName);
                ClientFrame.get().setAppletVersion(chatSystem);
                ClientFrame.this.tabIdField.setText(chatSystem.getName());
            }
        });
        panel.add(chatSystemBox);
        panel.add(new JLabel());
        // Proxy
        panel.add(new JLabel("<html><b>Proxy:     </b></html>"));
        panel.add(this.proxyField);
        panel.add(new JLabel());
        // Tab Id
        panel.add(new JLabel("<html><b>Tab Id:</b></html>"));
        this.tabIdField.setText(ChatSystem.DE.getName());
        panel.add(this.tabIdField);
        panel.add(new JLabel());
        // Hinzufügen Button
        JButton btnAdd = new JButton("Hinzufügen");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ChatSystem system = ChatSystem.fromName(chatSystemBox.getSelectedItem().toString());
                ClientFrame.this.progressBar.setValue(0);
                ClientFrame.this.progressLabel.setText("0 %");
                KLoader.getLoader(system, null).setProgress(ClientFrame.this.progressBar, 
                        ClientFrame.this.progressLabel);
                new Thread("GroupChatLoad") {
                    @Override
                    public void run() {
                        Logger.get().info(" Initializing Manipulation for " + system.getName());
                        if (!KLoader.getLoader(system, null).isReady())
                            KLoader.getLoader(system, null).prepare();
                        GroupChat groupChat = new GroupChat(system);
                        groupChats.add(groupChat);
                        
                        Util.setNimbus();
                        final JPanel gcPanel = new JPanel(new BorderLayout());
                        gcPanel.setOpaque(false);
                        
                        JPanel topPanel = new JPanel(new BorderLayout());
                        topPanel.setOpaque(false);
                        //topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
                        
                        JLabel infoLbl = new JLabel(" ");
                        infoLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 495));
                        topPanel.add(infoLbl, BorderLayout.WEST);
                        
                        JButton closeBtn = new JButton("x");
                        closeBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                groupChat.stop();
                                tabbedPane.remove(gcPanel);
                                Util.setNimbus();
                            }
                        });
                        topPanel.add(closeBtn);
                        
                        gcPanel.add(topPanel, BorderLayout.NORTH);
                        gcPanel.add(groupChat.getComponent(), BorderLayout.CENTER);
                        
                        tabbedPane.addTab(ClientFrame.this.tabIdField.getText(), gcPanel);
                        Util.setNimbus();
                    }
                }.start();
            }
        });
        panel.add(new JLabel());
        panel.add(btnAdd);
        panel.add(new JLabel());
        // Manipulation Info
        panel.add(new JLabel("<html><b>Manipulation:</b></html>"));
        panel.add(this.progressBar);
        panel.add(this.progressLabel);
        panel.add(new JLabel());
        panel.add(this.progressInfoLabel);
        panel.add(new JLabel());
        root.add(panel, BorderLayout.NORTH);
        //-------------------------------------------
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        
        // Version Info
        JPanel info = new JPanel(new GridLayout(2, 2));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 100));
        info.add(new JLabel("<html><b>Cache Version:</b></html>"));
        info.add(this.cacheVersionLbl);
        info.add(new JLabel("<html><b>Aktuelle Version:</b></html>"));
        info.add(this.currentVersionLbl);
        bottom.add(info, BorderLayout.CENTER);
        
        root.add(bottom, BorderLayout.SOUTH);
        
        return root;
    }
    
    public void setAppletVersion(ChatSystem chatSystem) {
        this.cacheVersionLbl.setText(chatSystem.getCacheVersion());
        this.currentVersionLbl.setText(chatSystem.getCurrentVersion());
    }
    public void setProgressLog(String str) {
        this.progressInfoLabel.setText(str);
    }
    
    public TrayIcon getTray() {
        return this.icon;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.trayClient) {
            boolean show = this.trayClient.getLabel().equals("Show Client");
            this.trayClient.setLabel(show ? "Hide Client" : "Show Client");
            super.setVisible(show);
        } else if (e.getSource() == this.trayChannels) {
            boolean show = this.trayChannels.getLabel().equals("Show Channels");
            this.trayChannels.setLabel(show ? "Hide Channels" : "Show Channels");
            for (GroupChat groupChat : this.groupChats) {
                Enumeration channels = groupChat.getChannelFrames();

                while (channels.hasMoreElements()) {
                    ((Frame) channels.nextElement()).setVisible(show);
                }
            }
        } else if (e.getSource() == this.trayBingoFrames) {
            boolean show = this.trayBingoFrames.getLabel().equals("Show Bingo Frames");
            this.trayBingoFrames.setLabel(show ? "Hide Bingo Frames" : "Show Bingo Frames");
            for (GroupChat groupChat : this.groupChats) {
                groupChat.setShowBingoFrames(show);
                Enumeration frames = groupChat.getBingoFrames();
                while (frames.hasMoreElements()) {
                    ((Frame) frames.nextElement()).setVisible(show);
                }
            }
        } else if (e.getSource() == this.trayMauMauFrames) {
            boolean show = this.trayMauMauFrames.getLabel().equals("Show MauMau Frames");
            this.trayMauMauFrames.setLabel(show ? "Hide MauMau Frames" : "Show MauMau Frames");
            for (GroupChat groupChat : this.groupChats) {
                groupChat.setShowMauMauFrames(show);
                Enumeration frames = groupChat.getMauMauFrames();
                while (frames.hasMoreElements()) {
                    ((Frame) frames.nextElement()).setVisible(show);
                }
            }
        } else if (e.getSource() == this.trayPokerFrames) {
            boolean show = this.trayPokerFrames.getLabel().equals("Show Poker Frames");
            this.trayPokerFrames.setLabel(show ? "Hide Poker Frames" : "Show Poker Frames");
            for (GroupChat groupChat : this.groupChats) {
                groupChat.setShowPokerFrames(show);
                Enumeration frames = groupChat.getPokerFrames();
                while (frames.hasMoreElements()) {
                    ((Frame) frames.nextElement()).setVisible(show);
                }
            }
        } else if (e.getSource() == this.close) {
            for (GroupChat gc : this.groupChats)
                gc.stop();
            System.exit(0);
        }
    }

}
