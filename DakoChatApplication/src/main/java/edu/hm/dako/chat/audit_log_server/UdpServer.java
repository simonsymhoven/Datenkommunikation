package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.connection.ServerSocketInterface;
import edu.hm.dako.chat.udp.UdpServerSocket;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class UdpServer extends AbstractServer {
    public UdpServer() {
        super();
    }

    public UdpServer(int serverPort) {
        super(serverPort);
    }

    public UdpServer(int serverPort, AuditLogModelInterface model) {
        super(serverPort, model);
    }

    public static void main(String[] args) {
        ServerInterface server;
        if (args.length == 0) {
            server = new UdpServer();
        } else {
            String arg = args[0];
            server = new UdpServer(Integer.parseInt(arg));
        }
        server.start(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void initLogger() {
        PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized ServerSocketInterface getServerSocket() throws IOException {
        if (serverSocket == null) {
            serverSocket = new UdpServerSocket(
                serverPort,
                ServerInterface.DEFAULT_SENDBUFFER_SIZE,
                ServerInterface.DEFAULT_RECEIVEBUFFER_SIZE
            );
        }
        log.info("Socket wurde auf Port " + serverPort + " initialisiert");

        return serverSocket;
    }
}
