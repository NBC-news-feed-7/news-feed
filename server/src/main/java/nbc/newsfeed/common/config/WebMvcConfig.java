package nbc.newsfeed.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 8.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	// 업로드된 파일이 GET 요청으로 잘 뿌려지도록
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/uploads/**")
			.addResourceLocations("file:uploads/");
	}
}
