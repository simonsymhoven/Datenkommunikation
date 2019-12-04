package edu.hm.dako.chat.audit_log_server;

import java.util.List;

public interface AuditLogPduDaoInterface<T> {
    /**
     * @return list of all elements
     */
    List<T> getAll();

    /**
     * Resets the new elements counter and returns a list of all recently added elements.
     *
     * @return list of elements
     */
    List<T> getAllNew();

    /**
     * @return {@code true} if new elements counter is greater 0
     */
    boolean hasNew();

    /**
     * @param message element to be appended to this list
     */
    void save(T message);

    /**
     * @param id if the element to return
     * @return the element at the specified position in this list
     */
    T get(long id);

    /**
     * @param message element to be removed from this list, if present
     */
    void delete(T message);
}
