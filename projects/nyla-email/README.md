# nyla-email
Emails (POP3, SMTP, IMAP) send/receive wrapper APIs using JavaMail


# Configuration

This module using the [NYLA](https://github.com/nyla-solutions/nyla) [Config](https://github.com/nyla-solutions/nyla/blob/master/src/main/java/nyla/solutions/core/util/Config.java) object. It supports setting the following properties from config.properties, or Java System Properties or Environment variables. 

The passwords are encrypted using the [Cryption object](https://github.com/nyla-solutions/nyla/blob/master/src/main/java/nyla/solutions/core/util/Cryption.java)
	
	
	#Mail Server
	mail.host=smtp.office365.com
	
	@Flag whether the from user/password is needed
	mail.auth.required=true
	
	#EMail from user
	mail.from=imani@home.org
	
	
	#Mail User password using Nyla Cryption
	mail.from.password={cryption}HMlC6NiiCErfg3KTYXjktA==
	
	# Mail SMTP port
	mail.smtp.port=587
	
	# Mail email Default port
	mail.port=587
	
	# Whether SSL is required
	mail.smtp.ssl.enable=false
	
	# Whether TLS encryption is used
	mail.smtp.starttls.enable=true
	

## Send Mail Usage 

		String to = "green_gregory@yahoo.com";
		String subject = "Test";
		String messageBody = "<b>Hello World</b>";
		
		Email email = new Email();
		email.setMailFromUser(Config.getProperty("junit.from.user"));
		email.sendMail(to, subject, messageBody);
		
## Read Mail Usage

		Email email = new Email();
		int count = 10;
		int startIndex = 1;
		String subjectPattern = ".*";
		
		Collection<EmailMessage> results = email.readMatches(count, startIndex, subjectPattern);
		
		for (EmailMessage message : results)
		{
			System.out.println("message:"+message);
		}

		MAIL_FORM=husbands@nbccministries.org
		

## The Mail System Friendly Properties


	MAIL_HOST=hostname
	MAIL_FROM_PASSWORD={cryption}122321sadsdsd==
	CRYPTION_KEY=SALT
	MAIL_AUTH_REQUIRED=true
	MAIL_PORT=25
	MAIL_SMTP_SSL_ENABLE=false
	MAIL_SMTP_STARTTLS_ENABLE=true