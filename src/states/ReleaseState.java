package states;

public class ReleaseState implements HeroState{
    public double modifyDamage(int baseDamage) { return baseDamage * 1.3; }
    public double modifyReiatsuCost(int baseCost) { return baseCost * 1.2; }
    public String getName() { return "Высвобождение меча"; }
}
