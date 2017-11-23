import java.awt.Color;
import teachnet.view.renderer.Shape;

public class ExplorerMsg {
    Color color = Color.RED;
    Shape shape = Shape.CIRCLE;
    static int counter = 0;
    int id;

    public ExplorerMsg()
        {
            this.counter++;
            id = counter;
        }
    public String toString()
        {
            return "explorer";
        }
}
