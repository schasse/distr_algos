import java.awt.Color;
import teachnet.view.renderer.Shape;

public class Confirmation {
    Color color = Color.GREEN;
    Shape shape = Shape.RHOMBUS;
    static int counter = 0;
    int id;

    public Confirmation() {
        this.counter++;
        id = counter;
    }
}
