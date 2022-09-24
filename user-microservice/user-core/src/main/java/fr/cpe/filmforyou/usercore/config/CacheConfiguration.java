package fr.cpe.filmforyou.usercore.config;

import fr.cpe.filmforyou.config.AbstractCacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration extends AbstractCacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager("getUser", "isTokenValid");
        concurrentMapCacheManager.setCacheNames(this.getCacheNames());
        return concurrentMapCacheManager;
    }

}
