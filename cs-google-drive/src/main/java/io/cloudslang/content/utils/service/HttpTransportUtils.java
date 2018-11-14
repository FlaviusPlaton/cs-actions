/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.utils.service;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.base.Strings;

import java.io.IOException;
import java.net.*;
import java.security.GeneralSecurityException;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class HttpTransportUtils {

    private HttpTransportUtils() {
        // prevent instantiation
    }

    public static NetHttpTransport getNetHttpTransport(String proxyHostOpt, String proxyPort, String proxyUserOpt, String proxyPassword) throws IOException, GeneralSecurityException {
        return new NetHttpTransport.Builder()
                .trustCertificates(GoogleUtils.getCertificateTrustStore())
                .setProxy(getProxy(proxyHostOpt, proxyPort, proxyUserOpt, proxyPassword))
                .build();
    }

    private static Proxy getProxy(String proxyHostOpt, String proxyPort, String proxyUserOpt, String proxyPassword) {
        if(!Strings.isNullOrEmpty(proxyHostOpt)) {
            if(!Strings.isNullOrEmpty(proxyUserOpt)) {
                Authenticator.setDefault(new ProxyAuthenticator(proxyUserOpt, proxyPassword));
            }
            SocketAddress addr = new InetSocketAddress(proxyHostOpt, Integer.valueOf(proxyPort));
            return new Proxy(Proxy.Type.HTTP, addr);
        } else {
            return Proxy.NO_PROXY;
        }
    }

    private static class ProxyAuthenticator extends Authenticator {
        private String user;
        private String password;

        private ProxyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            if(getRequestorType() == RequestorType.PROXY) {
                return new PasswordAuthentication(user, password.toCharArray());
            }
            return null;
        }
    }
}
