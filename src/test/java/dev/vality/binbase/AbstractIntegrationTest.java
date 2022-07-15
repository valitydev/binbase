package dev.vality.binbase;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractIntegrationTest {

    //    @ClassRule
//    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:9.6.8");
//
    @LocalServerPort
    protected int port;
//
//    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        @Override
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                    "spring.datasource.url=" + postgres.getJdbcUrl(),
//                    "spring.datasource.username=" + postgres.getUsername(),
//                    "spring.datasource.password=" + postgres.getPassword(),
//                    "flyway.url=" + postgres.getJdbcUrl(),
//                    "flyway.user=" + postgres.getUsername(),
//                    "flyway.password=" + postgres.getPassword(),
//                    "batch.strict_mode=false")
//                    .and(configurableApplicationContext.getEnvironment().getActiveProfiles())
//                    .applyTo(configurableApplicationContext);
//        }
//    }

}
