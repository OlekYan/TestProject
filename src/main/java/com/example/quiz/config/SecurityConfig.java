package com.example.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/webjars/**", "/css/**", "/", "/login", "/register").permitAll()
            .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login").permitAll()
            .defaultSuccessUrl("/play", true)
        )
        .logout(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  public UserDetailsService users(PasswordEncoder encoder) {
    UserDetails admin = User.withUsername("admin")
        .password(encoder.encode("admin123"))
        .roles("ADMIN")
        .build();

    // 13 players user1..user13 with role USER
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(admin);
    for (int i = 1; i <= 13; i++) {
      UserDetails u = User.withUsername("user" + i)
          .password(encoder.encode("pass" + i))
          .roles("USER")
          .build();
      manager.createUser(u);
    }
    return manager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
