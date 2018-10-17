/**
 * Класс для смс -повідомлення.
 * Для відправки використовується smpp протокол.
 * Викликаэться через рефлексію в конструкторі Task.java
 * Реалізує конкретний механізм відправки цього типа повідомлення
 */

package com.ua.notifier;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsmpp.bean.*;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;


public class SmsMsg extends BasicMsg {

    private static Logger logger = Logger.getLogger(SmsMsg.class.getName());

    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();;


    public SmsMsg(JSONObject json) throws JSONException {
        super(json);
        setType("Sms");
        setTypeId(BasicMsg.MSGTP_SMS);
    }


    @Override
    public void send() {
        SMPPSession session = new SMPPSession();
        try {
            session.connectAndBind(Main.getConfigMap().get(BasicMsg.MSGTP_SMS).getGateHost(),
                                   Main.getConfigMap().get(BasicMsg.MSGTP_SMS).getGatePort(),
                                   new BindParameter(BindType.BIND_TX,
                                                    Main.getConfigMap().get(BasicMsg.MSGTP_SMS).getGateLogin(),
                                                    Main.getConfigMap().get(BasicMsg.MSGTP_SMS).getGatePasswd(),
                                                    "cp", TypeOfNumber.UNKNOWN,
                                                    NumberingPlanIndicator.UNKNOWN,
                                                    null)
                                    );

        } catch (IOException e) {
            logger.log(Level.SEVERE,"Failed connect and bind to host",e.toString());

        }

        try {
            String messageId = session.submitShortMessage("CMT",
                                                        TypeOfNumber.INTERNATIONAL,
                                                        NumberingPlanIndicator.UNKNOWN,
                                                        "alextov",
                                                        TypeOfNumber.INTERNATIONAL,
                                                        NumberingPlanIndicator.UNKNOWN,
                                                        "380504144695",
                                                        new ESMClass(), (byte)0, (byte)1,
                                                        timeFormatter.format(new Date()),
                                                        null,
                                                        new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0,
                                                        new GeneralDataCoding(Alphabet.ALPHA_DEFAULT,  MessageClass.CLASS1, false),
                                                        (byte)0,
                                                        "jSMPP simplify SMPP on Java platform".getBytes());
            logger.info("Message submitted, message_id is " + messageId);
        } catch (PDUException e) {
            logger.log(Level.SEVERE,"Invalid PDU parameter",e.toString());
        } catch (ResponseTimeoutException e) {
            logger.log(Level.SEVERE,"Response timeout",e.toString());
        } catch (InvalidResponseException e) {
            logger.log(Level.SEVERE,"Receive invalid respose",e.toString());
        } catch (NegativeResponseException e) {
            logger.log(Level.SEVERE,"Receive negative response",e.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE,"IO error occur",e.toString());
        }

        session.unbindAndClose();
    }


}
