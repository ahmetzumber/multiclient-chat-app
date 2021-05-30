package Client;

import Message.Message;
import Server.SClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Views.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Client implements java.io.Serializable {

    public static Socket socket;
    public static ObjectInputStream sInput;
    public static ObjectOutputStream sOutput;
    public static boolean isPaired = false;
    public static ListenThread listen;

    public static void Start(String ip, int port) {
        try {
            // server ve socket baglantilari
            Client.socket = new Socket(ip, port);
            Client.listen = new ListenThread();
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listen.start();

            //Message msg = new Message(Message.Message_Type.Name);
            //msg.content = Login.nameTxt.getText();
            //Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listen.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();
                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Display(String message) {
        System.out.println(message);
    }

    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class ListenThread extends Thread implements java.io.Serializable {

    @Override
    public void run() {
        while (Client.socket.isConnected()) {
            try {
                Message msg = (Message) Client.sInput.readObject();
                switch (msg.type) {
                    case NAME:
                        break;
                    case ROOM_NAME:
                        // msg.content == newRoom.name
                        Login.menu.chat.roomlbl.setText(msg.content.toString().toUpperCase());
                        Login.menu.chat.setVisible(true);
                        break;
                    case LIST:
                        Login.menu.dlm.removeAllElements();
                        ArrayList<String> receivedNames = new ArrayList();
                        receivedNames = (ArrayList<String>) msg.content;
                        for (String item : receivedNames) {
                            Login.menu.dlm.addElement(item);
                        }
                        break;
                    case ROOM_LIST:
                        Login.menu.dlm2.removeAllElements();
                        ArrayList<String> receivedRooms = new ArrayList();
                        receivedRooms = (ArrayList<String>) msg.content;
                        for (String item : receivedRooms) {
                            Login.menu.dlm2.addElement(item);
                        }
                        break;
                    case JOIN_ROOM:
                        // msg.content == temp (NO or room name)
                        if (msg.content.equals("NO")) {
                            JOptionPane.showMessageDialog(Login.menu, "Room name Failure!!");
                            return;
                        }
                        Login.menu.chat.roomlbl.setText(msg.content.toString().toUpperCase());
                        Login.menu.chat.setVisible(true);
                        break;
                    case REFRESH:
                        Login.menu.chat.dlm.removeAllElements();
                        ArrayList<String> roomsClients = new ArrayList();
                        roomsClients = (ArrayList<String>) msg.content;
                        for (String item : roomsClients) {
                            Login.menu.chat.dlm.addElement(item);
                        }
                        break;
                    case TEXT:
                        Login.menu.chat.textArea.append(msg.content.toString()+"\n");
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
