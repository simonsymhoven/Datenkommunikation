package edu.hm.dako.chat.admin_tool;

/**
 * class to store all information about one client
 * At the same time TableItem is the internal data structure of used tableView
 */

public class TableItem {

    private String clientName;
    private int pduMessageCounter;
    private int pduLoginCounter;
    private int pduLogoutCounter;
    private int pduUndefineCounter;
    private int pduFinishCounter;
    private String loginTime;
    private String logoutTime;
    private String estimatedTime;

    /**
     *
     * @param clientName Client-Name
     * @param pduMessageCounter Counter for PDUs of type "Chat"
     * @param pduLoginCounter Counter for PDUs of type "Login"
     * @param pduLogoutCounter Counter for PDUs of type "Logout"
     * @param pduUndefineCounter Counter for PDUs of type "Undefined"
     * @param pduFinishCounter Counter for PDUs of type "Finish"
     * @param loginTime login time of client
     * @param logoutTime logout time of client
     * @param estimatedTime spent time of client
     */
    public TableItem(String clientName,
                     int pduMessageCounter,
                     int pduLoginCounter,
                     int pduLogoutCounter,
                     int pduUndefineCounter,
                     int pduFinishCounter,
                     String loginTime,
                     String logoutTime,
                     String estimatedTime) {
        this.clientName = clientName;
        this.pduMessageCounter = pduMessageCounter;
        this.pduLoginCounter = pduLoginCounter;
        this.pduLogoutCounter = pduLogoutCounter;
        this.pduUndefineCounter = pduUndefineCounter;
        this.pduFinishCounter = pduFinishCounter;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.estimatedTime = estimatedTime;
    }

    /**
     * returns the name of client
     * @return String
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * set the name of the client
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * returns the number of sent PDUs of type "Chat"
     * @return int
     */
    public int getPduMessageCounter() {
        return pduMessageCounter;
    }

    /**
     * set number of sent PDUs of tpe "Chat"
     * @param pduMessageCounter
     */
    public void setPduMessageCounter(int pduMessageCounter) {
        this.pduMessageCounter = pduMessageCounter;
    }

    /**
     * returns the number of sent PDUs of type "Login"
     * @return int
     */
    public int getPduLoginCounter() {
        return pduLoginCounter;
    }

    /**
     * set number of sent PDUs of tpe "Login"
     * @param pduLoginCounter
     */
    public void setPduLoginCounter(int pduLoginCounter) {
        this.pduLoginCounter = pduLoginCounter;
    }

    /**
     * returns the number of sent PDUs of type "Logout"
     * @return int
     */
    public int getPduLogoutCounter() {
        return pduLogoutCounter;
    }

    /**
     * set number of sent PDUs of tpe "Logout"
     * @param pduLogoutCounter
     */
    public void setPduLogoutCounter(int pduLogoutCounter) {
        this.pduLogoutCounter = pduLogoutCounter;
    }

    /**
     * returns the number of sent PDUs of type "Undefined"
     * @return int
     */
    public int getPduUndefineCounter() {
        return pduUndefineCounter;
    }

    /**
     * set number of sent PDUs of tpe "Undefined"
     * @param pduUndefineCounter
     */
    public void setPduUndefineCounter(int pduUndefineCounter) {
        this.pduUndefineCounter = pduUndefineCounter;
    }

    /**
     * returns the number of sent PDUs of type "Finish"
     * @return int
     */
    public int getPduFinishCounter() {
        return pduFinishCounter;
    }

    /**
     * set number of sent PDUs of tpe "Finish"
     * @param pduFinishCounter
     */
    public void setPduFinishCounter(int pduFinishCounter) {
        this.pduFinishCounter = pduFinishCounter;
    }

    /**
     * returns the login time of client
     * @return String
     */
    public String getLoginTime() {
        return loginTime;
    }

    /**
     * set login time of client
     * @param loginTime
     */
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * returns the logout time of client
     * @return String
     */
    public String getLogoutTime() {
        return logoutTime;
    }

    /**
     * set the logout Time of client
     * @param logoutTime
     */
    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    /**
     * returns the time, the client spent in chat
     * @return String
     */
    public String getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * set the spent time of client
     * @param estimatedTime
     */
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }


}
