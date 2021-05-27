package Message;

public class Message implements java.io.Serializable {
    //mesaj tipleri enum 

    public static enum Message_Type {
        Name, ROOM_NAME, ROOM_LIST, LIST, RivalConnected, CHANGE, START_CHAT, GameControl, FINISH
    }

    //mesajın tipi
    public Message_Type type;

    //mesajın içeriği obje tipinde ki istenilen tip içerik yüklenebilsin
    public Object content;
    
    public Message(Message_Type t) {
        this.type = t;
    }
}
