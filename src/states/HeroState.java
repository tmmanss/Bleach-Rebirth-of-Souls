package states;

public interface HeroState {
    double modifyDamage(int baseDamage);
    double modifyReiatsuCost(int baseCost);
    String getName();
}
