package edu.hm.dako.chat.AuditLogServerNew;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.connection.ServerSocketInterface;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public abstract class AbstractServer implements ServerInterface {
    static final Logger log = Logger.getLogger(AbstractServer.class);

    final int serverPort;
    ServerSocketInterface serverSocket;

    private ConnectionWorkerThread connectionWorker;
    private AuditLogModelInterface model;

    AbstractServer() {
        this(ServerInterface.AUDIT_LOG_SERVER_PORT);
    }

    AbstractServer(int serverPort) {
        this.serverPort = serverPort;
        initLogger();
    }

    AbstractServer(int serverPort, AuditLogModelInterface model) {
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
            connectionWorker.interrupt();
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
     * Creates a new {@link AuditLogModelInterface} instance, if {@code model} is <code>null</code>.
     *
     * @return a instance of {@link AuditLogModelInterface}.
     */
    private AuditLogModelInterface getModel() {
        if (model == null) {
            model = new AuditLogModel();
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
