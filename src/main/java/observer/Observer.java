package observer;

import heroes.BaseHero;

public interface Observer {
    void update(String event, BaseHero hero, int value);
}