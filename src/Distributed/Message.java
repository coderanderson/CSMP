package Distributed;

import java.io.Serializable;

public class Message implements Serializable {
    static final long serialVersionUID=1L;
    private int index;
    private String type;

    public Message(int index) {
        this.index = index;
    }

    public Message(int index, String type) {
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public String getType(){
        return type;
    }
}