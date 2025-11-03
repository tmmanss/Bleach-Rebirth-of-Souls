package heroes;

import strategy.AttackStrategy;

public class Hero {
    private final String name;
    private final HeroType type;
    private int reiatsu;
    private AttackStrategy attackStrategy;

    public Hero(String name, HeroType type, int reiatsu, AttackStrategy attackStrategy){
        this.name = name;
        this.type = type;
        this.reiatsu = reiatsu;
        this.attackStrategy = attackStrategy;
    }

    public String getName(){ return name; }
    public HeroType type(){return type;}
    public int getReiatsu(){ return reiatsu; }
    public AttackStrategy getAttackStrategy() { return attackStrategy; }

    public void setAttackStrategy(AttackStrategy attackStrategy){
        this.attackStrategy = attackStrategy;
    }

    public void attack(Hero target, int spendReiatsu){
        reiatsu -= spendReiatsu;
        attackStrategy.attack(this, target);
    }

    public void takeDamage(int damage){
        reiatsu-= damage;
        System.out.println(name + " получил " + damage + " урона. Осталось реацу: " + reiatsu);
    }

    @Override
    public String toString(){
        return name + " [" + type + "] | State: " +   " | Reiatsu: " + reiatsu;
    }

}
