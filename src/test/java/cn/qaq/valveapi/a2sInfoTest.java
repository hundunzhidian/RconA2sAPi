package cn.qaq.valveapi;

import cn.qaq.valveapi.utils.UdpServer;
import cn.qaq.valveapi.utils.UdpTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @program: valveapi
 * @description:
 * @author: QAQ
 * @create: 2021-06-19 14:01
 **/
@Slf4j
public class a2sInfoTest {

    @SneakyThrows
    @Test
    public void t1()
    {
        ObjectMapper objectMapper=new ObjectMapper();
        log.debug("服务器数据:{}",objectMapper.writeValueAsString(UdpServer.getServers("?????????")));
    }
}
