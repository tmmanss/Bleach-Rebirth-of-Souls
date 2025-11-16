package heroes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import strategy.AttackStrategy;
import strategy.Projectile;
import factory.AttackFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeroLoader {

    public static BaseHero loadHero(String heroName, List<Projectile> sharedProjectiles) {
        try (InputStream in = HeroLoader.class.getResourceAsStream("/data/heroes.json")) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> heroesList = gson.fromJson(new InputStreamReader(in), listType);

            Map<String, Object> heroData = heroesList.stream()
                    .filter(h -> heroName.equals(h.get("name")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Hero not found: " + heroName));

            String basePath = (String) heroData.get("basePath");
            Number reiatsu = (Number) heroData.get("reiatsu");
            Number attackRange = (Number) heroData.get("attackRange");

            HeroBuilder builder = new HeroBuilder()
                    .setName(heroName)
                    .setReiatsu(reiatsu.intValue())
                    .setAttackRange(attackRange.doubleValue())
                    .setBasePath(basePath)
                    .setProjectiles(sharedProjectiles);

            List<AttackStrategy> melee = new ArrayList<>();
            for (String id : (List<String>) heroData.get("meleeAttacks")) {
                melee.add(AttackFactory.createAttack(id,sharedProjectiles));
            }

            List<AttackStrategy> ranged = new ArrayList<>();
            for (String id : (List<String>) heroData.get("rangedAttacks")) {
                ranged.add(AttackFactory.createAttack(id,sharedProjectiles));
            }

            builder.setMeleeAttacks(melee);
            builder.setRangedAttacks(ranged);

            return builder.build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load hero: " + heroName, e);
        }
    }

    public static Set<String> getAllHeroNames() {
        try (InputStream in = HeroLoader.class.getResourceAsStream("/data/heroes.json")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> heroesList = gson.fromJson(new InputStreamReader(in), listType);

            if (heroesList == null) {
                throw new RuntimeException("heroes.json could not be loaded or is empty");
            }

            Set<String> names = new LinkedHashSet<>();
            for (Map<String, Object> hero : heroesList) {
                String name = (String) hero.get("name");
                if (name != null) {
                    names.add(name);
                }
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load hero names", e);
        }
    }
}

