/**
 * Клас інкапсулює в собі методи роботи з БД
 */

package com.ua.notifier;

import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BasicDB {

    private static Logger logger = Logger.getLogger(BasicDB.class.getName());
    private Connection dbConn;

    public static final String STATUS_NEW="NEW";
    public static final String STATUS_INPROGRESS="INPROGRESS";
    public static final String STATUS_OK="OK";
    public static final String STATUS_ERR="ERR";
    public static final String STATUS_PARTIAL="PARTIAL";


    //Ініціалізація зьеднання з БД. Автокомміт відключаємо.
    public BasicDB() throws IOException, SQLException{
        try {
            setDbConn(DriverManager.getConnection(Main.props.getProperty("dbServer"),
                                                  Main.props.getProperty("dbUser"),
                                                  Main.props.getProperty("dbPass")));
            dbConn.setAutoCommit(false);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,"Unable connect to database." +Utils.getStackTrace(e));
            throw e;
        }
    }

    //Процедура повертає список завдань, по яких мають бути вислані повідомлення
    public List<Task> getTaskSet(int cnt) {

        List<Task> taskList = new ArrayList<>();

        String sql_str = "select id, status, package from secembas.notifier_tasks where status in ('NEW') order by dt limit ? ";

        try (PreparedStatement smt = dbConn.prepareStatement(sql_str)) {
            smt.setInt(1, cnt);
            ResultSet rs = smt.executeQuery();
            while (rs.next()) {
                try {
                    JSONObject json = new JSONObject(rs.getString("package"));
                    taskList.add(new Task(rs.getInt("id"), json));
                }catch (Exception e) {
                    logger.log(Level.SEVERE,Utils.getStackTrace(e));
                    changeTaskStatus(rs.getInt("id"), STATUS_ERR, Utils.getStackTrace(e));
                    getDbConn().commit();
                }
            }
            rs.close();
            smt.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE,Utils.getStackTrace(e));
        }
        return taskList;
    }


    //Процедура змінює статуc завдання (notifier_tasks)
    public void changeTaskStatus(int taskId, String status, String errMsg) throws SQLException {
        CallableStatement sp = dbConn.prepareCall("{ ? = call secembas.notif_task_change_status( ?,?,?) }");
        sp.registerOutParameter(1, Types.INTEGER);
        sp.setInt(2, taskId);
        sp.setString(3, status);
        sp.setString(4, errMsg);
        sp.execute();
        sp.close();
    }

    //Процедуа вставляє запис про спробу відправки повідомлення до журналу відправлениї повідомлень (notifier_log)
    public Integer insertLog(int taskId, int notifierTp) throws SQLException {
        CallableStatement sp = dbConn.prepareCall("{ ? = call secembas.notif_log_add( ?,?) }");
        sp.registerOutParameter(1, Types.INTEGER);
        sp.setInt(2, taskId);
        sp.setInt(3, notifierTp);
        sp.execute();
        Integer id = sp.getInt(1);
        sp.close();
        return id;
    }
    ////Процедуа змінює статус відправки повідомлення в журналу відправлениї повідомлень
    public void changeLogStatus(int logId, String status, String errMsg) throws SQLException {
        CallableStatement sp = dbConn.prepareCall("{ ? = call secembas.notif_log_change_status( ?,?,?) }");
        sp.registerOutParameter(1, Types.INTEGER);
        sp.setInt(2, logId);
        sp.setString(3, status);
        sp.setString(4, errMsg);
        sp.execute();
        sp.close();
    }

    //Процедура вичитує масив параметрів\характеристик для всіх типів повідомлень з  notifier_tp в хешмапу
    public HashMap<Integer,ConfigItem> getConfig() {
        HashMap<Integer,ConfigItem> configMap = new HashMap();
        String sql_str = "select id,enabled, retry_count, gate_host,gate_port, gate_login, gate_passwd from secembas.notifier_tp where enabled = true ";

        try (PreparedStatement smt = dbConn.prepareStatement(sql_str)) {
            ResultSet rs = smt.executeQuery();
            while (rs.next()) {
                configMap.put(rs.getInt("id"),
                              new ConfigItem(rs.getInt("id"),
                                             rs.getBoolean("enabled"),
                                             rs.getInt("retry_count"),
                                             rs.getString("gate_host"),
                                             rs.getInt("gate_port"),
                                             rs.getString("gate_login"),
                                             rs.getString("gate_passwd")
                                            )
                             );
            }
            rs.close();
            smt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE,Utils.getStackTrace(e));
        }
        return configMap;
    }


    public Connection getDbConn() {
        return dbConn;
    }

    public void setDbConn(Connection dbConn) {
        this.dbConn = dbConn;
    }

}
