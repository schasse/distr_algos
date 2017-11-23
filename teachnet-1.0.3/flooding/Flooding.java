import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Flooding extends BasicAlgorithm
{
    int markInterface = -1;

    // id, caption and color appear in the view
    int id;
    String caption;
    Color color = Color.WHITE;

    // true, when node is the initiator otherwise stays false
    Boolean initial = false;
    // turns true if node is informed with an explorer and color of
    // the node becomes red
    Boolean informed = false;
    // turns true if the node has received all confirmation and the
    // color of the node turns green
    Boolean confirmed = false;
    // count of confirmations
    int count = 0;
    // the interface, from which the explorer came from
    int explorerOrigin;

    public void setup(java.util.Map<String, Object> config)
        {
            id = (Integer) config.get("node.id");
            updateView();
        }
    public void initiate()
        {
            initial = true;
            // send explorer to all neighbors
            for (int i = 0; i < checkInterfaces(); ++i) {
                send(i, new Explorer());
            }
            informed = true;
            updateView();
        }
    public void receive(int interf, Object message)
        {
            if (message instanceof Explorer) {
                // explorer from neighbor explorerOrigin is received
                Explorer explorerMsg = (Explorer) message;
                // send explorer to all neighbors except explorerOrigin
                if (! informed && ! leaf()) {
                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if (i != interf) {
                            send(i, explorerMsg);
                        }
                    }
                    informed = true;
                    explorerOrigin = interf;
                } else {
                    send(interf, new Confirmation());
                    informed = true;
                    confirmed = true;
                }
            } else if (message instanceof Confirmation) {
                // confirmation is received
                Confirmation confirmationMsg = (Confirmation) message;
                count = count + 1;
                if (! initial && count >= checkInterfaces() - 1) {
                    send(explorerOrigin, confirmationMsg);
                    confirmed = true;
                }
                if (initial && count == checkInterfaces()) {
                    confirmed = true;
                }
            }
            updateView();
        }
    private Boolean leaf()
    // returns true if this node is a leaf
        {
            return checkInterfaces() <= 1;
        }
    private void updateView()
    // this method updates the node's display depending on its state
        {
            if (informed) {
                color = Color.RED;
            }
            if (confirmed) {
                color = Color.GREEN;
            }
            caption = id + ": " + count;
        }
}
