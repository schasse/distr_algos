import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Bully extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color = Color.WHITE;

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
            // send max to next node
            send(0, max);
            updateView();
        }
    public void receive(int interf, Object message)
        {
            if (!initiated) {
                initiate();
            }
            if (message instanceof Integer) {
                int receivedMax = (int) message;
                if (receivedMax > max) {
                    max = receivedMax;
                    // send new max to next node
                    send(0, max);
                }
                if (receivedMax == id) {
                    // inform by a ring circuit
                    send(0, "" + max);
                }
            }
            if (message instanceof String) {
                confirmedMaster = (String) message;
                if (!confirmedMaster.equals("" + id)) {
                    // this node is not the elected master and pass on
                    // the confirmation
                    send(0, confirmedMaster);
                }
            }
            updateView();
        }
    private void updateView()
    // this method updates the node's display depending on its state.
    // it is called after each action (setup, initiate, receive)
         {
            if (null != confirmedMaster) {
                color = Color.GREEN;
            }
            if (("" + id).equals(confirmedMaster)) {
                color = Color.BLUE;
            }
            caption = "#" + id + " max: " + max;
        }
}