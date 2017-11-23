import java.awt.Color;
import teachnet.view.renderer.Shape;

public class Explorer {
    Color color = Color.RED;
    Shape shape = Shape.CIRCLE;
    static int counter = 0;
    int id;

    public Explorer()
        {
            this.counter++;
            id = counter;
        }
    public String toString()
        {
            return "explorer";
        }
}
