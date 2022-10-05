package se.gewalli.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration;

import se.gewalli.AppendBatch;
import se.gewalli.CommandsHandler;
import se.gewalli.data.InMemoryRepository;
import se.gewalli.data.Repository;
import se.gewalli.json.AppendToFile;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spring.web.WebMvcRequestHandler;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

@Configuration
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public Repository repository() {
        return new InMemoryRepository();
    }

    @Bean
    public AppendBatch appendBatch() throws IOException {
        var dbLocation = env.getProperty("FILE_DB_LOCATION");
        var logger = LoggerFactory.getLogger(AppConfig.class);
        if (dbLocation == null || dbLocation.isEmpty()) {
            logger.info("No database location found, using tmp");
            dbLocation = "/tmp/test.db";
        }
        var db=new File(dbLocation);
        if (! db.exists()){
            db.createNewFile();
        }
        return new AppendToFile(dbLocation,
                Executors.newFixedThreadPool(1),
                err -> logger.error("Error while appending ", err));
    }

    @Bean
    public CommandsHandler persistCommandsHandler() {
        return new CommandsHandler();
    }
    @Bean
    public InitializingBean removeSpringfoxHandlerProvider(DocumentationPluginsBootstrapper bootstrapper) {
        return () -> bootstrapper.getHandlerProviders().removeIf(WebMvcRequestHandlerProvider.class::isInstance);
    }

    @Bean
    public RequestHandlerProvider customRequestHandlerProvider(Optional<ServletContext> servletContext, HandlerMethodResolver methodResolver, List<RequestMappingInfoHandlerMapping> handlerMappings) {
        String contextPath = servletContext.map(ServletContext::getContextPath).orElse(Paths.ROOT);
        return () -> handlerMappings.stream()
            .filter(mapping -> !mapping.getClass().getSimpleName().equals("IntegrationRequestMappingHandlerMapping"))
            .map(mapping -> mapping.getHandlerMethods().entrySet())
            .flatMap(Set::stream)
            .map(entry -> new WebMvcRequestHandler(contextPath, methodResolver, tweakInfo(entry.getKey()), entry.getValue()))
            .sorted(RequestHandler.byPatternsCondition())
            .collect(Collectors.toList());
    }

    RequestMappingInfo tweakInfo(RequestMappingInfo info) {
        if (info.getPathPatternsCondition() == null) return info;
        String[] patterns = info.getPathPatternsCondition().getPatternValues().toArray(String[]::new);
        return info.mutate().options(new BuilderConfiguration()).paths(patterns).build();
    }
}