package heroes.factory;
import heroes.BaseHero;
import heroes.HeroAnimation;
import heroes.HeroBuilder;
import heroes.HeroMovement;
import strategy.MeleeAttack;
import strategy.Projectile;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class HeroFactory {
    private static final Map<String,Map<String,Object>> heroData;
    static {
        Map<String,Map<String,Object>> temp =null;
        try(FileReader reader=new FileReader("heroes.json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            temp=gson.fromJson(reader,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        heroData = temp;
        if(heroData==null){
            throw new RuntimeException("json is null");
        }
    }


    public static BaseHero createHero(String name, List<Projectile> projectiles) {
       if (!heroData.containsKey(name))
           throw new IllegalArgumentException("no name"+ name);
        Map<String,Object> data=heroData.get(name);

        HeroAnimation animation=new HeroAnimation((String)data.get("spritePath"));
        HeroMovement movement=new HeroMovement();
        BaseHero hero=new BaseHero(animation,movement);
        hero.setName(name);
        hero.setReiatsu(((Double)data.get("reiatsu")).intValue());
        hero.setAttackRange((Double)data.get("attackRange"));
        hero.setMeleeStrategy(new MeleeAttack(animation.attackFrames));
        return hero;
    }
}
