import java.awt.Color;
import teachnet.view.renderer.Shape;
import java.util.Set;


public class ExplorerMsg {
    Color color = Color.RED;
    Shape shape = Shape.CIRCLE;
    static int counter = 0;
    int id;
    // array of nodes which are already informed
    Set<Integer> taboo;

    public ExplorerMsg(Set<Integer> givenTaboo)
        {
            this.counter++;
            id = counter;
            taboo = givenTaboo;
        }
    public Set<Integer> getTaboo()
        {
            return taboo;
        }
    public String toString()
        {
            return "explorer " + taboo;
        }
}
