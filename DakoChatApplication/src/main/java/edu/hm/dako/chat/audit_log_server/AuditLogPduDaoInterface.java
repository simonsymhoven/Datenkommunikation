package edu.hm.dako.chat.audit_log_server;

import java.util.List;

public interface AuditLogPduDaoInterface<T> {
    /**
     * @return list of all messages
     */
    List<T> getAll();

    /**
     * A thread calling this method waits till {@link AuditLogPduDaoInterface#hasNew()} returns {@code true}.
     * Then, the counter for new messages is reset and a list of all recently added messages is returned.
     *
     * @return list of new messages
     * @throws InterruptedException if any thread interrupted the current thread before or
     *                              while the current thread was waiting. The <em>interrupted status</em> of the
     *                              current thread is cleared when this exception is thrown.
     */
    List<T> getAllNew() throws InterruptedException;

    /**
     * @return {@code true} if the counter for new messages is greater 0
     */
    boolean hasNew();

    /**
     * @return counter of new messages
     */
    int getNewMessageCounter();

    /**
     * Adds the message to list and notify all in {@link AuditLogPduDaoInterface#getAllNew()} waiting threads.
     *
     * @param message to be appended to this list
     */
    void save(T message);

    /**
     * @param id if the message to return
     * @return the message at the specified position in this list
     */
    T get(long id);

    /**
     * @param message to be removed from this list, if present
     */
    void delete(T message);
}
