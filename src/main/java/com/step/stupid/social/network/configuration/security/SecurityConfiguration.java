package com.step.stupid.social.network.configuration.security;

import com.step.stupid.social.network.configuration.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.step.stupid.social.network.configuration.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.step.stupid.social.network.configuration.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.step.stupid.social.network.service.impl.UserDetailsServiceImpl;
import com.step.stupid.social.network.service.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static com.step.stupid.social.network.util.ConstantUtil.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private HttpCookieOAuth2AuthorizationRequestRepository requestRepository;
    private OAuth2AuthenticationSuccessHandler successHandler;
    private OAuth2AuthenticationFailureHandler failureHandler;
    private TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        final int strength = 8;

        return new BCryptPasswordEncoder(strength);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();

        tokenAuthenticationFilter.setTokenProvider(tokenProvider);
        tokenAuthenticationFilter.setUserDetailsService(userDetailsService);

        return tokenAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher(BASE_URI_MATCHER)
                .authorizeRequests()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors(corsConfigurer -> {
                    CorsRegistry registry = new CorsRegistry();
                    registry.addMapping("/**")
                            .allowedOrigins("*")
                            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .allowCredentials(true)
                            .maxAge(3600);
                })
                .httpBasic()
                .disable()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login/**",
                        "/registration",
                        "/error",
                        "/static/**",
                        "/api/v1/user/**"
                ).permitAll()
                .antMatchers(AUTH_MATCHER, OAUTH2_MATCHER)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri(OAUTH2_AUTHORIZE_BASE_URI)
                .authorizationRequestRepository(requestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri(OAUTH2_CALLBACK_BASE_URI)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .formLogin()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .logout()
                .permitAll();

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setRequestRepository(HttpCookieOAuth2AuthorizationRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Autowired
    public void setSuccessHandler(OAuth2AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Autowired
    public void setFailureHandler(OAuth2AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}
