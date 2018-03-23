package server.jms.worker_to_client;

import server.jpa.AvailableOrder;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class WorkerGmail {
    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    public WorkerGmail(AvailableOrder order) throws MessagingException {
        setMailServerProperties();
        createEmailMessage(order);
    }

    private void setMailServerProperties() {

        String emailPort = "587"; // gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    private void createEmailMessage(AvailableOrder order) throws MessagingException {
        String[] toEmails = { order.getClient().getEmail() };
        String emailSubject = "Ваш заказ хотят выполнить!";
        String emailBody;
        if(order.getWorkers().length == 1) {
            emailBody = "Ваш заказ уже хочет взять работник!\n Проверьте состояние заказа в личном кабинете:  " +
                    "http://localhost:4200/lkclient/" + order.getClient().getLogin() + "/" +
                    order.getClient().getPassword();
        } else {
            emailBody = "На Ваш заказ уже подали заявку " + order.getWorkers().length + " работников!\n" +
                    " Проверьте состояние заказа в личном кабинете:  " +
                    "http://localhost:4200/lkclient/" + order.getClient().getLogin() + "/" +
                    order.getClient().getPassword();
        }
        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (String toEmail : toEmails) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setText(emailBody);

    }

    public void sendEmail() throws MessagingException {

        String emailHost = "smtp.gmail.com";
        String fromUser = "projectservertest";//just the id alone without @gmail.com
        String fromUserEmailPassword = "projectservertest1";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }
}
