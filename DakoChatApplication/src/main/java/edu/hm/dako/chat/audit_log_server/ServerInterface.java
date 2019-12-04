package edu.hm.dako.chat.audit_log_server;

public interface ServerInterface {
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
