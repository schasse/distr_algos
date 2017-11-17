import java.awt.Color;
import teachnet.view.renderer.Shape;

public class Rumor {
    Color color = Color.RED;
    Shape shape = Shape.CIRCLE;
    static int counter = 0;
    int id;

    public Rumor() {
        this.counter++;
        id = counter;
    }
}
