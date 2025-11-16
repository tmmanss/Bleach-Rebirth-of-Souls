package factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import strategy.AttackStrategy;
import strategy.MeleeAttack;
import strategy.RangedAttack;
import strategy.Projectile;
import core.SpriteLoader;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttackLoader {

    public static Map<String, AttackStrategy> loadAttacks(List<Projectile> sharedProjectiles) {
        Map<String, AttackStrategy> attacksMap = new HashMap<>();

        try (InputStream in = AttackLoader.class.getResourceAsStream("/data/attacks.json")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> attacksList = gson.fromJson(new InputStreamReader(in), listType);

            if (attacksList == null) {
                throw new RuntimeException("attacks.json could not be loaded or is empty");
            }

            for (Map<String, Object> attackData : attacksList) {
                String id = (String) attackData.get("id");
                String type = (String) attackData.get("type");
                String framesPath = (String) attackData.get("framesPath");

                Number damage = (Number) attackData.get("damage");
                Number reiatsuCost = (Number) attackData.get("reiatsuCost");
                Number range = (Number) attackData.get("range");

                BufferedImage[] frames = SpriteLoader.loadFrames(framesPath);

                // Skip attack if frames failed to load
                if (frames == null || frames.length == 0) {
                    System.err.println("WARNING: Skipping attack '" + id + "' - frames failed to load from: " + framesPath);
                    continue;
                }

                AttackStrategy attack = null;

                if ("melee".equals(type)) {
                    attack = new MeleeAttack(
                            frames,
                            range.doubleValue(),
                            damage.intValue(),
                            reiatsuCost.intValue()
                    );
                } else if ("ranged".equals(type)) {
                    String projectilePath = (String) attackData.get("projectileSprite");
                    Number projectileSpeed = (Number) attackData.get("projectileSpeed");
                    Number fireFrame = (Number) attackData.get("fireFrame");

                    if (projectilePath == null || projectilePath.isEmpty()) {
                        System.err.println("WARNING: Skipping ranged attack '" + id + "' - projectileSprite path is missing");
                        continue;
                    }

                    BufferedImage projectileSprite = SpriteLoader.loadImage(projectilePath);

                    // Skip ranged attack if projectile sprite failed to load
                    if (projectileSprite == null) {
                        System.err.println("WARNING: Skipping ranged attack '" + id + "' - projectile sprite failed to load from: " + projectilePath);
                        continue;
                    }

                    int fireFrameInt = fireFrame.intValue();
                    if (fireFrameInt >= frames.length) {
                        System.err.println("WARNING: fireFrame (" + fireFrameInt + ") >= frames.length (" + frames.length + 
                                         ") for attack '" + id + "'. Setting fireFrame to " + (frames.length - 1));
                        fireFrameInt = frames.length - 1;
                    }
                    
                    System.out.println("Loaded ranged attack '" + id + "' - projectile sprite: " + projectilePath + 
                                     " (size: " + projectileSprite.getWidth() + "x" + projectileSprite.getHeight() + 
                                     "), frames: " + frames.length + ", fireFrame: " + fireFrameInt);

                    attack = new RangedAttack(
                            frames,
                            sharedProjectiles,
                            projectileSprite,
                            projectileSpeed.doubleValue(),
                            range.doubleValue(),
                            damage.intValue(),
                            reiatsuCost.intValue(),
                            fireFrameInt
                    );
                }

                if (attack != null) {
                    attacksMap.put(id, attack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load attacks.json", e);
        }

        return attacksMap;
    }
}

