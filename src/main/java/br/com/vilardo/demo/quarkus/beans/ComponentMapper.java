package br.com.vilardo.demo.quarkus.beans;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Body;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("ComponentMapper")
@RegisterForReflection
public class ComponentMapper {
    
    public Map<String, Integer> componentMap;

    public void registerComponent(@Body String dependency){

        if(componentMap == null){
            componentMap = new HashMap<>();
        }

        Integer count = componentMap.get(dependency);
        if(count == null){
            count = 0;
        }
        count++;
        componentMap.put(dependency, count);
    }

    public void printMap() {
        System.out.println("COMPONENTE SUMMARY:");
        for (String key : componentMap.keySet()) {
            System.out.println("    " + key.replaceAll("<[^>]+>", "") + "," + componentMap.get(key));
        }
    
    }
}
