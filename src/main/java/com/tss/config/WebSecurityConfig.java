package com.tss.config;

import com.tss.filters.JwtRequestFilter;
import com.tss.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final DataSource dataSourceAuth;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUserDetailsService jwtUserDetailsService;

    public WebSecurityConfig(@Qualifier("dataSourceAuth") DataSource dataSourceAuth, JwtUserDetailsService jwtUserDetailsService, JwtRequestFilter jwtRequestFilter ) {
        this.dataSourceAuth = dataSourceAuth;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSourceAuth)
                .usersByUsernameQuery("select username, password, active from credentials where username=?")
                .authoritiesByUsernameQuery("select username, user_role from users_roles where username=?");
        auth.userDetailsService(jwtUserDetailsService);
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("password").roles("ADMIN");
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/rest/**").allowedOriginPatterns("http://localhost:[*]");
            }
        };
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(authenticate -> authenticate
                        .requestMatchers("/rest/authenticate").permitAll()
                        .requestMatchers("/rest/user/addUser").permitAll()
                        .requestMatchers(HttpMethod.GET,"/rest/**").permitAll()
                )
                .antMatcher("/rest/**")
                .authorizeHttpRequests(rest -> rest
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .requiresChannel().anyRequest().requiresSecure();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(web -> web
                        .requestMatchers("/tss").permitAll()
                        .requestMatchers("/registerForm").permitAll()
                        .requestMatchers("/addUser").permitAll()
                        .anyRequest().authenticated()
                )
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                .formLogin(withDefaults());
        return http.build();
    }
}
