package com.spirit.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main entry point for the Spirit application.
 * <p>This class bootstraps the Spring Boot application, configures the Vaadin theme, and sets up the
 * application shell settings.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @SpringBootApplication}: Marks this class as a Spring Boot application.</li>
 *   <li>{@code @Theme}: Specifies the theme for Vaadin components.</li>
 *   <li>{@code @Push}: Enables server push for real-time updates with automatic mode.</li>
 * </ul>
 */
@SpringBootApplication
@Theme(value = "spirit")
@Push(PushMode.AUTOMATIC)
public class SpiritApplication extends SpringBootServletInitializer implements AppShellConfigurator {

    /**
     * Main method to launch the Spring Boot application.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(SpiritApplication.class, args);
    }

    /**
     * Configures the application when deployed as a traditional WAR file.
     * @param application the {@link SpringApplicationBuilder} to configure.
     * @return the configured {@link SpringApplicationBuilder}.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpiritApplication.class);
    }

    /**
     * Configures the application shell settings, such as meta tags and the page title.
     * <p>Specifies a responsive viewport and sets the page title to "Spirit".</p>
     * @param settings the {@link AppShellSettings} to configure.
     */
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addMetaTag("viewport", "width=device-width, initial-scale=1, maximum-scale=1");
        settings.setPageTitle("Spirit");
    }
}
