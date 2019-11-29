package edu.hm.dako.chat.AuditLogServerNew;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.EndOfFileException;
import edu.hm.dako.chat.connection.ServerSocketInterface;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;

public class ConnectionWorkerThread extends Thread {
    private static final Logger log = Logger.getLogger(ConnectionWorkerThread.class);
    private final AuditLogModelInterface model;
    private final ServerSocketInterface socket;
    /**
     * If an exception is thrown while {@link #handleIncomingMessage()},
     * this flag is set to terminate and close the {@link #socket} connection.
     */
    private boolean finished = false;

    private Connection connection;

    public ConnectionWorkerThread(ServerSocketInterface socket, AuditLogModelInterface model) {
        this.socket = socket;
        this.model = model;
        setDaemon(true);
    }

    /**
     * Waits for connection requests and then processes them.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
            finished = false;

            try {
                connection = socket.accept();
                while (!finished && !Thread.currentThread().isInterrupted()) {
                    handleIncomingMessage();
                }
            } catch (Exception e) {
                if (socket.isClosed()) {
                    log.debug("Socket wurde bereits geschlossen");
                } else {
                    log.error("Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
                    ExceptionHandler.logException(e);
                }
            }
        }
        closeConnection();
    }

    /**
     * Waits for the next message from the client and puts it in the {@link AuditLogModelInterface} {@link #model}.
     * If a connection related exception is thrown, the {@link #finished} flag is set to terminate the connection.
     */
    private void handleIncomingMessage() {
        try {
            AuditLogPDU receivedPdu = (AuditLogPDU) connection.receive();
            log.debug(receivedPdu);
            model.addMessage(receivedPdu);

        } catch (EndOfFileException e) {
            log.debug("End of File beim Empfang, vermutlich Verbindungsabbau des Partners");
            finished = true;

        } catch (SocketException e) {
            log.error("Verbindungsabbruch beim Empfang der naechsten Nachricht vom Client");
            finished = true;

        } catch (Exception e) {
            if (e instanceof IOException) {
                log.error("Empfang einer Nachricht fehlgeschlagen");
                finished = true;
            } else {
                log.error("Verarbeitung einer Nachricht fehlgeschlagen");
                ExceptionHandler.logException(e);
            }
        }
    }

    /**
     * Disconnects a client properly.
     */
    private void closeConnection() {
        try {
            connection.close();
            log.debug("Connection wurde geschlossen");
        } catch (Exception e) {
            log.error("Connection konnte nicht geschlossen werden");
            ExceptionHandler.logException(e);
        }
    }
}
