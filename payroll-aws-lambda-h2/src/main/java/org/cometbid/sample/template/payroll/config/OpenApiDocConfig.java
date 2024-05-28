/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.sample.template.payroll.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 *
 * @author samueladebowale
 */
@Configuration
public class OpenApiDocConfig {

    @Value("${local.server.apidoc.url}")
    private String localServerUrl;

    @Value("${test.server.apidoc.url}")
    private String testServerUrl;

    @Value("${api.common.version}")
    private String appVersion;

    @Value("${api.common.title}")
    private String appTitle;

    @Value("${api.common.description}")
    private String appDescription;

    @Value("${api.common.termsOfService}")
    private String appTermsOfService;

    @Value("${api.common.license}")
    private String appLicenseName;

    @Value("${api.common.licenseUrl}")
    private String appLicenseUrl;

    @Value("${api.common.externalDocDesc}")
    String apiExternalDocDesc;

    @Value("${api.common.externalDocUrl}")
    String apiExternalDocUrl;

    @Value("${api.common.contact.name}")
    String apiContactName;

    @Value("${api.common.contact.url}")
    String apiContactUrl;

    @Value("${api.common.contact.email}")
    String apiContactEmail;

    @Bean
    public OpenAPI OpenApiDocumentation() {
        Server localServer = new Server();
        localServer.setDescription("for local usages only");
        localServer.setUrl(localServerUrl);

        Server testServer = new Server();
        testServer.setDescription("for testing purposes only");
        testServer.setUrl(testServerUrl);

        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("apikey",
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                        .scheme("apikey"))
                        .addSecuritySchemes("openIdConnect",
                                new SecurityScheme().type(SecurityScheme.Type.OPENIDCONNECT)
                                        .scheme("openIdConnect")))
                .info(new Info().title(appTitle)
                        .description(appDescription)
                        .version(appVersion)
                        .contact(new Contact()
                                //
                                .name(apiContactName)
                                //
                                .url(apiContactUrl)
                                //
                                .email(apiContactEmail))
                        //
                        .termsOfService(appTermsOfService)
                        //
                        .license(new License()
                                //
                                .name(appLicenseName)
                                //
                                .url(appLicenseUrl)))
                .externalDocs(new ExternalDocumentation()
                        .description(apiExternalDocDesc)
                        .url(apiExternalDocUrl));

        openAPI.setServers(Arrays.asList(localServer, testServer));

        return openAPI;
    }
}
