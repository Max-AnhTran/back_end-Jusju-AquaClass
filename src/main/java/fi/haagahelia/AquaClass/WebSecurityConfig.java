package fi.haagahelia.AquaClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService; 

	@Autowired
	private JWTFilter jwtFilter; // type of attribute -> class

	// Constructor injection
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {
			new AntPathRequestMatcher("/css/**"), // Enable css when logged out
			new AntPathRequestMatcher("/signup"),
			new AntPathRequestMatcher("/saveuser"),
			new AntPathRequestMatcher("/login"),
			new AntPathRequestMatcher("/api/login")
	};

	// with lambda
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(WHITE_LIST_URLS).permitAll()
						.anyRequest().authenticated())
				// Form login cho web
				.formLogin(formlogin -> formlogin
						.loginPage("/login")
						.defaultSuccessUrl("/courselist", true)
						.permitAll())
				// Basic Auth cho API (Cách mới)
				.httpBasic(Customizer.withDefaults())
				// Disable CSRF for API endpoints
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/api/**"))
				// Cấu hình session:
				.sessionManagement(session -> session
						.sessionCreationPolicy(
								org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED))
				// add filter for JWT authentication
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.permitAll());

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
