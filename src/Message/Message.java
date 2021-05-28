package Message;

public class Message implements java.io.Serializable {
    //mesaj tipleri enum 

    public static enum Message_Type {
        Name, ROOM_NAME, ROOM_LIST, LIST, JOIN_ROOM, START_CHAT, REFRESH
    }

    // type of message
    public Message_Type type;

    // casteble message content 
    public Object content;
    
    public Message(Message_Type t) {
        this.type = t;
    }
}
