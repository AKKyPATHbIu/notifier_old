/**
 * Класс для вайбер-повідомлення
 * Викликаэться через рефлексію в конструкторі Task.java
 * Реалізує конкретний механізм відправки цього типа повідомлення
 */
package com.ua.notifier;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Logger;


public class ViberMsg extends BasicMsg {

    private static Logger logger = Logger.getLogger(ViberMsg.class.getName());

    public ViberMsg(JSONObject json) throws JSONException{
        super(json);
        setType("Viber");
        setTypeId(BasicMsg.MSGTP_VIBER);
    }

    @Override
    public void send() {
        System.out.println(this.toString());
        //throw new RuntimeException("opa!");
    }

}
