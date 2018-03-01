package org.forest.backend.httpclient.conn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;
import org.forest.http.ForestRequest;
import org.forest.ssl.SSLKeyStore;
import org.forest.ssl.SSLUtils;

@ThreadSafe
public class ForestSSLConnectionFactory implements LayeredConnectionSocketFactory {

    public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();

//    private final SSLSocketFactory socketfactory;
    private ThreadLocal<ForestRequest> requestLocal = new ThreadLocal<>();
    private final X509HostnameVerifier hostnameVerifier;

    public static org.apache.http.conn.ssl.SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
        return new org.apache.http.conn.ssl.SSLConnectionSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    private static String[] split(String s) {
        return TextUtils.isBlank(s)?null:s.split(" *, *");
    }

    public static org.apache.http.conn.ssl.SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
        return new org.apache.http.conn.ssl.SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public ForestSSLConnectionFactory() {
        this(BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }


    public ForestSSLConnectionFactory(X509HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier != null?hostnameVerifier:BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
    }

    protected void prepareSocket(SSLSocket socket) throws IOException {
    }

    public Socket createSocket(HttpContext context) throws IOException {
        return SocketFactory.getDefault().createSocket();
    }

    public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
        Args.notNull(host, "HTTP host");
        Args.notNull(remoteAddress, "Remote address");
        Socket sock = socket != null?socket:this.createSocket(context);
        if(localAddress != null) {
            sock.bind(localAddress);
        }

        try {
            sock.connect(remoteAddress, connectTimeout);
        } catch (IOException var11) {
            try {
                sock.close();
            } catch (IOException var10) {
                ;
            }

            throw var11;
        }

        if(sock instanceof SSLSocket) {
            SSLSocket sslsock = (SSLSocket)sock;
            sslsock.startHandshake();
            this.verifyHostname(sslsock, host.getHostName());
            return sock;
        } else {
            return this.createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
        }
    }


    private ForestRequest getCurrentRequest() {
        return requestLocal.get();
    }


    public void setCurrentRequest(ForestRequest request) {
        requestLocal.set(request);
    }


    public ForestRequest removeCurrentRequest() {
        ForestRequest request = requestLocal.get();
        requestLocal.remove();
        return request;
    }



    public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException {
        ForestRequest request = getCurrentRequest();
        SSLSocket sslsock = (SSLSocket)SSLUtils.getSSLSocketFactory(request)
                .createSocket(socket, target, port, true);
        if (request != null) {
            SSLKeyStore keyStore = request.getKeyStore();
            if (keyStore != null) {
                String[] protocols = keyStore.getProtocols();
                String[] cipherSuites = keyStore.getCipherSuites();
                if (protocols != null) {
                    sslsock.setEnabledProtocols(protocols);
                }
                if (cipherSuites != null) {
                    sslsock.setEnabledCipherSuites(cipherSuites);
                }
            }
        }
        this.prepareSocket(sslsock);
        sslsock.startHandshake();
        this.verifyHostname(sslsock, target);
        return sslsock;
    }

/*
    X509HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }
*/

    private void verifyHostname(SSLSocket sslsock, String hostname) throws IOException {
        try {
            this.hostnameVerifier.verify(hostname, sslsock);
        } catch (IOException var6) {
            try {
                sslsock.close();
            } catch (Exception var5) {
                ;
            }

            throw var6;
        }
    }
}