package server.payments;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class PaymentGmail {
    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    public PaymentGmail(String email, long price) throws MessagingException {
        setMailServerProperties();
        createEmailMessage(email, price);
    }

    private void setMailServerProperties() {

        String emailPort = "587"; // gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    private void createEmailMessage(String email, long price) throws MessagingException {
        String emailSubject = "Вам поступил платеж!";
        String emailBody;
        emailBody = "Ваш месячный доход составил\n" + price + "!";
        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

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
