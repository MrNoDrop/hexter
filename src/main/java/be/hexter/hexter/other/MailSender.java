package be.hexter.hexter.other;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private static final String USERNAME = "patryk.sitko.algemeen@gmail.com";// change accordingly
    private static final String PASSWORD = "bbfc vvue oxdf qfwk";

    private static final Properties PROPS;
    static {
        PROPS = new Properties();
        PROPS.put("mail.smtp.auth", "true");
        PROPS.put("mail.smtp.starttls.enable", "true");
        PROPS.put("mail.smtp.host", "smtp.gmail.com");
        PROPS.put("mail.smtp.port", "587");
    }

    private static final Session session;
    static {
        session = Session.getInstance(PROPS, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    private static final Message message;
    static {
        message = new MimeMessage(session);
    }

    public static void send(List<String> recipients, String subject, Message message)
            throws MessagingException, AddressException, MessagingException {
        message.setFrom(new InternetAddress(USERNAME));
        for (String recipient : recipients) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        }
        message.setSubject(subject);
        Transport.send(message);
    }
}
