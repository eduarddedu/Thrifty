package org.codecritique.thrifty.security;

import org.codecritique.thrifty.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Profile("prod")
@Configuration
@EnableWebSecurity
public class ProdSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDao userDao;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return s -> {
            org.codecritique.thrifty.entity.User user = userDao.findByUsernameIgnoreCase(s);
            if (user == null) throw new UsernameNotFoundException("Wrong user email or password");
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER").build();
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(r -> r.getRequestURI().matches(".*.(css|js)")).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}