import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;
import java.util.Random;
import java.util.HashMap;

public class FloodingAlgorithm extends BasicAlgorithm
{
    int markInterface = -1;

    int id;
    String caption;

    Color color = null;

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
            informed = true;
            for (int i = 0; i < checkInterfaces(); ++i) {
                send(i, "rumor");
            }
            updateView();
        }
    public void receive(int interf, Object msgObject)
        {
            String msg = (String) msgObject;
            if (msg == "rumor") {
                if (! informed && ! initial) {
                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if (i != interf) {
                            send(i, "rumor");
                        }
                    }
                    informed = true;
                    rumorOrigin = interf;
                } else {
                    send(interf, "confirmation");
                }
            } else if (msg == "confirmation") {
                count = count + 1;
                if (! initial && count >= checkInterfaces() - 1) {
                    send(rumorOrigin, "confirmation");
                    confirmed = true;
                }
                if (initial && count == checkInterfaces()) {
                    confirmed = true;
                }
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
