package com.game.waitstart;


import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.game.common.eventdispatch.EventAnnotationManager;
import com.game.common.eventdispatch.EventAnnotationManager;
import com.game.common.messagedispatch.GameDispatchService;
import com.game.common.messagedispatch.GameMessageDispatchService;
import com.game.waitstart.nameserver.RegisterGameService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
@EnableKafka
public class WaitStartApplicatioin {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder(WaitStartApplicatioin.class).run(args);
//
//        GameMessageDispatchService dispatchService = run.getBean(GameMessageDispatchService.class);
//        dispatchService.init(run);
        EventAnnotationManager manager = new EventAnnotationManager();
        manager.init(run);
        RegisterGameService service = run.getBean(RegisterGameService.class);
        service.registerService();

    }
}
