package Server;

import Message.Message;
import Message.Message.Message_Type;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Client.Room;
import Views.Chat;
import Views.Login;

public class SClient implements java.io.Serializable {

    int clientID;
    Socket socket;
    public String name = "NoName";
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    // dinleme threadi
    Listen listenThread;

    public SClient(int clientID, Socket socket) {
        try {
            this.socket = socket;
            this.clientID = clientID;
            this.listenThread = new Listen(this);
            //this.pairThread = new PairingThread(this);
            this.sOutput = new ObjectOutputStream(this.socket.getOutputStream());
            this.sInput = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //client mesaj gönderme
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Disconnect(){
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public class Listen extends Thread implements java.io.Serializable {

        SClient sclient;

        public Listen(SClient sclient) {
            this.sclient = sclient;
        }
        

        @Override
        public void run() {
            while (sclient.socket.isConnected()) {
                try {
                    Message msg = (Message) sclient.sInput.readObject();
                    switch (msg.type) {
                        case Name:
                            System.out.println("gelen name mesajı: "+msg.content.toString());
                            sclient.name = msg.content.toString();
                            break;
                        case ROOM_NAME:
                            System.out.println("gelen room name mesajı: "+msg.content.toString());
                            Room newRoom = new Room(msg.content.toString(), sclient.name);
                            Server.rooms.add(newRoom);
                            newRoom.userNamesList.add(sclient.name);    // adding the creator to room's user list
                            Login.menu.chat = new Chat();
                            Login.menu.chat.roomlbl.setText(newRoom.name.toUpperCase());
                            Login.menu.chat.setVisible(true);
                            break;
                        case LIST:
                            ArrayList<String> usernames = new ArrayList<String>();
                            for (SClient item : Server.sclients) 
                                usernames.add(item.name);
                            Message mesaj = new Message(Message_Type.LIST);
                            mesaj.content = usernames;
                            Server.Send(this.sclient,mesaj);
                            break;
                        case ROOM_LIST:
                            ArrayList<String> roomNames = new ArrayList<String>();
                            for (Room item : Server.rooms) 
                                roomNames.add(item.name);
                            Message roomListMsg = new Message(Message_Type.ROOM_LIST);
                            roomListMsg.content = roomNames;
                            Server.Send(this.sclient,roomListMsg);
                            break;
                        case JOIN_ROOM:
                            // msg.content == choosen room name from list
                            // searching a room in server, so if it is exist then enter a room
                            // and add this sclient to rooms user list
                            for(Room item : Server.rooms){
                                if (item.name.equals(msg.content)){
                                    item.userNamesList.add(this.sclient.name);
                                    Login.menu.chat = new Chat();
                                    Login.menu.chat.roomlbl.setText(item.name.toUpperCase()); 
                                    Login.menu.chat.setVisible(true);
                                }
                            }       
                            break;
                        case START_CHAT:
                            Message chatStart = new Message(Message_Type.START_CHAT);
                            Server.Send(chatStart);
                            break;
                    }
                } catch (IOException ex) {
                    System.out.println("Listen Thread Exceptionnn: "+ex);
                    return;
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class Not Foundddd: "+ex);;
                } catch (IllegalThreadStateException te) {
                    System.out.println("Illegal Threaddd: "+te);
                }
            }
        }

    }
}
