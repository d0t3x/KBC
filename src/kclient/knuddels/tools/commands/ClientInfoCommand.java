package kclient.knuddels.tools.commands;

import java.io.File;
import java.util.Map;
import kclient.Start;
import kclient.knuddels.GroupChat;
import kclient.knuddels.tools.KTab;
import kclient.knuddels.tools.popup.Popup;
import kclient.module.Module;
import kclient.module.ModuleBase;
import kclient.module.script.ScriptApp;
import kclient.module.script.ScriptModule;
import kclient.tools.Parameter;

/**
 *
 * @author SeBi
 */
public class ClientInfoCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        if (arg.equalsIgnoreCase("smileys")) {
            Parameter smileys = new Parameter("module" + File.separator + "smileys");
            StringBuilder buffer = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, String> e : smileys.entrySet()) {
                buffer.append(i).append(" - ").append(e.getKey()).append(" = ").append(e.getValue()).append("#°-°#");
                i++;
            }
            groupChat.receive(Popup.create("Smileys", "Smileys (" + smileys.getAll().size() + ")", buffer.toString(), 300, 650, true));
            //groupChat.print(channel, buffer.toString());
            return;
        }

        KTab tabPanel = new KTab(0, "KClient [1.0." + Start.REVISION +"] by SeBi", "°>U-Labs.de|https://u-labs.de/<°", "KClient", 
            "°>CENTER<°"
            + "°>http://knds.sebitm.info/kclient/logo.png<°#"
            + "°B°Knuddels Bot Client##°r°°>LEFT<°"
            + "Der KClient ist ein manipulierter Knuddels Client, der es ermöglicht den Client auf jede Art und Weise zu verändern und zu erweitern.##"
            + "Die Manipulation wird automatisch durchgeführt wenn man einen Login hinzufügt. Sollte der \"\"KLoader\"\" bereits eine Instanz der gewählten Applet Version "
            + "beinhalten wird diese verwendet und die Manipulation muss nicht erneut durchgeführt weden.##"
            + "Ein sehr großer Dank geht an °>patlux|https://u-labs.de/members/patlux-4/<° und °>UnReal|https://u-labs.de/members/unreal-2321/<°");

        for (Module mdl : groupChat.getModule()) {
            StringBuilder mdlBuffer = new StringBuilder();
            mdlBuffer.append("_Name:_ ").append(mdl.getName()).append("#");
            mdlBuffer.append("_Author:_ ").append(mdl.getAuthor()).append("#");
            mdlBuffer.append("_Version:_ ").append(mdl.getVersion()).append("#");
            mdlBuffer.append("_Beschreibung:_#").append(mdl.getDescription());
            tabPanel.newTab(mdl.getName(), (((ModuleBase)mdl).getState() ? "°>py_g.gif<°" : "°>py_r.gif<°") + mdl.getName() + "°+0600°" + (((ModuleBase)mdl).getState() ? 
                    "°BB>py_r.gif<>_hDeaktivieren|/mdl -" + mdl.getName() + "<°" : 
                    "°BB>py_g.gif<>_hAktivieren|/mdl +" + mdl.getName() + "<°") + "§", mdlBuffer.toString());
        }

        StringBuilder buffer = new StringBuilder();
        for (ScriptApp app : ((ScriptModule)groupChat.getModule("ScriptApi")).getApps()) {
            buffer.append("°>py_").append(app.getState() ? "g" : "r").append(".gif<° _").append(app.getName()).append("_ [").append(app.getVersion()).append("] by ").append(app.getAuthor()).append("#");
            buffer.append("°%05°").append(app.getDescription()).append("#");
            buffer.append("°BB>_hDeaktivieren|/mdl scriptapi -").append(app.getName()).append("<° | ");
            buffer.append("°BB>_hAktivieren|/mdl scriptapi +").append(app.getName()).append("<° | ");
            buffer.append("°BB>_hNeuladen|").append("/mdl scriptapi r").append(app.getName()).append("<°#°r°§°-°");
        }

        tabPanel.newTab("Apps", "Apps [°BB>_hReload|/mdl scriptapi reload<°]", buffer.toString());
        groupChat.receive(Popup.create("KClient", null, tabPanel.getSwitchTab(), 770, 570, false));
    }
    
}
