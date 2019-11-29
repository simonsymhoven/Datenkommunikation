package edu.hm.dako.chat.AuditLogServerNew;

public interface ServerInterface {
    /**
     * Default server port.
     */
    int AUDIT_LOG_SERVER_PORT = 40001;
    /**
     * Default receive buffer size for server port in bytes.
     */
    int DEFAULT_RECEIVEBUFFER_SIZE = 800000;
    /**
     * Default send buffer size for server port in bytes.
     */
    int DEFAULT_SENDBUFFER_SIZE = 30000;

    /**
     * Starts a {@link ConnectionWorkerThread} without blocking.
     */
    void start();

    /**
     * Starts a {@link ConnectionWorkerThread}. If {@code blocking} is <code>true</code>,
     * this method blocks until the thread has finished.
     *
     * @param blocking If <code>true</code>, it is a blocking method.
     */
    void start(boolean blocking);

    /**
     * Stops the {@link ConnectionWorkerThread}.
     */
    void stop();
}
