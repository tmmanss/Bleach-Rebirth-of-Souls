package factory;

import heroes.BaseHero;
import heroes.HeroBuilder;
import strategy.Projectile;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HeroFactory {

    private static final Map<String, Map<String, Object>> heroData;

    static {
        Map<String, Map<String, Object>> temp = null;

        try (InputStream in = HeroFactory.class.getResourceAsStream("/data/heroes.json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
            temp = gson.fromJson(new InputStreamReader(in), type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        heroData = temp;
        if (heroData == null) {
            throw new RuntimeException("heroes.json could not be loaded");
        }
    }


    public static BaseHero createHero(String name, List<Projectile> sharedProjectiles) {

        if (!heroData.containsKey(name)) {
            throw new IllegalArgumentException("No such hero: " + name);
        }

        Map<String, Object> data = heroData.get(name);

        String basePath = (String) data.get("basePath");
        Number reiatsu = (Number) data.get("reiatsu");
        Number attackRange = (Number) data.get("attackRange");

        return new HeroBuilder()
                .setName(name)
                .setReiatsu(reiatsu.intValue())
                .setAttackRange(attackRange.doubleValue())
                .setBasePath(basePath)
                .setProjectiles(sharedProjectiles)
                .build();
    }

    public static Set<String> getAllHeroNames() {
        return heroData.keySet();
    }

}
