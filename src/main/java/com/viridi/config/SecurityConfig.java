package com.viridi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.viridi.security.CustomUserDetailService;
import com.viridi.security.JwtAuthenticationEntryPoint;
import com.viridi.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired 
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	
	private static final String[] PUBLIC_URLS = {"/api/v1/auth/**","/api/v1/category/**", "/api/v1/review/**", "/api/v1/coupon/**","/api/v1/product/**","/api/v1/cart/**","/api/v1/orders/**", "/v3/api-docs", "/v2/api-docs",
            "/swagger-resources/**", "/swagger-ui/**", "/webjars/**"

    };

	private final String[] ADMIN_URLS = {
            "/user/**"
    };
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
 
        http	
        		.csrf(csrf -> csrf.disable())
        		.authorizeHttpRequests(auth -> auth
        		.requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
                .requestMatchers(HttpMethod.DELETE, ADMIN_URLS).hasRole("ADMIN")
                /*.requestMatchers(HttpMethod.POST, ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, ADMIN_URLS).hasRole("ADMIN")*/
                .anyRequest()
                .authenticated())
        	.exceptionHandling(ex ->
    			ex.authenticationEntryPoint(this.jwtAuthenticationEntryPoint))
    		.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    		;
         
       
		
		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.authenticationProvider(daoAuthenticationProvider());
		DefaultSecurityFilterChain build = http.build();
		
		return build;
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
		
	}	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
    public FilterRegistrationBean coresFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.addAllowedHeader("Content-Type");
        corsConfiguration.addAllowedHeader("Accept");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        bean.setOrder(-110);

        return bean;
    }
	
}
