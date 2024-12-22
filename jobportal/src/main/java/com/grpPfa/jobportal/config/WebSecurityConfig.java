package com.grpPfa.jobportal.config;

import com.grpPfa.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomSuccessHandler customSuccessHandler;
    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
        CustomSuccessHandler customSuccessHandler

    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.customSuccessHandler=customSuccessHandler;
    }

    private final String [] publicUrls={"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "webjars/**",
            "/resources/**",
            "/assets/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**","/favicon","/resources/**","/error"
    };
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
                http.authenticationProvider(authenticationProvider());
                http.authorizeHttpRequests(auth-> {auth.requestMatchers(publicUrls).permitAll()
                        .anyRequest().authenticated();
                });
                http.formLogin(form-> form.loginPage("/login").permitAll().successHandler(customSuccessHandler)).logout(logout->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                }).cors(Customizer.withDefaults())
                        .csrf(AbstractHttpConfigurer::disable);


            return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
            return new  BCryptPasswordEncoder();
    }
}
