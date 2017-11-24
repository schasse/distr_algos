import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

public class Broadcast extends BasicAlgorithm
{
    // id, caption and color appear in the view
    int id;
    String caption;
    Color color = Color.WHITE;

    // turns true if the node has received all confirmation and the
    // color of the node turns green
    Boolean confirmed = false;

    public void setup(java.util.Map<String, Object> config)
        {
            id = (Integer) config.get("node.id");
            updateView();
        }
    public void initiate()
        {
            confirmed = true;
            // message to next neighbor
            send(0, "msg");
            updateView();
        }
    public void receive(int interf, Object msgObject)
        {
            if (! confirmed) {
                confirmed = true;
                send(0, "msg");
            }
            updateView();
        }
    private void updateView()
    // this method updates the node's display depending on its state
    // it is called after each action (setup, initiate, receive)
         {
            if (confirmed) {
                color = Color.GREEN;
            }
            caption = ""+id;
        }
}
