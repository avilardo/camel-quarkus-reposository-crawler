package br.com.vilardo.demo.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import br.com.vilardo.demo.quarkus.beans.ComponentMapper;
import br.com.vilardo.demo.quarkus.beans.TerminateBean;

@ApplicationScoped
public class RepoCrawlerRoute extends RouteBuilder{
    
    private static String TRIGGER_ID = "{{route.repo.crawler.trigger.id}}";
    private static String TRIGGER_URI = "{{route.repo.crawler.trigger.uri}}";

    @Inject
    ComponentMapper compMapper;

    @Inject
    TerminateBean terminateBean;

    @Override
    public void configure() throws Exception {
        

        from(TRIGGER_URI)
            .routeId(TRIGGER_ID)
            .log(LoggingLevel.INFO, "Teste \n ${body}")
            .to(AzureDevOpsRepoRoute.URI)
            .bean(compMapper, "printMap")
            .bean(terminateBean);

    }
}
