package server.jms.client_to_worker;

import org.apache.activemq.broker.region.policy.LastImageSubscriptionRecoveryPolicy;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GmailFromClient {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail = "projectservertest@gmail.com";
    String fromPassword = "projectservertest1";
    String emailSubject = "Test";
    String emailBody;
    List<String> toList;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public GmailFromClient(String[] toArray) throws UnsupportedEncodingException, MessagingException {

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        ArrayList<String> toList = new ArrayList<>(Arrays.asList(toArray));
        String purpose = toList.get(0);
        toList.remove(0);
        if(purpose.equals("toNotify")) {
            emailBody = "Вам доступен новый заказ для выполнения!\n" +
                    "Подробности можно увидеть в личном кабинете: http://localhost:4200/user-auth";
        }
        if(purpose.equals("toReportOnConfirm")){
            emailBody = "Ваша заявка на заказ принята!\n" +
                    "Проверить детали заказа можно в ичном кабинете: http://localhost:4200/user-auth";

        }
        this.toList = toList;
        createEmailMessage();
    }

    public MimeMessage createEmailMessage()
            throws MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (String email : toList) {
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
        }

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));

        emailMessage.setSubject(emailSubject);
        //emailMessage.setContent(emailBody, "text/html");// for a html email
         emailMessage.setText(emailBody);// for a text email
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }

}