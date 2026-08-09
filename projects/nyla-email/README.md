# nyla-email

JavaMail wrapper APIs for sending and receiving mail over **SMTP**, **IMAP**, and **POP3**.

Maven artifact: `com.github.nyla-solutions:nyla.solutions.email` (see `build.gradle` for the current version).

Depends on [NYLA Core](../core) (`Config`, `Cryption`, and related utilities).

---

## Configuration

Mail settings are read through [`Config`](../core/src/main/java/nyla/solutions/core/util/Config.java) from `config.properties`, Java system properties, or environment variables (NYLA’s usual configuration rules apply).

Passwords should be stored encrypted using [`Cryption`](../core/src/main/java/nyla/solutions/core/util/Cryption.java) (`{cryption}...` values).

Example `config.properties` entries:

```properties
# Mail server
mail.host=smtp.office365.com

# Whether SMTP auth is required
mail.auth.required=true

# From address
mail.from=imani@home.org

# Encrypted password (generate with Cryption)
mail.from.password={cryption}HMlC6NiiCErfg3KTYXjktA==

mail.smtp.port=587
mail.port=587
mail.smtp.ssl.enable=false
mail.smtp.starttls.enable=true
```

### Environment-friendly property names

These aliases are also supported (same semantics as the dotted keys above):

```properties
MAIL_HOST=hostname
MAIL_FROM_PASSWORD={cryption}122321sadsdsd==
CRYPTION_KEY=SALT
MAIL_AUTH_REQUIRED=true
MAIL_PORT=25
MAIL_SMTP_SSL_ENABLE=false
MAIL_SMTP_STARTTLS_ENABLE=true
```

---

## Send mail

```java
import nyla.solutions.core.util.Config;
import nyla.solutions.email.Email;

String to = "green_gregory@yahoo.com";
String subject = "Test";
String messageBody = "<b>Hello World</b>";

Email email = new Email();
email.setMailFromUser(Config.settings().getProperty("junit.from.user"));
email.sendMail(to, subject, messageBody);
```

---

## Read mail

```java
import nyla.solutions.email.Email;
import nyla.solutions.email.data.EmailMessage;

import java.util.Collection;

Email email = new Email();
int count = 10;
int startIndex = 1;
String subjectPattern = ".*";

Collection<EmailMessage> results = email.readMatches(count, startIndex, subjectPattern);

for (EmailMessage message : results) {
    System.out.println("message:" + message);
}
```
