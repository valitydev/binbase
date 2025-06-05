package dev.vality.binbase.batch;

import dev.vality.binbase.config.PostgresqlTest;
import dev.vality.binbase.domain.CountryCode;
import dev.vality.damsel.binbase.*;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


//@TestPropertySource(properties = {"batch.file_path=classpath:/data/binbase/case1", "batch.strict_mode=false"})
@PostgresqlTest
@SpringBootTest(webEnvironment = RANDOM_PORT)
class BatchUploadTest {

    @LocalServerPort
    protected int port;

    private BinbaseSrv.Iface binbaseClient;

    private static Path tempDir;

    @BeforeAll
    static void init() throws Exception {
        tempDir = Files.createTempDirectory("binbase-case1");

        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:/data/binbase/case1/*.csv");

        for (Resource resource : resources) {
            Path dest = tempDir.resolve(resource.getFilename());
            Files.copy(resource.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("✅ Copied test CSVs to: " + tempDir.toAbsolutePath());
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("batch.file_path", () -> tempDir.toAbsolutePath().toString());
        registry.add("batch.strict_mode", () -> "false");
    }

    @BeforeEach
    public void setup() throws URISyntaxException {
        binbaseClient = new THSpawnClientBuilder()
                .withAddress(new URI("http://localhost:" + port + "/v1/binbase"))
                .withNetworkTimeout(0)
                .build(BinbaseSrv.Iface.class);
    }

    @Test
    void testLookupData() throws TException {
        ResponseData responseData = binbaseClient.lookup("100001", Reference.last(new Last()));

        assertEquals(3L, responseData.getVersion());
        assertEquals(CountryCode.US, CountryCode.getByAlpha3Code(responseData.getBinData().getIsoCountryCode()));
        assertEquals(CardType.credit, responseData.getBinData().getCardType());

        responseData = binbaseClient.lookup("100001", Reference.version(2L));

        assertEquals(2L, responseData.getVersion());
        assertEquals(CountryCode.US, CountryCode.getByAlpha3Code(responseData.getBinData().getIsoCountryCode()));
        assertEquals(CardType.credit_or_debit, responseData.getBinData().getCardType());

        responseData = binbaseClient.lookup("100001", Reference.version(1L));

        assertEquals(1L, responseData.getVersion());
        assertEquals(CountryCode.CA, CountryCode.getByAlpha3Code(responseData.getBinData().getIsoCountryCode()));
        assertNull(responseData.getBinData().getCardType());

        responseData = binbaseClient.lookup("100115", Reference.last(new Last()));

        assertEquals(1L, responseData.getVersion());
        assertNull(responseData.getBinData().getIsoCountryCode());

        responseData = binbaseClient.lookup("100117", Reference.last(new Last()));

        assertEquals(1L, responseData.getVersion());

        responseData = binbaseClient.lookup("430288", Reference.last(new Last()));

        assertEquals(1L, responseData.getVersion());
        assertEquals(CountryCode.AW, CountryCode.getByAlpha3Code(responseData.getBinData().getIsoCountryCode()));

        responseData = binbaseClient.lookup("4302885002", Reference.last(new Last()));
        assertEquals(4L, responseData.getVersion());
        assertEquals(CountryCode.AW, CountryCode.getByAlpha3Code(responseData.getBinData().getIsoCountryCode()));
    }

    @Test
    void testBinNotFound() {
        assertThrows(BinNotFound.class, () -> binbaseClient.lookup("999000", Reference.last(new Last())));
    }

    @Test
    void testWhenWhenPanInWrongFormat() {
        assertThrows(BinNotFound.class,
                () -> binbaseClient.lookup("999999#Q", Reference.last(new Last())));
        assertThrows(BinNotFound.class,
                () -> binbaseClient.lookup("99999", Reference.last(new Last())));
        assertThrows(BinNotFound.class,
                () -> binbaseClient.lookup("99999999999999999999999999999", Reference.last(new Last())));
    }

    @Test
    void testMaxValuePan() throws TException {
        ResponseData responseData = binbaseClient.lookup("9999999999999999999", Reference.last(new Last()));
        assertNotNull(responseData);
    }

}
