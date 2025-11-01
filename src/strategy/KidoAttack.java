package strategy;

import heroes.Hero;

public class KidoAttack implements AttackStrategy{
    @Override
    public void attack(Hero attacker, Hero target){
        int damage = 15;
        System.out.println(attacker.getName() + " атакует " + target.getName() +"(" + damage + " урон )");
    }

    @Override
    public String getName(){
        return "Атака с кидо";
    };
}
