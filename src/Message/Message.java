package Message;

public class Message implements java.io.Serializable {

    public static enum Message_Type {
        NAME, ROOM_NAME, ROOM_LIST, LIST, JOIN_ROOM, START_CHAT, REFRESH, TEXT
    }

    // type of message
    public Message_Type type;

    // casteble message content 
    public Object content;
    
    public Message(Message_Type t) {
        this.type = t;
    }
}
