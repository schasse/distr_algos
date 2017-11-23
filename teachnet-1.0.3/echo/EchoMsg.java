import java.awt.Color;
import teachnet.view.renderer.Shape;

public class EchoMsg {
    Color color = Color.GREEN;
    Shape shape = Shape.RHOMBUS;
    static int counter = 0;
    int id;

    public EchoMsg()
        {
            this.counter++;
            id = counter;
        }
    public String toString()
        {
            return "confirmation";
        }
}
