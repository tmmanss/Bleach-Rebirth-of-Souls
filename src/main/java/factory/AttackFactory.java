package factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.SpriteLoader;
import strategy.AttackStrategy;
import strategy.MeleeAttack;
import strategy.Projectile;
import strategy.RangedAttack;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttackFactory {

    private static final Map<String, Map<String, Object>> attackData=new HashMap<>();
    static {
        try (InputStream in=AttackFactory.class.getResourceAsStream("/data/attacks.json")) {
            if (in==null)
                throw new RuntimeException("error: attacks.json not found");
            Gson gson=new Gson();
            Type listType= new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String,Object>> attackList= gson.fromJson(new InputStreamReader(in),listType);
            for (Map<String,Object> attack: attackList) {
                String id=(String) attack.get("id");
                attackData.put(id,attack);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load attacks.json", e);
        }
    }

    public static AttackStrategy createAttack(String id, List<Projectile> sharedProjectiles) {
        Map<String, Object> data=attackData.get(id);

        if (data==null)
            throw new RuntimeException("Attack '"+id+"' not found in attacks.json");
        String type=(String)data.get("type");
        String framesPath=(String)data.get("framesPath");
        Number damage=(Number)data.get("damage");
        Number cost=(Number)data.get("reiatsuCost");
        Number range=(Number)data.get("range");
        BufferedImage[] frames=SpriteLoader.loadFrames(framesPath);
        if (frames==null || frames.length==0)
            throw new RuntimeException("Attack "+id+" failed to load frames from "+framesPath);

        switch (type) {
            case "melee":
                return new MeleeAttack(
                        frames,
                        range.doubleValue(),
                        damage.intValue(),
                        cost.intValue()
                );

            case "ranged":
                String projectilePath=(String)data.get("projectileSprite");
                Number speed=(Number)data.get("projectileSpeed");
                Number fireFrame=(Number)data.get("fireFrame");

                if (projectilePath==null)
                    throw new RuntimeException("Ranged attack '"+id+"' missing projectileSprite");
                BufferedImage proj=SpriteLoader.loadImage(projectilePath);
                int fireFrameInt=fireFrame.intValue();
                if (fireFrameInt>=frames.length) {
                    System.err.println("Warning: fireFrame out of bounds for "+id+
                            ".setting fireFrame to last frame");
                    fireFrameInt=frames.length-1;
                }

                return new RangedAttack(
                        frames,
                        sharedProjectiles,
                        proj,
                        speed.doubleValue(),
                        range.doubleValue(),
                        damage.intValue(),
                        cost.intValue(),
                        fireFrameInt
                );

            default:
                throw new RuntimeException("Unknown attack type: "+type);
        }
    }

}
