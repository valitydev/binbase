package dev.vality.binbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@ServletComponentScan
@SpringBootApplication
public class BinbaseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BinbaseApplication.class, args);

        String shutdownFlag = context.getBeanFactory().resolveEmbeddedValue("${batch.shutdown_after_execute}");
        boolean needShutdown = Boolean.parseBoolean(shutdownFlag);
        if (needShutdown) {
            int exitCode = SpringApplication.exit(context);
            System.exit(exitCode);
        }
    }
}
