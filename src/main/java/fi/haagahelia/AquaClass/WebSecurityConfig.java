package fi.haagahelia.AquaClass;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	// @Autowired
	private UserDetailsService userDetailsService; // type of attribute -> interface

	// Constructor injection
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {

			new AntPathRequestMatcher("/css/**"), // Enable css when logged out
			new AntPathRequestMatcher("/signup"),
			new AntPathRequestMatcher("/saveuser"),
			new AntPathRequestMatcher("/login")
	};

	// with lambda
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(WHITE_LIST_URLS).permitAll()
						.requestMatchers("/api/**").authenticated()
						.anyRequest().authenticated())
				// Form login cho web
				.formLogin(formlogin -> formlogin
						.loginPage("/login")
						.defaultSuccessUrl("/courselist", true)
						.permitAll())
				// Basic Auth cho API (Cách mới)
				.httpBasic(httpBasicCustomizer -> {
				})
				// Disable CSRF for API endpoints
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/api/**"))
				// Cấu hình session:
				.sessionManagement(session -> session
						.sessionCreationPolicy(
								org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED))
				.logout(logout -> logout.permitAll());

		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
