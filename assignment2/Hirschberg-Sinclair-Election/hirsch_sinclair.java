import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

public class hirsch_sinclair extends BasicAlgorithm
{
    // caption and color appear in the view
    String caption;
    Color color = Color.WHITE;

    int id;
    int max = -1;
    int counter;
    // active participients or passive
    Boolean active;
    String confirmedMaster;
    Boolean initiated = false;

    public void setup(java.util.Map<String, Object> config)
        {
            id = (int) config.get("node.id");
            active = true;
            updateView();
        }
    public void initiate()
        {
            max = id;
            initiated = true;
            counter = 0;
            active = true;

            // send max to next node
            for (int i = 0; i < checkInterfaces(); ++i) {
                send(i, id);
            }
            updateView();
        }
    public void receive(int interf, Object message)
        {	
        	if(!initiated){
            	max = id;
            	initiated = true;
            	counter = 0;
            	updateView();
            	active = true;
       		}

        	int receivedID = (int) message;
        	if (receivedID == id){
        		if ((counter++)%2 == 0){
				    for (int i = 0; i < checkInterfaces(); ++i) {
                	send(i, id);
            	}	
        		}
        	} else if (receivedID == -1) {
        		// go to passive state
        		active = false;
        	} else {
	        	if (active){
	        		// if received ID is bigger, inform the node that he won, and go to passive state
	        		if (receivedID > id){
	        			active = false;
	        			send(interf, receivedID);
	        		} else {
	        		// if the received ID is lower, inform the node that he lost
	        			active = true;
	        			send(interf,-1);
	        			for (int i = 0; i < checkInterfaces(); ++i) {
	        				if(i != interf)
                				send(i, id);
            			}
	        		}	
	        	} else {  // pure data forwarding
	        		for (int i = 0; i < checkInterfaces(); ++i) {
	        			if (i != interf)
	                		send(i, receivedID);
	            	}
	        	}
        	}
            updateView();
        }
    private void updateView()
    // this method updates the node's display depending on its state.
    // it is called after each action (setup, initiate, receive)
         {
            if (null != confirmedMaster) {
                color = Color.GREEN;
            }
            if (("" + id).equals(confirmedMaster)) {
                color = Color.BLUE;
            }
            caption = "#" + id + " max: " + max;
        }
}
