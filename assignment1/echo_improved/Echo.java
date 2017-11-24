import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

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
    Set<Integer> neighbors = new HashSet<Integer>();

    public void setup(java.util.Map<String, Object> config)
        {
            id = (Integer) config.get("node.id");
            Integer networkSize = (Integer) config.get("network.size");

            if (config.get("topology").equals("ring")) {
                // here we calcuate the neighbors of a ring topology
                int left = (id + 1) % networkSize;
                int right = (id + networkSize - 1) % networkSize;
                neighbors.add(left);
                neighbors.add(right);
            }
            // System.out.println("" + id + ": [" + neighbors[0] + ", " + neighbors[1] + "]");
            updateView();
        }
    public void initiate()
        {
            initial = true;
            // send explorer to all neighbors including the taboo set
            Set<Integer> taboo = new HashSet<Integer>();
            taboo.add(id);
            taboo.addAll(neighbors);
            Iterator neighborIterator = neighbors.iterator();
            while (neighborIterator.hasNext()) {
                send((int)neighborIterator.next(), new ExplorerMsg(taboo));
            }
            informed = true;
            updateView();
        }
    public void receive(int interf, Object msgObject)
        {
            if (! informed) {
                // must be the first explorer

                // newTaboo = taboo u neighbors
                Set<Integer> taboo = ((ExplorerMsg) msgObject).getTaboo();
                Set<Integer> newTaboo = new HashSet<Integer>();
                newTaboo.addAll(taboo);
                newTaboo.addAll(neighbors);

                // send explorer to all neighbors except the once in
                // the taboo set
                Iterator neighborIterator = neighbors.iterator();
                while (neighborIterator.hasNext()) {
                    int neighbor = (int)neighborIterator.next();
                    if (! taboo.contains(neighbor)) {
                        send(neighbor, new ExplorerMsg(newTaboo));
                    }
                }
                informed = true;
                explorerOrigin = interf;
            }
            count = count + 1;
            if (count == neighbors.size()) {
                if (! initial) {
                    System.out.println(explorerOrigin);
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
