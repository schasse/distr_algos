import teachnet.algorithm.BasicAlgorithm;

import java.awt.Color;

public class Flooding extends BasicAlgorithm
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
                send(i, new Rumor());
            }
            updateView();
        }
    public void receive(int interf, Object message)
        {
            if (message instanceof Rumor) {
                Rumor rumorMsg = (Rumor) message;
                if (! informed && ! initial) {
                    for (int i = 0; i < checkInterfaces(); ++i) {
                        if (i != interf) {
                            send(i, rumorMsg);
                        }
                    }
                    informed = true;
                    rumorOrigin = interf;
                } else {
                    send(interf, new Confirmation());
                }
            } else if (message instanceof Confirmation) {
                Confirmation confirmationMsg = (Confirmation) message;
                count = count + 1;
                if (! initial && count >= checkInterfaces() - 1) {
                    send(rumorOrigin, confirmationMsg);
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
