package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuditLogGuiModel extends AbstractAuditLogModel {
    /**
     * List of {@link AuditLogPDU} messages.
     */
    ObservableList<AuditLogPDU> messages = FXCollections.observableArrayList();
}
