package edu.hm.dako.chat.adminTool;

public class TableItem {
    private String clientName;
    private String pduCounter;
    private String login;
    private String logout;
    private String time;

    public TableItem(String clientName, String pduCounter, String login, String logout, String time) {
        this.clientName = clientName;
        this.pduCounter = pduCounter;
        this.time = time;
        this.login = login;
        this.logout = logout;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPduCounter() {
        return pduCounter;
    }

    public void setPduCounter(String pduCounter) {
        this.pduCounter = pduCounter;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }
}
