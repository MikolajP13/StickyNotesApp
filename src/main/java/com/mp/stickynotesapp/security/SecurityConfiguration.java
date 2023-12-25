package com.mp.stickynotesapp.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(PathRequest.toH2Console()).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form.loginPage("/login").permitAll())
//                .logout(logout -> logout.logoutUrl("/logout").permitAll());
//
//        return http.build();
//    }
//
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//
//        roleHierarchy.setHierarchy("""
//            ROLE_ADMIN > ROLE_MANAGER
//            ROLE_MANAGER > ROLE_USER
//            """);
//
//        return roleHierarchy;
//    }
//
//    @Bean
//    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy);
//
//        return expressionHandler;
//    }

}
