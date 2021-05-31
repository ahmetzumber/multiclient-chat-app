package Message;

public class Message implements java.io.Serializable {

    public static enum Message_Type {
        NAME, ROOM_NAME, ROOM_LIST, LIST, JOIN_ROOM, START_CHAT, DECIDE, DECIDE_FINISH, TEXT, REFRESH
    }

    // type of message
    public Message_Type type;

    // message sender 
    public String senderName;
    
    // who wants to talk
    public String whoWantsToTalk;
    
    // casteble message content 
    public Object content;
    
    public Message(Message_Type t) {
        this.type = t;
    }
}
