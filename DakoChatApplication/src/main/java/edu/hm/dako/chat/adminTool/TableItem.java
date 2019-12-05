package edu.hm.dako.chat.adminTool;

public class TableItem {
    private String clientName;
    private Integer pduCounter;

    public TableItem(String clientName, Integer pduCounter) {
        this.clientName = clientName;
        this.pduCounter = pduCounter;
    }
}
