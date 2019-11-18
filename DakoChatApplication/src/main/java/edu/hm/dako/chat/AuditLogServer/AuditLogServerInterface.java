package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.AuditLogPduType;
import javafx.application.Platform;

public interface AuditLogServerInterface {
    /**
     * Uebergabe einer Fehlermeldung
     *
     * @param sender
     *          Absender der Fehlermeldung
     * @param errorMessage
     *          Fehlernachricht
     * @param errorCode
     *          Error Code
     */

    public void setErrorMessage(String sender, String errorMessage, long errorCode);


    /**
     * Uebergabe einer Nachricht zur Ausgabe in der Messagezeile
     *
     * @param user
     *          Absender der Nachricht
     * @param type
     *          Typ der Nachricht
     * @param message
     *          Nachrichtentext
     */
    public void setMessageLine(String user, AuditLogPduType type, String message);
}
