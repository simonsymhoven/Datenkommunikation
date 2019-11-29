package edu.hm.dako.chat.AuditLogServerNew;

import edu.hm.dako.chat.common.AuditLogPDU;

import java.util.List;

public interface AuditLogModelInterface {
    /**
     * Returns the messages list.
     *
     * @return List of messages.
     */
    List<AuditLogPDU> getMessages();

    /**
     * Replaces the messages list with a new one.
     *
     * @param messages new messages list
     */
    void setMessages(List<AuditLogPDU> messages);

    /**
     * Adds a message to the list.
     *
     * @param message message to add.
     */
    void addMessage(AuditLogPDU message);
}
