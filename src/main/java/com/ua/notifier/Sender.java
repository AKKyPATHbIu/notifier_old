/**
 * Клас , що займається крупновузловим опрацюванням завдань.
 */

package com.ua.notifier;

import org.json.JSONException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    private static Logger logger = Logger.getLogger(Sender.class.getName());

    public void sendNotification () throws SQLException, JSONException, IOException {

        BasicDB db1 = new BasicDB();
        //Отримуємо з БД пакет завдать recordCount штук
        List<Task> taskList=db1.getTaskSet(Integer.parseInt(Main.props.getProperty("recordCount")));

        String taskStatus = "";
        for ( Task tsk:taskList) {
            try {
                // Далі все в одній транзакції - рядок в notifier_tasks заблоковано процедурою db1.changeTaskStatus.
                db1.changeTaskStatus(tsk.getId(), BasicDB.STATUS_INPROGRESS, null);
                taskStatus = BasicDB.STATUS_OK;
                    // проходимось по кожному мессаджу в межах однієї таски
                    for (BasicMsg msg : tsk.getNotifications()) {
                        Integer logId = db1.insertLog(tsk.getId(),msg.getTypeId());
                        try {
                            if (Main.getConfigMap().get(msg.getTypeId()).getEnabled()) {
                                msg.send();
                            }
                        }catch (Exception e) {
                                logger.log(Level.SEVERE, Utils.getStackTrace(e));
                                db1.changeLogStatus(logId,BasicDB.STATUS_ERR,Utils.getStackTrace(e));
                                taskStatus=BasicDB.STATUS_PARTIAL;
                        }
                    }
                db1.changeTaskStatus(tsk.getId(), taskStatus, null);
                db1.getDbConn().commit();
            }catch (Exception e ) {
                logger.log(Level.SEVERE, Utils.getStackTrace(e));
                db1.changeTaskStatus(tsk.getId(), BasicDB.STATUS_ERR, Utils.getStackTrace(e));
                db1.getDbConn().commit();
            }
        }
    }

}
