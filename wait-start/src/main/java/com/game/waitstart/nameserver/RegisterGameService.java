package com.game.waitstart.nameserver;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.game.common.constant.InfoConstant;
import com.game.waitstart.config.WaitStartConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zheng
 */
@Component
public class RegisterGameService {
    @NacosInjected
    private NamingService namingService;
    @Resource
    private WaitStartConfig waitStartConfig;
    public void registerService(){
        Instance instance = new Instance();
//        instance.setInstanceId(String.valueOf(waitStartConfig.getServerId()));
        instance.setPort(waitStartConfig.getPort());
        instance.setServiceName(waitStartConfig.getName());
        instance.setClusterName(waitStartConfig.getClusterName());
        instance.setEphemeral(true);
        instance.getMetadata().put(InfoConstant.SERVER_ID,String.valueOf(waitStartConfig.getServerId()));
        instance.getMetadata().put(InfoConstant.MODULE_ID,String.valueOf(waitStartConfig.getModuleId()));

        instance.setIp(waitStartConfig.getHost());
        try {
            namingService.registerInstance(waitStartConfig.getClusterName(),instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
