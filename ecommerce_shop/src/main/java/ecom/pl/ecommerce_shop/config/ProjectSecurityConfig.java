package ecom.pl.ecommerce_shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity  // Enable method-level security
public class ProjectSecurityConfig {

    private final DataSource dataSource;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/**").authenticated()
                        .requestMatchers("/api/product/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/notices", "/contact", "/error").permitAll()
                );

        // Enable form login with custom success handler
        httpSecurity.formLogin(form -> form
                .successHandler(authenticationSuccessHandler())
        );

        // Enable basic authentication
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        // JDBC-based user service using data source and user roles
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Set SQL queries for retrieving user and role data from database
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?");

        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt encoder for password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        // Redirects to specified site after success login
        return new SimpleUrlAuthenticationSuccessHandler("/v3/swagger-ui.html");
    }
}
