package webtool;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableScheduling
@PropertySource("classpath:system-web.properties")
public class MvcConfiguration implements WebMvcConfigurer {
	
	@Autowired
	InetConfig machine;
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
////		registry.addInterceptor(logInterceptor);
//	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	    System.setProperty("machine.hostname", machine.getHostname());
	}
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getResolver() throws IOException {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		// no limit
		resolver.setMaxUploadSize(-1);
		return resolver;
	}

//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
		
	}

//	@Override
//	public void addFormatters(FormatterRegistry arg0) {
//		// TODO Auto-generated method stub		
//	}

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	    registry.addResourceHandler("swagger-ui.html")
//	      .addResourceLocations("classpath:/META-INF/resources/");
//
//	    registry.addResourceHandler("/webjars/**")
//	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
//	}
//
//	@Override
//	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    //registry.addViewController("/").setViewName("forward:/react/index.html");
		//registry.addViewController("/").setViewName("main");
		registry.addViewController("/").setViewName("staff");
	}

//	@Override
//	public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void configurePathMatch(PathMatchConfigurer arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public MessageCodesResolver getMessageCodesResolver() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Validator getValidator() {
//		// TODO Auto-generated method stub
//		return null;
//	}


}
