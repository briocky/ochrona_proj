package pl.edu.pw.ee.secureloansystem.infrastructure.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.encoding.MyPasswordEncoder;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.filter.TokenFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfiguration {

  final TokenFilter tokenFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults());
    http.csrf(AbstractHttpConfigurer::disable);

    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

    http.authorizeHttpRequests(request -> {
      request.requestMatchers("/api/auth/login").permitAll();
      request.requestMatchers("/api/auth/register").permitAll();

      request.anyRequest().authenticated();
    });

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new MyPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      PasswordEncoder encoder, UserDetailsService userService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(encoder);
    provider.setUserDetailsService(userService);
    return provider;
  }
}
