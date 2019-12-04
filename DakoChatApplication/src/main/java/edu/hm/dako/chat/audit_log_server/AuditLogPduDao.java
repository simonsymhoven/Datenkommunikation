package edu.hm.dako.chat.audit_log_server;

import edu.hm.dako.chat.common.AuditLogPDU;

import java.util.ArrayList;
import java.util.List;

public class AuditLogPduDao implements AuditLogPduDaoInterface<AuditLogPDU> {
    /**
     * List of {@link AuditLogPDU}.
     */
    private List<AuditLogPDU> pduList;
    /**
     * Counter for recently added {@link AuditLogPDU} since {@link AuditLogPduDao#getAllNew()} got called.
     */
    private int newPduCounter = 0;

    public AuditLogPduDao() {
        this(new ArrayList<>());
    }

    /**
     * If the default {@link ArrayList} does not meet the requirements, it can be replaced this way.
     *
     * @param pduList alternative list
     */
    public AuditLogPduDao(List<AuditLogPDU> pduList) {
        this.pduList = pduList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AuditLogPDU> getAll() {
        return pduList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AuditLogPDU> getAllNew() {
        final int lastIndex = pduList.size() - 1;
        final List<AuditLogPDU> newPduList = pduList.subList(lastIndex - newPduCounter, lastIndex);
        newPduCounter = 0;

        return newPduList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean hasNew() {
        return newPduCounter > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void save(AuditLogPDU message) {
        pduList.add(message);
        newPduCounter++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized AuditLogPDU get(long id) {
        return pduList.get((int) id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void delete(AuditLogPDU message) {
        pduList.remove(message);
    }
}
