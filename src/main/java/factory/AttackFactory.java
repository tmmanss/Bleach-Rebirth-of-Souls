package factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.SpriteLoader;
import strategy.*;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class AttackFactory {

    private static final Map<String, Map<String, Object>> attackData;

    static {
        Map<String, Map<String, Object>> temp = null;

        try (InputStream in = AttackFactory.class.getResourceAsStream("/data/attacks.json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
            temp = gson.fromJson(new InputStreamReader(in), type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        attackData = temp;
        if (attackData == null)
            throw new RuntimeException("attacks.json could not be loaded");
    }


    public static AttackStrategy createAttack(String name, List<Projectile> sharedProjectiles) {

        Map<String, Object> data = attackData.get(name);

        String type = (String) data.get("type");
        String framesPath = (String) data.get("framesPath");

        Number damage = (Number) data.get("damage");
        Number cost = (Number) data.get("cost");
        Number range = (Number) data.get("range");

        BufferedImage[] frames = SpriteLoader.loadFrames(framesPath);

        switch (type) {

            case "melee":
                return new MeleeAttack(
                        frames,
                        range.doubleValue(),
                        damage.intValue(),
                        cost.intValue()
                );

            case "ranged":
                String projectilePath = (String) data.get("projectileSprite");
                Number speed = (Number) data.get("projectileSpeed");
                Number fireFrame = (Number) data.get("fireFrame");

                BufferedImage proj = SpriteLoader.loadImage(projectilePath);

                return new RangedAttack(
                        frames,
                        sharedProjectiles,
                        proj,
                        speed.doubleValue(),
                        range.doubleValue(),
                        damage.intValue(),
                        cost.intValue(),
                        fireFrame.intValue()
                );
        }

        throw new RuntimeException("Unknown attack type: " + type);
    }

}
