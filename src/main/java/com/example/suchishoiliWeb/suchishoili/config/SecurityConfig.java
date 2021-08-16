package com.example.suchishoiliWeb.suchishoili.config;

import com.example.suchishoiliWeb.suchishoili.fixedVariables.AdminFixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/addOrder", "/dashboard", "/watchInventory", "/expense", "/addCategory",
                        "/addInventory", "/orderList", "/addImage", "/admin").hasAuthority(AdminFixedValue.ADMIN_TYPE)
                .antMatchers(HttpMethod.GET, "/register", "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/submitAddCategory", "/submitAddProduct").hasAuthority(AdminFixedValue.ADMIN_TYPE)
                .and().formLogin().loginPage("/")
//                .loginProcessingUrl("/loginAdmin")
                .defaultSuccessUrl("/admin")
                .failureUrl("/?error").permitAll()
                .and().logout().logoutUrl("/adminLogout")
                .logoutSuccessUrl("/");
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return daoAuthenticationProvider;
    }
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
