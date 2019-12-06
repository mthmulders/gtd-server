package com.infosupport.training.reactjs.gtdserver.config;

import com.infosupport.training.reactjs.gtdserver.security.TokenAuthenticationFilter;
import com.infosupport.training.reactjs.gtdserver.security.TokenAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import static com.infosupport.training.reactjs.gtdserver.config.SecurityConfiguration.PROTECTED_URLS;
import static com.infosupport.training.reactjs.gtdserver.config.SecurityConfiguration.PUBLIC_URLS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class GtdSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationProvider provider;
    private final AuthenticationEntryPoint forbiddenEntryPoint;
    private final SimpleUrlAuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
            .and()
                .exceptionHandling()
                // this entry point handles when you request a protected page and you are not yet authenticated
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint, PROTECTED_URLS)
            .and()
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PROTECTED_URLS)
                .authenticated()
            .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    public TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler);
        return filter;
    }
}
