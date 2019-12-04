package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;

import java.util.ArrayList;
import java.util.List;

public class AuditLogPduDao implements AuditLogPduDaoInterface<AuditLogPDU> {
    /**
     * List of {@link AuditLogPDU}.
     */
    private List<AuditLogPDU> messages;
    /**
     * Counter for recently added {@link AuditLogPDU} since {@link AuditLogPduDao#getAllNew()} got called.
     */
    private int newMessageCounter = 0;

    public AuditLogPduDao() {
        this(new ArrayList<>());
    }

    /**
     * If the default {@link ArrayList} does not meet the requirements, it can be replaced this way.
     *
     * @param list alternative list
     */
    public AuditLogPduDao(List<AuditLogPDU> list) {
        this.messages = list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AuditLogPDU> getAll() {
        return new ArrayList<>(messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AuditLogPDU> getAllNew() throws InterruptedException {
        while (!hasNew()) {
            wait();
        }
        final int fromIndex = messages.size() - newMessageCounter;
        newMessageCounter = 0;

        return new ArrayList<>(messages).subList(fromIndex, messages.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean hasNew() {
        return newMessageCounter > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getNewMessageCounter() {
        return newMessageCounter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void save(AuditLogPDU message) {
        messages.add(message);
        newMessageCounter++;
        notifyAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized AuditLogPDU get(long id) {
        return messages.get((int) id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void delete(AuditLogPDU message) {
        messages.remove(message);
    }
}
