package edu.hm.dako.chat.adminTool;

public class TableItem {
    private String clientName;
    private Integer pduCounter;

    public TableItem(String clientName, Integer pduCounter) {
        this.clientName = clientName;
        this.pduCounter = pduCounter;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getPduCounter() {
        return pduCounter;
    }

    public void setPduCounter(Integer pduCounter) {
        this.pduCounter = pduCounter;
    }
}
