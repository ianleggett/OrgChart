package webtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = { "webtool", "webtool.service", "webtool.utils" })
@EnableAutoConfiguration
@EntityScan(basePackages = { "webtool.pojo", "webtool.utils" })
@EnableJpaRepositories(basePackages = { "webtool.repository" })
@EnableOpenApi // Enable open api 3.0.3 spec
//@Import({ SecurityConfiguration.class })
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@ImportResource("classpath:application-context.xml")
@PropertySources({ @PropertySource("classpath:exch-config.properties"),@PropertySource("classpath:sys-config.properties") })
public class Application extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
    
	@Bean
	public Docket api() {                
	    return new Docket(DocumentationType.OAS_30)          
	      .select()                                       
	      .apis(RequestHandlerSelectors.basePackage("webtool.jwt"))
	      //.paths(PathSelectors.ant("/*"))                     
	      .build();
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}
