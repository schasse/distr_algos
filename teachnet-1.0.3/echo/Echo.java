import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Echo extends BasicAlgorithm
{
    int markInterface = -1;

    int id;
    String caption;

    Color color = Color.WHITE;

    Boolean initial = false;
    Boolean informed = false;
    Boolean confirmed = false;
    int count = 0;
    int rumorOrigin;

    public void setup(java.util.Map<String, Object> config)
        {
            id = (Integer) config.get("node.id");
            updateView();
        }
    public void initiate()
        {
            initial = true;
            for (int i = 0; i < checkInterfaces(); ++i) {
                send(i, "rumor");
            }
            informed = true;
            updateView();
        }
    public void receive(int interf, Object msgObject)
        {
            String msg = (String) msgObject;
            if (! informed) {
                for (int i = 0; i < checkInterfaces(); ++i) {
                    if (i != interf) {
                        send(i, "rumor");
                    }
                }
                informed = true;
                rumorOrigin = interf;
            }
            count = count + 1;
            if (count == checkInterfaces()) {
                if (! initial) {
                    send(rumorOrigin, "rumor");
                }
                confirmed = true;
            }
            updateView();
        }
    private void updateView()
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
