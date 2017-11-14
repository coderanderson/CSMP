import java.io.Serializable;

public class Message implements Serializable {
    static final long serialVersionUID=1L;
    private int index;

    public Message(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
