/*
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package nyla.solutions.core.ds;

import nyla.solutions.core.util.Config;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import static nyla.solutions.core.util.Config.settings;

//Based on implementation by Charlie Black
public class SSLConfigSocketFactory extends SocketFactory {

	
	  public static final String LDAP_SSL_KEYSTORE_PROP = "LDAP_SSL_KEYSTORE";
	  public static final String LDAP_SSL_TRUSTSTORE_PROP = "LDAP_SSL_TRUSTSTORE";
	  public static final String LDAP_SSL_KEYSTORE_PASSWORS_PROP = "LDAP_SSL_KEYSTORE_PASSWORD";
	  public static final String LDAP_SSL_TRUSTSTORE_PASSWORS_PROP = "LDAP_SSL_TRUSTSTORE_PASSSWORD";

    private static SocketFactory instance;

    private SSLContext context;
    private SSLSocketFactory sslSocketFactory;

    public SSLConfigSocketFactory() throws Exception {
        context = SSLContext.getInstance("TLS");


        String keyStore = settings().getProperty(LDAP_SSL_KEYSTORE_PROP);
        String trustStore = settings().getProperty(LDAP_SSL_TRUSTSTORE_PROP);
        String keyStorePassword = settings().getProperty(LDAP_SSL_KEYSTORE_PASSWORS_PROP);
        String trustStorePassword = settings().getProperty(LDAP_SSL_TRUSTSTORE_PASSWORS_PROP);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore ks = KeyStore.getInstance("JKS");
        try (InputStream inputStream = new FileInputStream(keyStore)) {
            ks.load(inputStream, keyStorePassword.toCharArray());
        }
        kmf.init(ks, keyStorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ts = KeyStore.getInstance("JKS");
        try (InputStream inputStream = new FileInputStream(trustStore)) {
            ts.load(inputStream, trustStorePassword.toCharArray());
        }
        tmf.init(ts);

        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        sslSocketFactory = context.getSocketFactory();
    }

    public static synchronized SocketFactory getDefault() {
        if(instance == null) {
            try {
                instance = new SSLConfigSocketFactory();
            } catch (Exception e) {
      
                throw new RuntimeException("Could not instantiate the SSL Socket Factory.", e);
            }
        }
        return instance;
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslSocketFactory.createSocket();
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        return sslSocketFactory.createSocket(s, i);
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return sslSocketFactory.createSocket(s, i, inetAddress, i1);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return sslSocketFactory.createSocket(inetAddress, i);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return sslSocketFactory.createSocket(inetAddress, i, inetAddress1, i1);
    }
}
