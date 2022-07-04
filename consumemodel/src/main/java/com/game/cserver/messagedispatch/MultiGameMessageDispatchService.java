package com.game.cserver.messagedispatch;

import com.game.common.annotation.BandKeyAndType;
import com.game.common.util.TopicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个指令对应多个handler
 * 但是一个玩法只要只有一个对应的处理类
 * 每个玩法的处理类可能相同，可能不相同
 * 所以根据匹配的id获得对应的处理累
 * @author zheng
 */
@Service
public class MultiGameMessageDispatchService {
    private Map<String, BaseHandler> eventMapping = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;
    private Map<String ,Class> eventMap = new HashMap<>();
    @PostConstruct
    public void init(){
        init(applicationContext);
    }
    public void init(ApplicationContext context){
        context.getBeansOfType(BaseHandler.class).values().forEach(bean->{
            BandKeyAndType annotation = bean.getClass().getAnnotation(BandKeyAndType.class);
            if (annotation != null){
                eventMapping.put(TopicUtil.generateTopic(annotation.serviceId(),annotation.gametype()),bean);
            }
        });
    }
    public BaseHandler getByServiceIdAndGameType(Integer serviceId, Integer gameType){
        return eventMapping.get(TopicUtil.generateTopic(serviceId,gameType));
    }

    /**
     * 添加注册事件
     * @param baseName
     * @param mapping
     * @param annotation
     */

}
