package br.com.vilardo.demo.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import br.com.vilardo.demo.quarkus.beans.ComponentMapper;

@ApplicationScoped
public class AzureDevOpsRepoRoute extends RouteBuilder{
    
    private static String ID = "{{route.azure.repo.list.id}}";
    public static String URI = "{{route.azure.repo.list.uri}}";

    public static String OUT_URI = "{{route.azure.repo.list.out}}";

    public static String basePath = "{{app.azure.repo.path}}";

    @Inject
    ComponentMapper compMapper;

    @Override
    public void configure() throws Exception {
        

        from(URI)
            .routeId(ID)
            .setHeader(Exchange.HTTP_PATH, simple(basePath))
            .setHeader(Exchange.HTTP_QUERY, simple("api-version=6.0"))
            .to(OUT_URI)
            // .convertBodyTo(String.class)
            // .log(LoggingLevel.INFO, "RESPONSE > ${body}")
            .split().jsonpath("$.value[*]").parallelProcessing()
                .setProperty("id", jsonpath("$.id"))
                .setProperty("name", jsonpath("$.name"))
                .removeHeader("*")
                
                .setHeader(Exchange.HTTP_PATH, simple(basePath+"/${exchangeProperty.id}/items"))
                .setHeader(Exchange.HTTP_QUERY, simple("path=pom.xml&api-version=6.0"))
                .setBody(constant(null))
                .to(OUT_URI)
                .filter(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo("200"))
                    .log(LoggingLevel.INFO, "ACHOU! ${exchangeProperty.id} - ${exchangeProperty.name} \n")
                    .split().tokenizeXML("artifactId", "dependency")
                    //.split().xpath("/project/dependencies/dependency/artifactId/text()",String.class)
                        .filter(body().contains("camel-"))
                        .bean(compMapper, "registerComponent");
                        
                ;

            
    }
}
