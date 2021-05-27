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
                    case Name:
                        break;
                    case LIST:
                        Login.menu.dlm.removeAllElements();
                        ArrayList<String> gelenIsımler = new ArrayList();
                        gelenIsımler = (ArrayList<String>) msg.content;
                        for (String item : gelenIsımler) 
                            Login.menu.dlm.addElement(item); 
                        break;
                    case ROOM_LIST:
                        Login.menu.dlm2.removeAllElements();
                        ArrayList<String> gelenRoomlar = new ArrayList();
                        gelenRoomlar = (ArrayList<String>) msg.content;
                        for (String item : gelenRoomlar) 
                            Login.menu.dlm2.addElement(item); 
                        break;     
                    case RivalConnected:
                        System.out.println("Rival Connected girdim....");
                        String rivalName = (String) msg.content;
                        Client.isPaired = true;
                        //Login.control.setText("Eslestirildi...........");
                        //Login.gs.player1.setText("You");
                        //Login.gs.player2.setText(rivalName);
                        //Login.gs.setVisible(true);
                        break;
                    case GameControl:
                        System.out.println("GameControl girdimm....");
                        //Login.gs.roundControl = (int) msg.content;
                        System.out.println((int) msg.content);
                        if ((int) msg.content == 1) {
                            //Login.gs.changeTurn(true);
                        } else if ((int) msg.content == 0) {
                            //Login.gs.changeTurn(false);
                        }
                        break;
                    case CHANGE:
                        System.out.println("change turnnnn girdim");
                        //ScoreMessage score = (ScoreMessage) msg.content;
                        //Login.gs.getRivalButtonByGivenType(score.score_type).setText(String.valueOf(score.content));
                        //Login.gs.roundControl = 1;
                        //Login.gs.changeTurn(true);
                        break;
                    case FINISH:
                        //Login.gs.finishState = true;
                        //Login.gs.totalPlayer2.setText((String) msg.content);
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
