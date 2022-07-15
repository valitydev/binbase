package dev.vality.binbase.config;

import dev.vality.cds.storage.StorageSrv;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CardStorageConfig {

    @Value("${client.cds.url}")
    private Resource cdsUrl;

    @Value("${client.cds.timeout}")
    private int cdsTimeout;

    @Bean
    public StorageSrv.Iface cardStorageSrv() throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(cdsUrl.getURI())
                .withNetworkTimeout(cdsTimeout)
                .build(StorageSrv.Iface.class);
    }
}
