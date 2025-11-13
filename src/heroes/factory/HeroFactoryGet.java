package heroes.factory;

public class HeroFactoryGet {
    public static HeroFactory getFactory(String name) {
        if(name==null)throw new IllegalArgumentException("name==null");
        return switch (name.toLowerCase()){
            case "zangetsu" -> new ZangetsuFactory();
            case "ichigo" -> new IchigoFactory();
            default -> throw new IllegalArgumentException("noname hero: "+name);
        };

    }
}
