package com.ua.notifier;

/**
 * Головний клас програми, що вичитуэ завдання з БД (notifier_task) та здійснює розсилку повідомлень
 * за типами месенджерів та адресами що зберігаються у завданні.
 */

import org.json.JSONException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    public  static Properties props = new Properties();
    private static final String CONFIG = "notifier.properties";

    private static HashMap<Integer,ConfigItem> configMap = new HashMap<>();


    //Завантаження параметрів програми  з проперті файла
    private static void loadProps() throws IOException {
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(CONFIG)) {
            props.load(in);
            in.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE,Utils.getStackTrace(e));
            e.printStackTrace();
            System.exit(1);
        }
    }
    // Завантаження до хешмепа параметрів всіх месенджерів (хостб портб шлюз...)
    private static void setConfigMap()  throws IOException, SQLException{
        BasicDB db = new BasicDB();
        configMap = db.getConfig();
    }

    public static void main(String[] args)  throws java.io.IOException , InterruptedException, SQLException, JSONException {
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        logger.log(Level.INFO,"Application started ... ");
        loadProps();
        setConfigMap();

        try {
            Sender snd = new Sender();
            snd.sendNotification();
        } catch (Exception e) {
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
            throw e;
        }
    }

    public static HashMap<Integer, ConfigItem> getConfigMap() {
        return configMap;
    }


}
