import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Bully extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color;

    int id;
    int max = -1;
    String confirmedMaster;
    Boolean initiated = false;

    public static int nodeCount = 5;
    public static int random = (int) (Math.random() * nodeCount) + 1;

    public void setup(java.util.Map<String, Object> config)
        {
            int configId = (int) config.get("node.id");
            String scenario = (String) config.get("scenario");
            if (scenario.equals("worstcase")) {
                // reverse order
                id = nodeCount - configId - 1;
            }
            if (scenario.equals("bestcase")) {
                id = configId;
            }
            if (scenario.equals("averagecase")) {
                id = (random * configId + 1) % nodeCount;
                System.out.println("" + random + " " + configId + " " + id);
            }
            max = id;
            updateView();
        }
    public void initiate()
        {
            initiated = true;
            // send max to next node
            send(0, max);
            updateView();
        }
    public void receive(int interf, Object message)
        {
            if (message instanceof Integer) {
                int receivedMax = (int) message;
                if (receivedMax > max) {
                    max = receivedMax;
                    // send new max to next node
                    send(0, max);
                } else {
                    if (!initiated) { initiate(); }
                }
                if (receivedMax == id) {
                    // inform by a ring circuit
                    send(0, "" + max);
                }
            }
            if (message instanceof String) {
                confirmedMaster = (String) message;
                if (!confirmedMaster.equals("" + id)) {
                    // this node is not the elected master and passes
                    // on the confirmation
                    send(0, confirmedMaster);
                }
            }
            updateView();
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
