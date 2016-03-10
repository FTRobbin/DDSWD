package BP;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class RandomVariable {

    public String name;

    public int type;

    public boolean negated;

    public RandomVariable(String name, int type, boolean negated) {
        this.name = name;
        this.type = type;
        this.negated = negated;
    }
}
