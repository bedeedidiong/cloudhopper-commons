package com.cloudhopper.httpclient.util;

/*
 * #%L
 * ch-httpclient-util
 * %%
 * Copyright (C) 2012 - 2013 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Utility class for creating Apache Jakarta HttpClients.
 *
 * @author joelauer
 */
public class HttpClientFactory {

    private HttpClientFactory() {
        // only static methods
    }

    /**
     * Configures the HttpClient with an SSL TrustManager that will accept any
     * SSL server certificate.  The server SSL certificate will not be verified.
     * This method creates a new Scheme for "https" that is setup for an SSL
     * context to uses an DoNotVerifySSLCertificateTrustManager instance. This
     * scheme will be registered with the HttpClient using the
     * getConnectionManager().getSchemeRegistry().register(https) method.
     * @param client The HttpClient to configure.
     */
    static public void configureWithNoSslCertificateVerification(HttpClient client) throws NoSuchAlgorithmException, KeyManagementException {
        //
        // create a new https scheme with no SSL verification
        //
        Scheme httpsScheme = SchemeFactory.createDoNotVerifyHttpsScheme();

        //
        // register this new scheme on the https client
        //
        client.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }

    /**
     * Adding support for SSL mutual authentication using specified keystore/truststore.
     * Specifying keystore/truststore is optional. If unspecified, a normal SSL scheme
     * is created.
     */
    static public void configureWithSslKeystoreTruststore( HttpClient client, 
							   File keystoreFile,
							   String keystorePassword,
							   File truststoreFile,
							   String truststorePassword ) 
	throws CertificateException, FileNotFoundException, IOException, 
	       KeyStoreException, KeyManagementException, NoSuchAlgorithmException, 
	       UnrecoverableKeyException {

        //
        // create a new https scheme with no SSL verification
        //
        Scheme httpsScheme = SchemeFactory.createHttpsScheme( keystoreFile, keystorePassword,
							      truststoreFile, truststorePassword );
        //
        // register this new scheme on the https client
        //
        client.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }


    static public void configureWithPreemptiveBasicAuth(DefaultHttpClient client, String username, String passsword) {
        //
        // create a new request interceptor that includes adding basic auth
        //
        PreemptiveBasicAuthHttpRequestInterceptor interceptor = new PreemptiveBasicAuthHttpRequestInterceptor();

        // set credentials
        interceptor.setCredentials(username, passsword);

        // add as the first request interceptor
        client.addRequestInterceptor(interceptor, 0);
    }

}
