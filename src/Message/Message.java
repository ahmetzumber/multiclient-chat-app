package Message;

public class Message implements java.io.Serializable {

    public static enum Message_Type {
        NAME, ROOM_NAME, ROOM_LIST, LIST,                                       // MENU MESSAGE TYPES
        JOIN_ROOM, REFRESH, TEXT,  ROOM_FILE, ROOM_FILE_NOTIFY,                 // ROOM MESSAGE TYPES
        START_CHAT, DECIDE, DECIDE_FINISH, P2P_TEXT, P2P_FILE, P2P_FILE_NOTIFY, // P2P MESSAGE TYPES 
    }

    // type of message
    public Message_Type type;

    // message sender 
    public String senderName;
    
    // who wants to talk
    public String whoWantsToTalk;
    
    // to send a file content as a byte array
    public byte[] fileContent;
    
    // casteble message content 
    public Object content;
    
    public Message(Message_Type t) {
        this.type = t;
    }
}
