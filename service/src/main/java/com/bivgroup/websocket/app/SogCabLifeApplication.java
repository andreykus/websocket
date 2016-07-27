package com.bivgroup.websocket.app;


import com.bivgroup.websocket.servlet.FileServlet;
import com.bivgroup.websocket.websocket.spring.WebSocketConfigure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan

//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableDiscoveryClient

//@EnableOAuth2Client
//@EnableConfigurationProperties

//@EnableZuulProxy
//@EnableTurbineStream
//@EnableHystrixDashboard

public class SogCabLifeApplication extends ResourceServerConfigurerAdapter implements WebApplicationInitializer {

    private static Logger logger = LogManager.getLogger(SogCabLifeApplication.class);

    private static final String SERVER_PROPS_PATH = "conf/server.properties";

    @Autowired
    private ResourceServerProperties sso;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addServlet("dispatchServlet", new FileServlet());


        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebSocketConfigure.class);
        ctx.setServletContext(servletContext);
        logger.debug("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ {}", servletContext.getContextPath());
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
       //dynamic.addMapping("/asss");
       // dynamic.setLoadOnStartup(1);

        //super.onStartup(servletContext);
    }


    private static Properties loadPropsFromFile(String filename) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(filename));
            return props;
        } catch (java.io.IOException e) {
            logger.error("Failed to load properties from file {}, exiting: {}", filename, e.getMessage());
            System.exit(-1);
        }
        return null;
    }


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SogCabLifeApplication.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }


        //PropertyConfigurator.configure(LOG4J_PROPS_PATH);
// this init jetty websocket
//        Properties wsProps = loadPropsFromFile(SERVER_PROPS_PATH);
//        WebsocketServer server = new WebsocketServer(wsProps);
//        server.run();



    }

//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }

//	@Bean
//	public RequestInterceptor oauth2FeignRequestInterceptor(){
//		return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
//	}
//
//    @Bean
//    public OAuth2RestTemplate clientCredentialsRestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
//    }
//
//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/", "/demo").permitAll()
//                .anyRequest().authenticated();
//    }

//
//
//	@Bean
//	public RequestInterceptor oauth2FeignRequestInterceptor(){
//		return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
//	}


////////////////////////////


//	@Configuration
//	@EnableWebSecurity
//	protected static class webSecurityConfig extends WebSecurityConfigurerAdapter {
//
//		@Autowired
//		private MeWebUserDetailsService userDetailsService;
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			// @formatter:off
//			http
//					.authorizeRequests().anyRequest().authenticated()
//					.and()
//					.csrf().disable();
//			// @formatter:on
//		}
//
//		@Override
//		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//			auth.userDetailsService(userDetailsService)
//					.passwordEncoder(new BCryptPasswordEncoder());
//		}
//
//		@Override
//		@Bean
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}
//	}

//	@Configuration
//	@EnableAuthorizationServer
//	protected static class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
//
//		private TokenStore tokenStore = new InMemoryTokenStore();
//
//		@Autowired
//		@Qualifier("authenticationManagerBean")
//		private AuthenticationManager authenticationManager;
//
//		@Autowired
//		private MeWebUserDetailsService userDetailsService;
//
//		@Autowired
//		private Environment env;
//
//		@Override
//		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//			// TODO persist clients details
//
//			// @formatter:off
//			clients.inMemory()
//					.withClient("browser")
//					.authorizedGrantTypes("refresh_token", "password")
//					.scopes("ui")
//					.and()
//					.withClient("account-service")
//					.secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
//					.authorizedGrantTypes("client_credentials", "refresh_token")
//					.scopes("server")
//					.and()
//					.withClient("statistics-service")
//					.secret(env.getProperty("STATISTICS_SERVICE_PASSWORD"))
//					.authorizedGrantTypes("client_credentials", "refresh_token")
//					.scopes("server")
//					.and()
//					.withClient("notification-service")
//					.secret(env.getProperty("NOTIFICATION_SERVICE_PASSWORD"))
//					.authorizedGrantTypes("client_credentials", "refresh_token")
//					.scopes("server");
//			// @formatter:on
//		}
//
//		@Override
//		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//			endpoints
//					.tokenStore(tokenStore)
//					.authenticationManager(authenticationManager)
//					.userDetailsService(userDetailsService);
//		}
//
//		@Override
//		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//			oauthServer
//					.tokenKeyAccess("permitAll()")
//					.checkTokenAccess("isAuthenticated()");
//		}
//	}


//    @Bean
//    public MultipartConfigElement multipartConfigElement(@Value("${upload.max_archive_size}") long maxUploadSize) {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxRequestSize(maxUploadSize);
//        factory.setMaxFileSize(maxUploadSize);
//        return factory.createMultipartConfig();
//    }
//
//    @Bean
//    public StandardServletMultipartResolver multipartResolver() {
//        return new StandardServletMultipartResolver();
//    }
//
//    @Bean
//    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet, MultipartConfigElement multipartConfig) {
//        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
//        registration.addUrlMappings("/*");
//        registration.setMultipartConfig(multipartConfig);
//        registration.setAsyncSupported(true);
//        return registration;
//    }

    /**
     * bind servlet for site metadata (html,css,js ...)
     * @return
     */
    @Bean
    public ServletRegistrationBean metadataCatalogRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean();
        Map<String, String> params = new HashMap<String, String>();
        params.put("basePath", "C:/projects/websocket/metadata/target/generated-resources/static");
        registration.setName("fileServlet");
        registration.addUrlMappings("/html/*");
        registration.setInitParameters(params);
        registration.setServlet(new FileServlet());
        return registration;
    }

    /**
     * bind servlet for start websocket (sockJS)
     * @return
     */
    @Bean
    public ServletRegistrationBean webSocketRegistration() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebSocketConfigure.class);
        ServletRegistrationBean registration = new ServletRegistrationBean();
        Map<String, String> params = new HashMap<String, String>();
        registration.setName("websoketServlet");
        registration.addUrlMappings("/websockets/*");
        registration.setInitParameters(params);
        registration.setServlet(new DispatcherServlet(ctx));
        return registration;
    }

}
