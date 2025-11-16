package factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import heroes.BaseHero;
import heroes.HeroBuilder;
import strategy.AttackStrategy;
import strategy.Projectile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeroLoader {

    public static BaseHero loadHero(String heroName, Map<String, AttackStrategy> attacksMap, List<Projectile> sharedProjectiles) {
        try (InputStream in = HeroLoader.class.getResourceAsStream("/data/heroes.json")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> heroesList = gson.fromJson(new InputStreamReader(in), listType);

            if (heroesList == null) {
                throw new RuntimeException("heroes.json could not be loaded or is empty");
            }

            Map<String, Object> heroData = null;
            for (Map<String, Object> hero : heroesList) {
                if (heroName.equals(hero.get("name"))) {
                    heroData = hero;
                    break;
                }
            }

            if (heroData == null) {
                throw new RuntimeException("Hero not found: " + heroName);
            }

            String basePath = (String) heroData.get("basePath");
            Number reiatsu = (Number) heroData.get("reiatsu");
            Number attackRange = (Number) heroData.get("attackRange");

            HeroBuilder builder = new HeroBuilder()
                    .setName(heroName)
                    .setReiatsu(reiatsu.intValue())
                    .setAttackRange(attackRange.doubleValue())
                    .setBasePath(basePath)
                    .setProjectiles(sharedProjectiles);

            // Resolve melee attacks
            List<String> meleeAttackIds = (List<String>) heroData.get("meleeAttacks");
            List<AttackStrategy> meleeAttacks = new ArrayList<>();
            if (meleeAttackIds != null) {
                for (String attackId : meleeAttackIds) {
                    AttackStrategy attack = attacksMap.get(attackId);
                    if (attack != null) {
                        meleeAttacks.add(attack);
                    } else {
                        System.err.println("Warning: Attack not found: " + attackId);
                    }
                }
            }

            // Resolve ranged attacks
            List<String> rangedAttackIds = (List<String>) heroData.get("rangedAttacks");
            List<AttackStrategy> rangedAttacks = new ArrayList<>();
            if (rangedAttackIds != null) {
                for (String attackId : rangedAttackIds) {
                    AttackStrategy attack = attacksMap.get(attackId);
                    if (attack != null) {
                        rangedAttacks.add(attack);
                    } else {
                        System.err.println("Warning: Attack not found: " + attackId);
                    }
                }
            }

            builder.setMeleeAttacks(meleeAttacks);
            builder.setRangedAttacks(rangedAttacks);

            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
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

