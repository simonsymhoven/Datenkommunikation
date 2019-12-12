package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.ExceptionHandler;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class MessageLoggingWorkerThread extends Thread {
    private static final Logger log = Logger.getLogger(MessageLoggingWorkerThread.class);
    private final AuditLogPduDaoInterface<AuditLogPDU> model;
    private int messageCounter = 0;
    private BufferedWriter csvWriter;

    public MessageLoggingWorkerThread(AuditLogPduDaoInterface<AuditLogPDU> model) {
        this.model = model;
        setDaemon(true);
    }

    /**
     * Waits for new {@link AuditLogPDU} messages from the {@link AuditLogPduDaoInterface#getAllNew()}
     * and then logs them to a csv log file.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final List<AuditLogPDU> allNew = model.getAllNew();
                messageCounter += allNew.size();
                log.debug("Anzahl neuer Nachrichten: " + messageCounter);
                for (AuditLogPDU auditLogPDU : allNew) {
                    log.debug(auditLogPDU);
                    getCsvWriter()
                        .append(auditLogPDU.getPduType().toString())
                        .append(';')
                        .append(auditLogPDU.getClientThreadName())
                        .append(';')
                        .append(auditLogPDU.getServerThreadName())
                        .append(';')
                        .append(auditLogPDU.getUserName())
                        .append(';')
                        .append(new Date(auditLogPDU.getAuditTime()).toString())
                        .append(';');
                    getCsvWriter()
                        .newLine();
                }
                getCsvWriter().flush();
                Thread.sleep(1000);
            } catch (InterruptedException | IOException e) {
                ExceptionHandler.logException(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * If {@link MessageLoggingWorkerThread#csvWriter} is @null, a new {@link BufferedWriter} object is created and
     * returned. As a result a new CSV file will be created as well.
     *
     * @return new or existing {@link MessageLoggingWorkerThread#csvWriter}
     * @throws IOException If an I/O error occurs
     */
    private BufferedWriter getCsvWriter() throws IOException {
        if (csvWriter == null) {
            final File logDir = new File(System.getProperty("user.dir") + File.separator + "logs");
            if (!logDir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                logDir.mkdirs();
            }

            //noinspection SpellCheckingInspection
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssZ").withZone(ZoneId.of("UTC"));
            String logFileDate = formatter.format(Instant.now());

            File logFile = new File(logDir.getAbsolutePath() + File.separator + "audit_log_" + logFileDate + ".csv");

            csvWriter = new BufferedWriter(new FileWriter(logFile, true));

            createLogHeaderLine();
        }

        return csvWriter;
    }

    /**
     * @throws IOException If an I/O error occurs
     */
    private void createLogHeaderLine() throws IOException {
        csvWriter
            .append("PduType")
            .append(';')
            .append("ClientThreadName")
            .append(';')
            .append("ServerThreadName")
            .append(';')
            .append("UserName")
            .append(';')
            .append("AuditTime")
            .append(';');
        csvWriter.newLine();
    }
}
