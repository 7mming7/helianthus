package mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.activation.FileDataSource;
import javax.mail.Message;

/**
 * User: shuiqing
 * DateTime: 17/4/24 上午11:24
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class MailTest {

    private static final String YOUR_EMAIL_ADDRESS = "793885652@qq.com";

    private static final String YOUR_EMAIL_PASSWORD = "463668gmm...";

    private static final ServerConfig serverConfigSMTP = new ServerConfig("smtp.qq.com", 25, YOUR_EMAIL_ADDRESS, YOUR_EMAIL_PASSWORD);

    private static final boolean LOGGING_MODE = false;

    public static void main(String[] args){

        Email emailNormal = new Email();
        emailNormal.setFromAddress("793885652", "793885652@qq.com");
        emailNormal.addRecipient("helianthus301", "helianthus301@163.com", Message.RecipientType.TO);
        emailNormal.setTextHTML("<b>We should meet up!</b><img src='cid:thumbsup'>");
        emailNormal.setSubject("hey1");

        /*String base64String = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABeElEQVRYw2NgoAAYGxu3GxkZ7TY1NZVloDcAWq4MxH+B+D8Qv3FwcOCgtwM6oJaDMTAUXOhmuYqKCjvQ0pdoDrCnmwNMTEwakC0H4u8GBgYC9Ap6DSD+iewAoIPm0ctyLqBlp9F8/x+YE4zpYT8T0LL16JYD8U26+B7oyz4sloPwenpYno3DchCeROsUbwa05A8eB3wB4kqgIxOAuArIng7EW4H4EhC/B+JXQLwDaI4ryZaDSjeg5mt4LCcFXyIn1fdSyXJQVt1OtMWGhoai0OD8T0W8GohZifE1PxD/o7LlsPLiFNAKRrwOABWptLAcqc6QGDAHQEOAYaAc8BNotsJAOgAUAosG1AFA/AtUoY3YEFhKMAvS2AE7iC1+WaG1H6gY3gzE36hUFJ8mqzbU1dUVBBqQBzTgIDQRkWo5qCZdpaenJ0Zx1aytrc0DDB0foIG1oAYKqC0IZK8D4n1AfA6IzwPxXpCFoGoZVEUDaRGGUTAKRgEeAAA2eGJC+ETCiAAAAABJRU5ErkJggg==";
        emailNormal.addEmbeddedImage("thumbsup", parseBase64Binary(base64String), "image/png");*/

        FileDataSource fds = new FileDataSource("/Users/gemingming/Downloads/88E58PICixT_1024.jpg");
        emailNormal.addEmbeddedImage("thumbsup",fds);

        // note: the following statements will produce 6 new emails!
        sendMail(emailNormal);
    }

    private static void sendMail(final Email email) {
        // ProxyConfig proxyconfig = new ProxyConfig("localhost", 1030);
        sendMail(serverConfigSMTP, TransportStrategy.SMTP_TLS, email);
    }

    private static void sendMail(ServerConfig serverConfigSMTP, TransportStrategy smtpTls, Email email) {
        Mailer mailer = new Mailer(serverConfigSMTP, smtpTls);
        mailer.setTransportModeLoggingOnly(LOGGING_MODE);
        mailer.sendMail(email);
    }
}
