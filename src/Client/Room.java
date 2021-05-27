package Client;

import java.util.ArrayList;

public class Room {
    
    public String name;
    public ArrayList<String> userNamesList = new ArrayList();
    
    public Room(String name, String creatorName){
        this.name = name;
        userNamesList.add(creatorName);
    }
    
    
}
