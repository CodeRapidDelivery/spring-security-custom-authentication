package com.marbor.customauthentication.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import com.marbor.customauthentication.resources.Routes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(
            ApiDescriptionReader apiDescriptionReader,
            ApiModelReader apiModelReader,
            DocumentationPluginsManager pluginsManager,
            TypeResolver typeResolver) {
        return new WithAuthenticationOperationsScanner(apiDescriptionReader, apiModelReader, pluginsManager, typeResolver);
    }
}

class WithAuthenticationOperationsScanner extends ApiListingScanner {
    private final TypeResolver typeResolver;

    public WithAuthenticationOperationsScanner(
            ApiDescriptionReader apiDescriptionReader,
            ApiModelReader apiModelReader,
            DocumentationPluginsManager pluginsManager,
            TypeResolver typeResolver) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
        this.typeResolver = typeResolver;
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        final Multimap<String, ApiListing> definition = super.scan(context);
        final List<ApiDescription> apis = new LinkedList<>();
        final Tag authenticationResource = new Tag("authentication-resource", "Authentication resource");

        apis.add(
                new ApiDescriptionBuilder(context.getDocumentationContext().operationOrdering())
                        .path(Routes.LOGIN_ROUTE)
                        .description("Authentication documentation")
                        .operations(List.of(createLoginOperation(authenticationResource.getName())))
                        .build()
        );
        apis.add(
                new ApiDescriptionBuilder(context.getDocumentationContext().operationOrdering())
                        .path(Routes.LOGOUT_ROUTE)
                        .description("Authentication documentation")
                        .operations(List.of(createLogoutOperation(authenticationResource.getName())))
                        .build()
        );

        definition.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(apis)
                .tags(Set.of(authenticationResource))
                .description("Authentication")
                .build());

        return definition;
    }

    private Operation createLoginOperation(String resourceName) {
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .uniqueId("login")
                .tags(Set.of(resourceName))
                .parameters(List.of(
                        new ParameterBuilder()
                                .name("username")
                                .description("user login")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("password")
                                .description("user password")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("department")
                                .description("user department")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build()
                        )
                )
                .responseMessages(Set.of(
                        new ResponseMessageBuilder()
                                .code(200)
                                .message("{}")
                                .build(),
                        new ResponseMessageBuilder()
                                .code(401)
                                .message("Unauthorized")
                                .build()
                ))
                .summary("Log in")
                .notes("Here you can log in")
                .build();
    }

    private Operation createLogoutOperation(String resourceName) {
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .uniqueId("logout")
                .tags(Set.of(resourceName))
                .summary("Log out")
                .notes("Here you can log out")
                .build();
    }
}
