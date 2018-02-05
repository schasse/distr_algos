import teachnet.algorithm.BasicAlgorithm;
import teachnet.view.renderer.Shape;
import java.awt.Color;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ByzantineGenerals extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color;

    int id;
    Boolean commander;
    Boolean traitor;
    Boolean terminated;
    MessageTree receivedCommands;

    static int messageCounter = 0;

    public static String initialCommand = "retreat";
    public static int[] traitors = {1};

    public void setup(java.util.Map<String, Object> config)
        {
            id = (int) config.get("node.id");
            commander = (id == 0);
            receivedCommands = new MessageTree("", "");
            terminated = false;

            traitor = false;
            for (int i = 0; i < traitors.length; ++i) {
                if (traitors[i] == id) {
                    traitor = true;
                }
            }

            updateView();
        }
    public void initiate()
        {
            if (commander == true) {
                HashSet<Integer> receivers = new HashSet<Integer>();
                for (int i = 1; i < checkInterfaces(); i++) {
                    receivers.add(i);
                }
                start(traitors.length, receivers, id + "", maybeHonest(initialCommand));
            }
            updateView();
        }
    public void start(int traitorCount, HashSet<Integer> receivers, String receiverChain, String command)
    {
        Iterator receiverIterator = receivers.iterator();
        while (receiverIterator.hasNext()) {
            int receiver = (int) receiverIterator.next();
            Message message = new Message(traitorCount, receivers, receiverChain, maybeHonest(command), traitor);
            send(receiver, message);
        }
    }
    public String maybeHonest(String command)
    {
        if (traitor) {
            if (Math.random() <= 0.5) {
                return "attack";
            } else {
                return "retreat";
            }
        } else {
            return command;
        }
    }
    public void receive(int interf, Object messageObject)
        {
            Message message = (Message) messageObject;
            receivedCommands.add(message.receiverChain(), message.command());

            if (message.traitorCount != 0) {
                HashSet<Integer> remainingReceivers = new HashSet<Integer>();
                remainingReceivers.addAll(message.receivers());
                remainingReceivers.remove(id);
                start(message.traitorCount() - 1, remainingReceivers, id + ":" + message.receiverChain, message.command);
            } else {
                terminated = true;
            }
            updateView();
        }
    private void updateView()
    // this method updates the node's display depending on its state.
    // it is called after each action (setup, initiate, receive)
         {
            if (commander == true && traitor == false) {
                color = Color.BLUE;
            }
            if (commander == true && traitor == true) {
                color = Color.DARK_GRAY;
            }
            if (commander == false && traitor == false) {
                color = Color.WHITE;
            }
            if (commander == false && traitor == true) {
                color = Color.LIGHT_GRAY;
            }
            if (commander == true) {
                caption = "#" + id + " " + initialCommand;
            }
            if (commander == false && terminated == false) {
                caption = "#" + id;
            }
            if (commander == false && terminated == true) {
                caption = "#" + id + " " + receivedCommands.result();
            }
        }
    private class Message {
        int traitorCount;
        HashSet<Integer> receivers;
        String receiverChain;
        String command;

        Color color;
        Shape shape = Shape.RHOMBUS;
        int id;

        public Message(int tc, HashSet<Integer> rs, String rc, String c, Boolean traitor)
        {
            traitorCount = tc;
            receivers = rs;
            receiverChain = rc;
            command = c;

            ByzantineGenerals.messageCounter++;
            id = ByzantineGenerals.messageCounter;
            if (traitor) {
                color = Color.GRAY;
            } else {
                color = Color.WHITE;
            }

        }
        public int traitorCount() { return traitorCount; }
        public HashSet<Integer> receivers() { return receivers; }
        public String receiverChain() { return receiverChain; }
        public String command() { return command; }
        public String toString() { return "#" + id + " " + receiverChain + ":" + command; }
    }
    private class MessageTree {
        private String receiverChain;
        private String command;
        private List<MessageTree> children;

        public MessageTree(String rc, String c) {
            receiverChain = rc;
            command = c;
            children = new ArrayList<MessageTree>();
        }
        public void add(String rc, String c) {
            String postfix = rc.substring(1, rc.length());
            Iterator childrenIterator = children.iterator();
            while (childrenIterator.hasNext()) {
                MessageTree child = (MessageTree) childrenIterator.next();
                if (postfix.endsWith(":" + child.receiverChain)) {
                    child.add(rc, c);
                    return;
                }
            }
            children.add(new MessageTree(rc, c));
        }
        public String result() {
            Iterator childrenIterator = children.iterator();
            int attackCount = 0;
            int retreatCount = 0;
            while (childrenIterator.hasNext()) {
                MessageTree child = (MessageTree) childrenIterator.next();
                if (child.result().equals("attack")) { attackCount++; }
                if (child.result().equals("retreat")) { retreatCount++; }
            }
            if (command == "attack") { attackCount++; }
            if (command == "retreat") { retreatCount++; }
            if (attackCount < retreatCount) {
                return "retreat";
            } else {
                return "attack";
            }
        }
    }
}
