import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

public class HirschSinclair extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color = Color.WHITE;

    // node ID
    int id;

    // is node active or pasive?
    Boolean active;

    // id of a winner
    int winner;

    // node is ready to reinitiate data
    int keep_alive;

    // indicators that a node has beaten all other nodes
    int explorers_sent;

    // has the node participated in elections?
    Boolean initiated = false;

    public void setup(java.util.Map<String, Object> config)
        {
            id = (int) config.get("node.id");
            active = true;
            updateView();
        }
    public void initiate()
        {
            initiated = true;
            active = true;
            keep_alive = 1;
            explorers_sent = 2;
            for (int i = 0; i < checkInterfaces(); ++i) {
                send(i, id);
            }
            updateView();
        }
    public void receive(int interf, Object message)
        {
            if(!initiated){
                initiated = true;
                active = true;
                keep_alive = 1;
                updateView();
            }

            int receivedID = (int) message;
            if (receivedID == id){
                explorers_sent --;
                if (explorers_sent == 0){
                    if(keep_alive != 0){
                        for (int i = 0; i < checkInterfaces(); ++i) {
                            send(i, id);
                        }
                        explorers_sent = 2;
                        keep_alive = 0;
                    } else{
                        winner = id;
                        color = Color.RED;
                    }
                }
            } else if (receivedID == -1) {
                // go to passive state
                color = Color.BLUE;
                active = false;

            } else {
                if (active){
                    // if received ID is bigger, inform the node that he won, and go to passive state
                    if (receivedID > id){
                        active = false;
                        color = Color.BLUE;
                        send(interf, receivedID);
                        winner = receivedID;
                    } else {
                        // if the received ID is lower, inform the node that he lost
                        active = true;
                        keep_alive = 1;
                        if (explorers_sent == 0){
                            for (int i = 0; i < checkInterfaces(); ++i) {
                                if(i != interf)
                                    send(i, id);
                            }
                            explorers_sent = 1;
                        }

                        send(interf,-1);
                    }
                } else {  // pure data forwarding
                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if (i != interf)
                            send(i, receivedID);
                    }
                    winner = receivedID;
                }
            }
            updateView();
        }
    private void updateView(){
        // this method updates the node's display depending on its state.
        // it is called after each action (setup, initiate, receive)
        caption = "#: " + id +"winner: "+ winner;
    }
}
