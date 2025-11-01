package heroes;

public class UniqueTechnique {
    private final String name;
    private final int damage;

    public UniqueTechnique(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName(){
        return name;
    }

    public int getDamage(){
        return damage;
    }

    @Override
    public String toString(){
        return name + " (" + damage +" урон)";
    }

}
