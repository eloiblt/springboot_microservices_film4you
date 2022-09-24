package fr.cpe.filmforyou.config;

import fr.cpe.filmforyou.userlib.webservice.UserWebService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

public abstract class AbstractWebConfig extends WebSecurityConfigurerAdapter {

    private final UserWebService userWebService;

    protected AbstractWebConfig(UserWebService userWebService) {
        this.userWebService = userWebService;
    }

    /**
     * Configuration des filtres à appliquer aux requêtes.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/swagger-ui/index.html**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll()
                // Tous ce qui est public est authorisé
                .antMatchers("/public/**").permitAll()
                // Tous ce qui est private est authorisé car c'est une communication Microservice to Microservice
                .antMatchers("/private/**").permitAll()
                // Tous ce qui commence par secured doit être authentifié.
                .antMatchers("/secured/**").authenticated()
                .anyRequest().anonymous()
                .and()
                .addFilter(new AbstractAuthFilter(authenticationManager(), this.userWebService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
