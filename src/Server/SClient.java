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

public class SClient implements java.io.Serializable {

    int clientID;
    Socket socket;
    public String name = "NoName";
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    // dinleme threadi
    Listen listenThread;
    //PairingThread pairThread;
    //rakip client
    SClient rival;
    //eşleşme durumu
    public boolean paired = false;

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
                            //sclient.pairThread.start();
                            break;
                        case LIST:
                            ArrayList<String> usernames = new ArrayList<String>();
                            for (SClient item : Server.sclients) 
                                usernames.add(item.name);
                            Message mesaj = new Message(Message_Type.LIST);
                            mesaj.content = usernames;
                            Server.Send(mesaj);
                            break;
                        case START_CHAT:
                            Message chatStart = new Message(Message_Type.START_CHAT);
                            Server.Send(chatStart);
                            break;
                        case CHANGE:
                            sclient.rival.Send(msg);
                            System.out.println("Ben sclientim mesajı yolladımmmm");
                            break;
                        case GameControl:
                            Server.Send(sclient.rival, msg);
                            break;
                        case FINISH:
                            Server.Send(sclient.rival, msg);
                            break;
                    }
                } catch (IOException ex) {
                    System.out.println("Listen Thread Exceptionnn");
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class Not Foundddd");
                } catch (IllegalThreadStateException te) {
                    System.out.println("Illegal Threaddd");
                }
            }
        }

    }
}
