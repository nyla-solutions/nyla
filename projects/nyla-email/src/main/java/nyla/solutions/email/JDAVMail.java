package nyla.solutions.email;

import javax.mail.*;

import javax.mail.internet.*;

import java.util.*;

/**
 * <pre>
 *  JDAVMail provides a set of functions to send emails via the JDAV protocol
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class JDAVMail implements SendMail
{

   /**
    * Constructor for JDAVMail initializes internal data settings.
    */
   public JDAVMail()
   {
      super();
   }// --------------------------------------------

   /**
    * 
    * @see nyla.solutions.email.SendMail#sendMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
    */
   public void sendMail(String aTo, String aFrom, String aSubject, String aMessageBody) throws Exception
   {
      try
      {
         Properties prop = new Properties();
         // Set the default enveloppe sender address
         prop.setProperty("mail.davmail.from", "green_gregory@hotmail.com");
         Session ses = Session.getInstance(prop);

         // Create the transport connection
         Transport transport = ses.getTransport("davmail_xmit");
         transport.connect(null, "", "");

         // Prepare the message
         MimeMessage txMsg = new MimeMessage(ses);
         txMsg.setSubject("Test subject");

         InternetAddress addrFrom = new InternetAddress(
        		 aFrom);
         txMsg.setFrom(addrFrom);

         InternetAddress addrTo = new InternetAddress(
         "your_recipient's_address", "your_recipient's_name");
         txMsg.addRecipient(Message.RecipientType.TO, addrTo);

         txMsg.setText("Hello world !");
         txMsg.setSentDate(new Date());

         // Send the message
         transport.sendMessage(txMsg, txMsg.getAllRecipients());
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

}
