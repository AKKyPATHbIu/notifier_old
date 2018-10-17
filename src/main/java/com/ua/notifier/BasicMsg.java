/**
 * Базовий клас для абстракного месседжа (пошта, смс, вайбер...)
 */

package com.ua.notifier;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Logger;

public abstract class BasicMsg {

    private static Logger logger = Logger.getLogger(BasicMsg.class.getName());

    private String address;
    private String subject;
    private String body;
    private String type;
    protected  Integer typeId;

    //Constants messae types
    public static final Integer MSGTP_RABBIT=1;
    public static final Integer MSGTP_MAIL=2;
    public static final Integer MSGTP_VIBER=3;
    public static final Integer MSGTP_TELEGRAM=4;
    public static final Integer MSGTP_SMS=5;


    public BasicMsg(JSONObject json) throws JSONException{
        super();
        setAddress(json.getString("address"));
        setSubject(json.getString("subject"));
        setBody(json.getString("body"));
    }

    //Відправка мессаджа - буде overrided в класах нащадках
    public void send () {};


    @Override
    public String toString() {
        return "BasicMsg{" +
                "address='" + address + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", type='" + type + '\'' +
                ", typeId=" + typeId +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

}
