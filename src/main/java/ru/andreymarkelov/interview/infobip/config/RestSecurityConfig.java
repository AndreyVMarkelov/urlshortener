package ru.andreymarkelov.interview.infobip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.andreymarkelov.interview.infobip.service.AccountService;
import ru.andreymarkelov.interview.infobip.util.RestBasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccountService accountService;

    @Autowired
    private RestBasicAuthenticationEntryPoint restBasicAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/account").anonymous()
                .antMatchers(HttpMethod.POST, "/register").authenticated()
                .antMatchers(HttpMethod.GET, "/statistic").authenticated()
                .antMatchers(HttpMethod.GET, "/r/**").anonymous()
                .anyRequest().permitAll()
                .and().httpBasic().authenticationEntryPoint(restBasicAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }
}
