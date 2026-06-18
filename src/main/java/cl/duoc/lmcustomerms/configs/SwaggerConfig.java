package cl.duoc.lmcustomerms.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()

                        .title("LM Customer API")
                        .version("v1")
                        .description("Documentacion para API REST de gestión de clientes en sistema Libreria Market"))
                .components(new Components()
                        .addExamples("ClienteDelete400Example", new Example().value(leerJson("/examples/ClienteDelete400Example.json")))
                        .addExamples("ClienteDelete404Example", new Example().value(leerJson("/examples/ClienteDelete404Example.json")))
                        .addExamples("ClienteFindAllByPnombre200Example", new Example().value(leerJson("/examples/ClienteFindAllByPnombre200Example.json")))
                        .addExamples("ClienteFindAllByPnombre404Example", new Example().value(leerJson("/examples/ClienteFindAllByPnombre404Example.json")))
                        .addExamples("ClienteFindByEmail200Example", new Example().value(leerJson("/examples/ClienteFindByEmail200Example.json")))
                        .addExamples("ClienteFindByEmail404Example", new Example().value(leerJson("/examples/ClienteFindByEmail404Example.json")))
                        .addExamples("ClienteFindByFono200Example", new Example().value(leerJson("/examples/ClienteFindByFono200Example.json")))
                        .addExamples("ClienteFindById200Example", new Example().value(leerJson("/examples/ClienteFindById200Example.json")))
                        .addExamples("ClienteFindByNumRun200Example", new Example().value(leerJson("/examples/ClienteFindByNumRun200Example.json")))
                        .addExamples("ClienteFindByNumRun400Example", new Example().value(leerJson("/examples/ClienteFindByNumRun400Example.json")))
                        .addExamples("ClienteFindByNumRun404Example", new Example().value(leerJson("/examples/ClienteFindByNumRun404Example.json")))
                        .addExamples("ClienteSave201Example", new Example().value(leerJson("/examples/ClienteSave201Example.json")))
                        .addExamples("ClienteSave400Example", new Example().value(leerJson("/examples/ClienteSave400Example.json")))
                        .addExamples("ClienteSave409Example", new Example().value(leerJson("/examples/ClienteSave409Example.json")))
                        .addExamples("ClienteUpdate200Example", new Example().value(leerJson("/examples/ClienteUpdate200Example.json")))
                        .addExamples("ClienteUpdate400Example", new Example().value(leerJson("/examples/ClienteUpdate400Example.json")))
                        .addExamples("ClienteUpdate404Example", new Example().value(leerJson("/examples/ClienteUpdate404Example.json")))
                        .addExamples("ListAllClientes200Example", new Example().value(leerJson("/examples/ListAllClientes200Example.json")))
                        //.addExamples("", new Example().value(leerJson("/examples/.json")))

        );
    }

    /**
     * Método ayudante que lee el contenido de un archivo JSON del proyecto
     * y lo convierte en un String para que Swagger lo entienda.
     */
    private String leerJson(String rutaArchivo) {
        try {
            ClassPathResource resource = new ClassPathResource(rutaArchivo);
            // Lee todos los bytes del archivo y los convierte a texto usando UTF-8
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Si te equivocas en el nombre del archivo, Swagger no se cae,
            // solo mostrará este mensaje de error en la interfaz.
            return "{\n  \"error\": \"No se pudo encontrar el archivo: " + rutaArchivo + "\"\n}";
        }
    }
}
