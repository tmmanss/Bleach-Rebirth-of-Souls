package states;

class NormalState implements HeroState {
    public double modifyDamage(int baseDamage) { return baseDamage; }
    public double modifyReiatsuCost(int baseCost) { return baseCost; }
    public String getName() { return "Normal"; }
}
