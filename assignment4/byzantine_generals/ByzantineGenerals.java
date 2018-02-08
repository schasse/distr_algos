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

    // the node's id
    int id;
    // True determines the node to be a commander. There is only one
    // commander, which has id 0.
    Boolean commander;
    // True determines the node to be a traitor. A node will be a
    // traitor when the traitors array includes its id.
    Boolean traitor;
    // When there are no more messages to send, the algorithm
    // terminates, see receive method.
    Boolean terminated;
    // This object holds all the received commands that are either
    // 'attack' or 'retreat' commands. The path or chain of the
    // receivers determines the location in the tree.
    MessageTree receivedCommands;

    // Simply counts all sent messages.
    static int messageCounter = 0;

    // Configures the initial command which the commander sends.
    public static String initialCommand = "attack";
    // Configures the nodes, which are traitors.
    public static int[] traitors = {0, 5};

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
    // Only the commander executes the 'initiate' method that sends a
    // command to each general.
        {
            if (commander == true) {
                // receivers = {1, 2, ..., n}
                HashSet<Integer> receivers = new HashSet<Integer>();
                for (int i = 1; i < checkInterfaces(); i++) {
                    receivers.add(i);
                }
                sendTo(traitors.length, receivers, id + "", initialCommand);
            }
            updateView();
        }

    public void sendTo(int traitorCount, HashSet<Integer> receivers, String receiverChain, String command)
    // Sends commands to all the receivers.
    {
        Iterator receiverIterator = receivers.iterator();
        while (receiverIterator.hasNext()) {
            int receiver = (int) receiverIterator.next();
            Message message = new Message(traitorCount, receivers, receiverChain, maybeHonest(command), traitor);
            send(receiver, message);
        }
    }

    public String maybeHonest(String command)
    // Returns the given command if the node is no traitor, otherwise
    // a random command.
    {
        if (traitor && commander) {
            if (Math.random() <= 0.5) {
                return "attack";
            } else {
                return "retreat";
            }
        }
        if (traitor && !commander) {
            if (command == "attack") {
                return "retreat";
            }
            if (command == "retreat") {
                return "attack";
            }
        }
        //if (!traitor)
        return command;
    }

    public void receive(int interf, Object messageObject)
    // Receives a command, appends it to the message tree and
    // distributes the command to the remaining receivers.
        {
            Message message = (Message) messageObject;
            receivedCommands.add(message.receiverChain(), message.command());

            if (message.traitorCount != 0) {
                HashSet<Integer> remainingReceivers = new HashSet<Integer>();
                remainingReceivers.addAll(message.receivers());
                remainingReceivers.remove(id);
                sendTo(message.traitorCount() - 1, remainingReceivers, id + ":" + message.receiverChain, message.command);
            } else {
                terminated = true;
                System.out.println("\n#" + id);
                receivedCommands.print(null);
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
        // The algorithm decreases traitorCount in each recursion step.
        int traitorCount;
        // The receiver of the message has to send the message to the
        // receivers in this set.
        HashSet<Integer> receivers;
        // The history or path of previous receivers.
        String receiverChain;
        // Command is either attack or retreat.
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
        // Saves the historical path of previous senders. p.e. '6:2:0'
        private String receiverChain;
        // Saves the command either 'attack' or 'retreat'.
        private String command;
        // Saves the child trees.
        private List<MessageTree> children;

        public MessageTree(String rc, String c) {
            receiverChain = rc;
            command = c;
            children = new ArrayList<MessageTree>();
        }

        public void add(String rc, String c)
        // Adds a command in the tree in the correct location.
        {
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

        public String result()
        // Calculates the result for a tree, either 'attack' or 'retreat'.
        {
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

        public void print(String spaces)
        {
            if (spaces == null) { spaces = ""; }
            Iterator childrenIterator = children.iterator();
            // command == '' -> root
            String newspaces = spaces;
            if (command != "") {
                System.out.println(spaces + "+" + receiverChain + ":" + command);
                newspaces = newspaces + "|";
            }
            while (childrenIterator.hasNext()) {
                MessageTree child = (MessageTree) childrenIterator.next();
                child.print(newspaces);
            }
        }
    }
}
