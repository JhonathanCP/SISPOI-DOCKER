package com.essalud.sispoi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class URISensitiveConfig implements WebMvcConfigurer {

    @SuppressWarnings("deprecation")
    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        PathPatternParser patternParser = new PathPatternParser();
        patternParser.setCaseSensitive(false);
        configurer.setUseTrailingSlashMatch(true);
        configurer.setPatternParser(patternParser);
    }

    // @Configuration
    // public static class TrailingSlashFilter implements Filter {
    //     @Override
    //     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    //             throws IOException, ServletException {
    //         if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
    //             HttpServletRequest httpRequest = (HttpServletRequest) request;
    //             String requestURI = httpRequest.getRequestURI();
    //             HttpServletResponse httpResponse = (HttpServletResponse) response;

    //             // Si la URI termina con una barra, redirige a la versión sin barra
    //             if (requestURI.endsWith("/") && requestURI.length() > 1) {
    //                 String newURI = requestURI.substring(0, requestURI.length() - 1);
    //                 httpResponse.sendRedirect(newURI);
    //                 return;
    //             }
    //         }
    //         chain.doFilter(request, response);
    //     }
    // }
}