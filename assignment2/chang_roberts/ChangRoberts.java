import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

public class ChangRoberts extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color;

    int id;
    int max = -1;
    String confirmedMaster;
    Boolean initiated = false;

    public void setup(java.util.Map<String, Object> config)
    {
        id = (int) config.get("node.id");
        updateView();
    }
    public void initiate()
    {
        max = id;
        initiated = true;
        send(0, max);
        updateView();
    }
    public void receive(int interf, Object message)
    {
        if (message instanceof Integer) {
            int receivedMax = (int) message;

            if (!initiated) {
                if (receivedMax < id){  // it ismportant to send highest ID
                    max = id;
                    initiated = true;
                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if(i!= interf){
                            send(i, max);
                        }
                    }
                } else { // redundant to send ID if no chance to win
                    max = id;
                    initiated = true;
                }
            }

            if (receivedMax > max) {

                // if a received max is higher than mine, send received
                max = receivedMax;
                for (int i = 0; i < checkInterfaces(); ++i) {
                    if(i!= interf){
                        send(i, max);
                    }
                }
            }

            // if the the message has made a full circle it won
            if (receivedMax == id) {
                // inform by a ring circuit
                for (int i = 0; i < checkInterfaces(); ++i) {
                    if(i != interf){
                        send(i, "" + max);
                    }
                }
            }

            if (message instanceof String) {
                confirmedMaster = (String) message;
                if (!confirmedMaster.equals("" + id)) {
                    // this node is not the elected master and pass on
                    // the confirmation

                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if(i != interf){
                            send(i, confirmedMaster);
                        }
                    }
                }
            }
            updateView();
        }
    }
    private void updateView()
    // this method updates the node's display depending on its state.
    // it is called after each action (setup, initiate, receive)
    {
        if (null == confirmedMaster) {
            color = Color.WHITE;
        }
        if (null != confirmedMaster) {
            color = Color.GREEN;
        }
        if (("" + id).equals(confirmedMaster)) {
            color = Color.BLUE;
        }
        caption = "#" + id + " max: " + max;
    }
}
