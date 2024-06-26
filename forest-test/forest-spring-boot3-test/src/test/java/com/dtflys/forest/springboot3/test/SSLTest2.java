package com.dtflys.forest.springboot3.test;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.logging.ForestLogger;
import com.dtflys.forest.springboot3.test.client2.GiteeClient;
import com.dtflys.forest.springboot3.test.ssl.MyHostnameVerifier;
import com.dtflys.forest.springboot3.test.ssl.MySSLSocketFactoryBuilder;
import com.dtflys.forest.ssl.SSLKeyStore;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("ssl2")
@SpringBootTest
@ContextConfiguration(classes = SSLTest2.class)
@EnableAutoConfiguration
public class SSLTest2 extends BaseSpringBootTest {

    @Resource
    private ForestConfiguration sslConfig;

    @Resource
    private GiteeClient giteeClient;


    @Test
    public void testConfiguration() {
        ForestLogger logger = Mockito.mock(ForestLogger.class);

        assertThat(sslConfig.getMaxConnections()).isEqualTo(300);
        assertThat(sslConfig.getMaxRouteConnections()).isEqualTo(300);
        assertThat(sslConfig.getTimeout()).isEqualTo(3000);
        assertThat(sslConfig.getConnectTimeout()).isEqualTo(3000);
        assertThat(sslConfig.getMaxRetryCount()).isEqualTo(2);
        assertThat(sslConfig.getSslKeyStores().size()).isEqualTo(1);

        SSLKeyStore sslKeyStore = sslConfig.getKeyStore("keystore1");
        assertThat(sslKeyStore).isNotNull();

//        assertEquals("keystore1", sslKeyStore.getId());
//        assertEquals("test.keystore", sslKeyStore.getFilePath());
//        assertEquals("123456", sslKeyStore.getKeystorePass());
//        assertEquals("123456", sslKeyStore.getCertPass());
//        assertEquals(1, sslKeyStore.getProtocols().length);
//        assertEquals("SSLv3", sslKeyStore.getProtocols()[0]);
        assertThat(sslKeyStore.getSslSocketFactoryBuilder()).isNotNull().isInstanceOf(MySSLSocketFactoryBuilder.class);
        assertThat(sslKeyStore.getHostnameVerifier()).isNotNull().isInstanceOf(MyHostnameVerifier.class);
        ForestRequest<String> request = giteeClient.index2();
        assertThat(request).isNotNull();
        request.getLogConfiguration().getLogHandler().setLogger(logger);
        String result = request.executeAsString();
        assertThat(result.startsWith("Global: ")).isTrue();
        Mockito.verify(logger).info("[Forest] [Test2] 请求: \n" +
                "\tGET https://gitee.com/dt_flys HTTPS\n" +
                "\t请求头: \n" +
                "\t\tUser-Agent: forest/dev");
        Throwable th = null;
        try {
            giteeClient.index3();
        } catch (ForestRuntimeException ex) {
            th = ex.getCause();
        }
        assertThat(th).isNotNull();
    }

}
