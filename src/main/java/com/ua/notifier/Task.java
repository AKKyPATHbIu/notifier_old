/**
 * Клас завдання - заповнюється данними з бд (notifier_task).
 * Містить в собі кілька повідомлень - створюються при розпаршуванні json з таблички завдань notifier_task.
 *
 */

package com.ua.notifier;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
//import com.ua.mqreader.Utils;
public class Task {

    private static Logger logger = Logger.getLogger(Task.class.getName());

    private int id;
    private Date dt;
    private Integer clinica;
    private ArrayList<BasicMsg> notifications = new ArrayList<>();
    private String status;
    private String errMsg;
    private JSONObject jObj;


    public Task(int id, JSONObject obj) throws ClassNotFoundException, JSONException, NoSuchMethodException,
                                                InstantiationException, IllegalAccessException, InvocationTargetException{
        super();
        setId(id);
        JSONArray arrNotif = obj.getJSONObject("data").getJSONArray ("messages");
        for (int i = 0; i < arrNotif.length(); i++) {
            //по параметру "тип повідомлення" в джейсоні отримуєм назву классу та створюємо його екземпляр
            Class msg = Class.forName("com.ua.notifier." + arrNotif.getJSONObject(i).getString("type"));
            notifications.add((BasicMsg) msg.getConstructor(JSONObject.class).newInstance(arrNotif.getJSONObject(i)));
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getClinica() {
        return clinica;
    }

    public void setClinica(Integer clinica) {
        this.clinica = clinica;
    }

    public List<BasicMsg> getNotifications() {
        return notifications;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public JSONObject getjObj() {
        return jObj;
    }

    public void setjObj(JSONObject jObj) {
        this.jObj = jObj;
    }
}
