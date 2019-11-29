package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.AuditLogPduType;

public interface AuditLogServerInterface {
    /**
     * Uebergabe einer Fehlermeldung
     *
     * @param sender       Absender der Fehlermeldung
     * @param errorMessage Fehlernachricht
     * @param errorCode    Error Code
     */

    void setErrorMessage(String sender, String errorMessage, long errorCode);


    /**
     * Uebergabe einer Nachricht zur Ausgabe in der Messagezeile
     *
     * @param user    Absender der Nachricht
     * @param type    Typ der Nachricht
     * @param message Nachrichtentext
     */
    void setMessageLine(String user, AuditLogPduType type, String message, Long auditLogTime);


    /**
     * Schreibt Daten ins Log-File
     *
     * @param data Nachricht die ins Logfile geschrieben werden soll
     */
    void writeDataToLogFile(String data);
}
