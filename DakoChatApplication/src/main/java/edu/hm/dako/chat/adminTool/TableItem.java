package edu.hm.dako.chat.adminTool;

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getPduMessageCounter() {
        return pduMessageCounter;
    }

    public void setPduMessageCounter(int pduMessageCounter) {
        this.pduMessageCounter = pduMessageCounter;
    }

    public int getPduLoginCounter() {
        return pduLoginCounter;
    }

    public void setPduLoginCounter(int pduLoginCounter) {
        this.pduLoginCounter = pduLoginCounter;
    }

    public int getPduLogoutCounter() {
        return pduLogoutCounter;
    }

    public void setPduLogoutCounter(int pduLogoutCounter) {
        this.pduLogoutCounter = pduLogoutCounter;
    }

    public int getPduUndefineCounter() {
        return pduUndefineCounter;
    }

    public void setPduUndefineCounter(int pduUndefineCounter) {
        this.pduUndefineCounter = pduUndefineCounter;
    }

    public int getPduFinishCounter() {
        return pduFinishCounter;
    }

    public void setPduFinishCounter(int pduFinishCounter) {
        this.pduFinishCounter = pduFinishCounter;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

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
}
