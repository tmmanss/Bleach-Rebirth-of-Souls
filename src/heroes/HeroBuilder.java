package heroes;

import strategy.AttackStrategy;

public class HeroBuilder {
    private String name;
    private HeroType type;
    private int reiatsu;
    private AttackStrategy strategy;

    public HeroBuilder setName(String name){
        this.name = name;
        return this;
    }

    public HeroBuilder setType(HeroType type){
        this.type = type;
        return this;
    }

    public HeroBuilder setReiatsu(int reiatsu){
        this.reiatsu = reiatsu;
        return this;
    }

    public HeroBuilder setStrategy(AttackStrategy strategy){
        this.strategy = strategy;
        return this;
    }

    public Hero build(){
        return new Hero(name, type, reiatsu, strategy);
    }
}
