package states;

public class BankaiState implements HeroState{
    public double modifyDamage(int baseDamage) { return baseDamage * 2; }
    public double modifyReiatsuCost(int baseCost) { return baseCost * 1.5; }
    public String getName() { return "BANKAI"; }
}
