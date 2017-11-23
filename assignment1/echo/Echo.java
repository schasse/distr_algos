import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Echo extends BasicAlgorithm
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
    // count of received messages
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
                send(i, new ExplorerMsg());
            }
            informed = true;
            updateView();
        }
    public void receive(int interf, Object msgObject)
        {
            if (! informed) {
                // must be the first explorer
                // send explorer to all neighbors except explorerOrigin
                for (int i = 0; i < checkInterfaces(); ++i) {
                    if (i != interf) {
                        send(i, new ExplorerMsg());
                    }
                }
                informed = true;
                explorerOrigin = interf;
            }
            count = count + 1;
            if (count == checkInterfaces()) {
                if (! initial) {
                    send(explorerOrigin, new EchoMsg());
                }
                confirmed = true;
            }
            updateView();
        }
    private void updateView()
    // this method updates the node's display depending on its state
    // it is called after each action (setup, initiate, receive)
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
