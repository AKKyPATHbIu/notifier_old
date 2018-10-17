/**
 * Класс для email повідомлення.
 * Для відправки використовується javax.mail.
 * Викликаэться через рефлексію в конструкторі Task.java
 * Реалізує конкретний механізм відправки цього типа повідомлення
 */

package com.ua.notifier;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Transport;


public class MailMsg extends BasicMsg {

    private static Logger logger = Logger.getLogger(MailMsg.class.getName());

    public MailMsg(JSONObject json) throws JSONException {
        super(json);
        setType("Mail");
        setTypeId(BasicMsg.MSGTP_MAIL);
    }

    @Override
    public void send() {
        String from = "a.tovstyuk@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.auth", "true");
        //props.setProperty("mail.smtp.host", Main.getConfigMap().get(MailMsg.getTypeId()).getGateHost());
        //props.setProperty("mail.smtp.port", "465");
        //props.put("mail.debug", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGateLogin(),
                                                          Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGatePasswd());
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getAddress()));
            message.setSubject(this.getSubject());
            message.setText(this.getBody());

            Transport transport = session.getTransport("smtps");
            transport.connect (Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGateHost(),
                               Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGatePort(),
                               Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGateLogin(),
                               Main.getConfigMap().get(BasicMsg.MSGTP_MAIL).getGatePasswd());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.info("Mail sent.");

        } catch (MessagingException e) {
            logger.log(Level.SEVERE,Utils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }


}
