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
            int configId = (int) config.get("node.id");
            int nodeCount = (int) config.get("nodecount");
            String scenario = (String) config.get("scenario");
            if (scenario.equals("bestcase")) {
                id = configId;
            }
            if (scenario.equals("worstcase")) {
                // ids in reverse order
                id = nodeCount - configId - 1;
            }
            if (scenario.equals("averagecase")) {
                // LOL, i roled the dice and figured out this random number
                int random = (int) (0.34000319169778204 * nodeCount) + 1;
                id = ((random * configId) + 1) % nodeCount;
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
                if (receivedMax < max) {
                    // we determined the initiator cannot win. since
                    // only initiators can win, let's make this last
                    // node (with a larger id) the new initiator.
                    if (!initiated) { initiate(); }
                }
                if (receivedMax > max) {
                    max = receivedMax;
                    // send new max to next node
                    send(0, max);
                }
                if (receivedMax == id) {
                    // this node wins and informs by a ring circuit
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
            color = Color.WHITE;
            if (initiated) {
                color = Color.GRAY;
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
