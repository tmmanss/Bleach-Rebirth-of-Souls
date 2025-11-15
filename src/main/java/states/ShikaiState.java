package states;

public class ShikaiState implements HeroState{
    public double modifyDamage(int baseDamage) { return baseDamage * 1.5; }
    public double modifyReiatsuCost(int baseCost) { return baseCost * 1.25; }
    public String getName() { return "Shikai"; }
}
