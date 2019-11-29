package edu.hm.dako.chat.AuditLogServerNew;

import edu.hm.dako.chat.common.AuditLogPDU;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuditLogGuiModel extends AbstractAuditLogModel {
    /**
     * List of {@link AuditLogPDU} messages.
     */
    ObservableList<AuditLogPDU> messages = FXCollections.observableArrayList();
}
