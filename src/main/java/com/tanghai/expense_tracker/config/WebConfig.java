package com.tanghai.expense_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class WebConfig {
//
//    @Value("${backend_server.web_url}")
//    private String SERVER_URL;

    @Bean
    public CorsFilter corsFilter() {
        // Create a new CorsConfiguration object
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials like cookies, authorization headers, or TLS client
        // certificates
        config.setAllowCredentials(true);

        // Specify the list of allowed origins that can access the application
        // Your front-end URLs only
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173",
                "https://expense-tracker-v2-web.onrender.com/" // Production domain
        ));

        // Allow Standard headers
        config.setAllowedHeaders(Arrays.asList("Authorization", // Authorization → for tokens (JWT, OAuth, etc.)
                "Cache-Control", // Cache-Control → browser cache behavior
                "Content-Type", // Content-Type → like application/json
                "X-Requested-With", // X-Requested-With → for Ajax requests (especially legacy)
                "Accept", // Accept → what response formats are acceptable (application/json, etc.)
                "Origin" // Origin → original domain of request (used internally)
        ));

        // Specify the HTTP methods that are allowed
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));

        // Create a source that maps URL patterns to CORS configurations
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Register the CORS configuration to apply to all paths (/** means all endpoints)
        source.registerCorsConfiguration("/**", config);

        // Return the CorsFilter with the configured source
        return new CorsFilter(source);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilder jacksonBuilder() {
//        return new Jackson2ObjectMapperBuilder().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//    }
//
//    // Optional: Provide ObjectMapper if needed directly
//    @Bean
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.build();
//    }
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins(SERVER_URL)  // your frontend origin
//                        .allowedMethods("GET", "POST", "PATCH")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }
}