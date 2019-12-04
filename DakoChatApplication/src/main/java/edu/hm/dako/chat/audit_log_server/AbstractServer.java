package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.connection.ServerSocketInterface;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public abstract class AbstractServer implements ServerInterface {
    /**
     * Default server port.
     */
    static final int AUDIT_LOG_SERVER_PORT = 40001;
    /**
     * Default receive buffer size for server port in bytes.
     */
    static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;
    /**
     * Default send buffer size for server port in bytes.
     */
    static final int DEFAULT_SENDBUFFER_SIZE = 30000;
    static final Logger log = Logger.getLogger(AbstractServer.class);

    static ServerSocketInterface serverSocket;

    final int serverPort;

    private ConnectionWorkerThread connectionWorker;
    private AuditLogPduDaoInterface<AuditLogPDU> model;

    AbstractServer() {
        this(AbstractServer.AUDIT_LOG_SERVER_PORT);
    }

    AbstractServer(int serverPort) {
        this.serverPort = serverPort;
        initLogger();
    }

    AbstractServer(int serverPort, AuditLogPduDaoInterface<AuditLogPDU> model) {
        this(serverPort);
        this.model = model;
    }

    /**
     * Initializes the configuration for {@link #log}.
     *
     * @see PropertyConfigurator
     */
    abstract void initLogger();

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        start(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(boolean blocking) {
        try {
            if (connectionWorker == null) {
                connectionWorker = new ConnectionWorkerThread(getServerSocket(), getModel());
            }
            connectionWorker.start();

            if (blocking) {
                connectionWorker.join();
            }
        } catch (IOException e) {
            log.error("Socket konnte nicht initialisiert werden");
            ExceptionHandler.logException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("ConnectionWorkerThread wurde unterbrochen");
            ExceptionHandler.logException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (connectionWorker != null) {
            log.error("ConnectionWorkerThread wird unterbrochen");
            connectionWorker.interrupt();
        }
    }

    /**
     * Creates a new {@link AuditLogPduDaoInterface} instance, if {@code model} is <code>null</code>.
     *
     * @return a instance of {@link AuditLogPduDaoInterface}.
     */
    private synchronized AuditLogPduDaoInterface<AuditLogPDU> getModel() {
        if (model == null) {
            model = new AuditLogPduDao();
        }

        return model;
    }

    /**
     * Creates a new {@link ServerSocketInterface} instance, if {@code serverSocket} is <code>null</code>.
     *
     * @return a instance of {@link ServerSocketInterface}.
     * @throws IOException if the new instance can not be created.
     */
    abstract ServerSocketInterface getServerSocket() throws IOException;
}
