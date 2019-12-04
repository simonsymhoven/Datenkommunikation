package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAuditLogModel implements AuditLogModelInterface {
    /**
     * List of {@link AuditLogPDU} messages.
     */
    private List<AuditLogPDU> messages = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuditLogPDU> getMessages() {
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessages(List<AuditLogPDU> messages) {
        this.messages = messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(AuditLogPDU message) {
        messages.add(message);
    }
}
