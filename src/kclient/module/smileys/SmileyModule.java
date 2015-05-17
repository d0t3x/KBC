package kclient.module.smileys;

import java.io.File;
import java.util.List;
import kclient.Start;
import kclient.knuddels.GroupChat;
import kclient.knuddels.network.GameConnection;
import kclient.knuddels.network.generic.GenericProtocol;
import kclient.knuddels.tools.toolbar.Button;
import kclient.module.Module;
import kclient.module.ModuleBase;
import kclient.tools.Parameter;
import kclient.tools.Util;

/**
 *
 * @author SeBi
 */
public class SmileyModule extends ModuleBase implements Module {
    private Parameter settings;
    
    public SmileyModule(GroupChat groupChat) {
        super(groupChat);
        super.state = true;
    }

    @Override
    public String getName() {
        return "Smileys";
    }
    @Override
    public String getAuthor() {
        return "°>_hSeBi|https://u-labs.de/members/sebi-2841/<°";
    }
    @Override
    public String getDescription() {
        return "Das SmileyModule ermöglich dir jeden Smiley der auf dem Knuddels Server liegt "
                + "über einen von dir festgelten Syntax ersetzen und damit anzeigen zu lassen##"
                + "_Anleitung:_#"
                + "In der Datei 'data/module/smileys.properties' den Smiley mit dem Syntax "
                + "(_REPLACEMENT_ = _URL_) eintragen.#Anschließend den Client neustarten oder im Chat"
                + " _°>/mdl " + getName() + " reload|/mdl " + getName() + " reload<°_ eingeben.##"
                + "Danach kannst du den Smiley mit dem von dir festgelegtem Replacement verwenden.##"
                + "_Beispiel:_#"
                + "Aus :-) wird °>sm_00.gif<°";
    }
    @Override
    public String getVersion() {
        return "1.0." + Start.REVISION;
    }

    @Override
    public List<Button> getButtons(String channel) {
        return null;
    }

    @Override
    public String handleInput(String packet, String[] tokens) {
        if (!this.getState())
            return packet;
        String opcode = tokens[0];
        if (opcode.equals("e")) {
            String[] tmp = tokens[3].split(" ");
            for (int i = 0; i < tmp.length; i++)
                if (this.settings.containsKey(tmp[i].toLowerCase()))
                    tmp[i] = this.settings.get(tmp[i].toLowerCase());
            tokens[3] = Util.join(" ", tmp);
            packet = Util.join(GroupChat.delimiter, tokens);
        } else if (opcode.equals("r")) {
            String[] tmp = tokens[4].split(" ");
            for (int i = 0; i < tmp.length; i++)
                if (this.settings.containsKey(tmp[i].toLowerCase()))
                    tmp[i] = this.settings.get(tmp[i].toLowerCase());
            tokens[4] = Util.join(" ", tmp);
            packet = Util.join(GroupChat.delimiter, tokens);
        }
        return packet;
    }
    @Override
    public String handleOutput(String packet, String[] tokens) {
        if (!this.getState())
            return packet;
        String opcode = tokens[0];
        
        return packet;
    }

    @Override
    public GenericProtocol handleExtendInput(GameConnection connection, GenericProtocol protocol) {
        return protocol;
    }
    @Override
    public GenericProtocol handleExtendOutput(GameConnection connection, GenericProtocol protocol) {
        return protocol;
    }

    @Override
    public boolean handleCommand(String cmd, String arg, String channel) {
        if (arg.equals("reload")) {
            this.load();
            return true;
        }
        return false;
    }

    @Override
    public void save() {
    }
    @Override
    public void load() {
        this.settings = new Parameter("module" + File.separator + "smileys");
    }
    
}
