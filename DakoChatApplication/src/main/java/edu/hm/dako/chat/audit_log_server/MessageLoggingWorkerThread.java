package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.ExceptionHandler;
import org.apache.log4j.Logger;

import java.util.List;

public class MessageLoggingWorkerThread extends Thread {
    private static final Logger log = Logger.getLogger(MessageLoggingWorkerThread.class);
    private final AuditLogPduDaoInterface<AuditLogPDU> model;
    private int messageCounter = 0;

    public MessageLoggingWorkerThread(AuditLogPduDaoInterface<AuditLogPDU> model) {
        this.model = model;
        setDaemon(true);
    }

    /**
     * Waits for new {@link AuditLogPDU} messages from the {@link AuditLogPduDaoInterface#getAllNew()}
     * and then processes them.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final List<AuditLogPDU> allNew = model.getAllNew();
                messageCounter += allNew.size();
                log.debug("Anzahl neuer Nachrichten: " + messageCounter);
                // TODO: Add logging
                for (AuditLogPDU auditLogPDU : allNew) {
                    log.debug(auditLogPDU);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ExceptionHandler.logException(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
