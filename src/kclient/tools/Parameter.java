package kclient.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author SeBi
 */
public class Parameter {
    private static final Parameter default_params;
    static {
        default_params = new Parameter("params");
    }
    
    public static Parameter getDefault() {
        return default_params;
    }
    
    private final Map<String, String> values;
    
    public Parameter(String file) {
        this.values = new HashMap<>();
        Properties props = new Properties();
        if (file.startsWith("http")) {
            InputStream inp = null;
            try {
                URL url = new URL(file);
                inp = url.openStream();
                props.load(inp);
            } catch (IOException e) {
                Logger.get().error(e);
            } finally {
                if (inp != null)
                    try {
                        inp.close();
                    } catch (IOException e) {
                    }
            }
        } else {
            FileReader reader = null;
            
            file = "data" + File.separator + file + ".properties";
            try {
                reader = new FileReader(file);
                props.load(reader);
            } catch (IOException e) {
                Logger.get().error(e);
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    Logger.get().error(e);
                }
            }
        }
        
        for (String key : props.stringPropertyNames())
            this.values.put(key.toLowerCase(), props.getProperty(key));
        props.clear();
    }
    
    public boolean containsKey(String key) {
        return this.values.containsKey(key.toLowerCase());
    }
    
    public String get(String key) {
        if (this.values.containsKey(key.toLowerCase()))
            return this.values.get(key.toLowerCase());
        return null;
    }
    public int getInt(String key) {
        String val = get(key);
        if (val == null)
            return -1;
        return Integer.parseInt(val);
    }
    public boolean getBoolean(String key) {
        if (this.containsKey(key))
            return this.get(key).equals("true");
        return false;
    }    
    public Color getColor(String key) {
        String strColor = get(key);
        if (strColor == null || strColor.isEmpty())
            return Color.black;
        if (strColor.contains(",")) {
            String[] tmp = strColor.split(",");
            if (tmp.length != 3)
                return Color.black;
            return new Color(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
        }
        return Color.decode("#" + strColor);
    }
    
    public Collection<Map.Entry<String, String>> entrySet() {
        return this.values.entrySet();
    }
    public Collection<String> getAll() {
        return this.values.values();
    }
    
    public void close() {
        this.values.clear();
    }
}