package fr.cpe.filmforyou.config;

import java.util.List;

public abstract class AbstractCacheConfiguration {

    protected List<String> getCacheNames() {
        return List.of("getUserDTO");
    }

}
