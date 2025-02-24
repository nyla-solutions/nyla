package nyla.solutions.email;

public interface EmailTags
{

   public static final String MAIL_SESSION_JNDI_USED = "MAIL_SMTP_JNDI_SESSION_USED";

   public static final String MAIL_SESSION_JNDI_NAME = "MAIL_SMTP_JNDI_SESSION_NAME";
   
   /**
    * MAIL_FROM_PASSWORD_PROP = "MAIL_FROM_PASSWORD"
    */
   public static final String MAIL_FROM_PASSWORD_PROP = "MAIL_FROM_PASSWORD";

   /**
    * MAIL_SUBJECT_PROP = "MAIL_SUBJECT"
    */
   public static final String MAIL_SUBJECT_PROP = "MAIL_SUBJECT";
   

   /**
    * true then mail user/password required
    */
   public static final String MAIL_AUTHENICATION_REQUIRED_PROP = "MAIL_AUTH_REQUIRED";

   public static final String EMAIL_DELIMITER_IND = ";";

   public static final String MAIL_SERVER_PROP = "MAIL_HOST";

   /**
    * MAIL_FROM_ADDRESS_PROP = "MAIL_FROM"
    */
   public static final String MAIL_FROM_ADDRESS_PROP = "MAIL_FROM";
  
   public static final String  TEMPLATE_DIR_PROP = "TEMPLATE_DIR";

    public static final String TEMPLATE_NAME = "tmp_nm";

    public static final String EMAIL_US_TEMPLATE = "email_us";

    public static final String TO = "to";

    public static final String CATEGORY = "CATEGORY";

    public static final String CONTACT_ID = "CONTACT_ID";

    public static final String SUBJECT = "SUBJECT";

    public static final String BODY = "BODY";

    public static final String FROM_EMAIL = "MAIL_FROM";

    public static final String FROM_NAME = "MAIL_FROM";

    public static final String FILE_LIST = "FILE_LIST";

    public static final String FILE_TYPE = "FILE_TYPE";

    public static final String INCLUDE_FILES = "INCLUDE_FILES";

    public static final String CC = "CC";

}

