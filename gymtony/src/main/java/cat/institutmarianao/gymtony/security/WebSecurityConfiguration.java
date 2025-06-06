package cat.institutmarianao.gymtony.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cat.institutmarianao.gymtony.repositories.UsuarioRepository;

@Configuration
public class WebSecurityConfiguration {

    protected static final String LOGIN_URL = "/login";
    protected static final String LOGIN_FAIL_URL = "/loginfailed";
    protected static final String LOGOUT_URL = "/logout";
    protected static final String DEFAULT_SUCCESS_URL = "/home";  // Página a la que redirigir después de un login exitoso

    protected static final String[] ENDPOINTS_WHITELIST = {
        "/css/**", "/images/**", LOGIN_URL, LOGIN_FAIL_URL, "/register"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return new CustomUserDetailsService(usuarioRepository);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                .requestMatchers("/admin/**", "/clases/*/delete").hasRole("responsable")
                .requestMatchers("/clases/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage(LOGIN_URL)
                .defaultSuccessUrl(DEFAULT_SUCCESS_URL, true)
                .failureUrl(LOGIN_FAIL_URL)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl(LOGIN_URL)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
