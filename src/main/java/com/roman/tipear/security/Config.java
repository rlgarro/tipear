package com.roman.tipear.security;

import com.roman.tipear.implementation.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {

   @Autowired
   private UserDetailsImpl userDetailsService;

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   // AUTHENTICATION
   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }

   // AUTHORIZATION
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/login", "/register", "/confirm/**").anonymous().antMatchers("/", "/?recover=true", "/u/**", "/u/js/**", "/test/text", "/css/**", "/js/**", "/recover", "/recover/**").permitAll().anyRequest().authenticated()
              .and()
                 .formLogin().usernameParameter("username").loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error=true")
                 .loginProcessingUrl("/login/process").permitAll()
             .and()
              .logout().logoutUrl("/logout").logoutSuccessUrl("/");
   }
}
