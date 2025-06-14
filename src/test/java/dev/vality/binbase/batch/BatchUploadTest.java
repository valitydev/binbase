package dev.vality.binbase.batch;

import dev.vality.binbase.config.PostgresqlTest;
import dev.vality.binbase.domain.CountryCode;
import dev.vality.damsel.binbase.BinNotFound;
import dev.vality.damsel.binbase.BinbaseSrv;
import dev.vality.damsel.binbase.CardType;
import dev.vality.damsel.binbase.Last;
import dev.vality.damsel.binbase.Reference;
import dev.vality.damsel.binbase.ResponseData;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@TestPropertySource(properties = {"batch.file_path=classpath:/data/binbase/case1", "batch.strict_mode=false"})
@PostgresqlTest
@SpringBootTest(webEnvironment = RANDOM_PORT)
class BatchUploadTest {

    @LocalServerPort
    protected int port;

    private BinbaseSrv.Iface binbaseClient;

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
