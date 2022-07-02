package com.game.common.messagedispatch;

import com.game.common.eventdispatch.ListenerHandler;
import com.game.common.model.anno.GameMessage;
import com.game.common.model.anno.HeaderAnno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个指令对应一个handler
 * @author zheng
 */
@Service
public class GameMessageDispatchService {
    private Map<String, List<ListenerHandler>> eventMapping = new HashMap<>();
    private Map<String, Map<String ,ListenerHandler>> nameEventMapping = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    private Map<String ,Class> eventMap = new HashMap<>();
    @PostConstruct
    public void init(){
        init(applicationContext);
    }
    public void init(ApplicationContext context){
        context.getBeansWithAnnotation(GameDispatchService.class).values().forEach(bean->{
            Method[] methods = bean.getClass().getMethods();
            String baseName = bean.getClass().getSimpleName();
            for (Method method : methods){
                GameMessageListener listener = method.getAnnotation(GameMessageListener.class);
                if (listener != null && listener.onUsed().equals("true")){
                    Class<? extends GameMessage> eventClass = listener.value();
                    HeaderAnno annotation = eventClass.getAnnotation(HeaderAnno.class);
                    if (annotation != null ){
                        ListenerHandler mapping = new ListenerHandler(bean,method);
                        addEventListenerMapping(String.valueOf(annotation.serviceId()),mapping,annotation.serviceId()+"-"+listener.name());
                        eventMap.put(eventClass.getName(),listener.value());
                    }


                }
            }
        });
    }
    private  ListenerHandler handler(GameMessage organ, String desc){
        Map<String, ListenerHandler> gameTypeMap = nameEventMapping.get(String.valueOf(organ.getHeader().getServiceId()));
        if (gameTypeMap == null){
            throw new RuntimeException("don't have correct response handler");
        }
        return gameTypeMap.get(organ.getHeader().getServiceId()+"-"+desc);

    }
    public void sendGameMessage(GameMessage gameMessage) throws InvocationTargetException, IllegalAccessException {
        sendGameMessage(gameMessage,gameMessage.getHeader().getDescribe());
    }
    public void sendGameMessage(GameMessage gameMessage, String desc) throws InvocationTargetException, IllegalAccessException {
        ListenerHandler handler = handler(gameMessage,desc);
        if (handler == null){
            throw new RuntimeException("cannot find the method with event name "+gameMessage+" method "+desc);
        }
        handler.getMethod().invoke(handler.getBean(),gameMessage);
    }
    /**
     * 添加注册事件
     * @param baseName
     * @param mapping
     * @param annotation
     */
    private void addEventListenerMapping(String baseName, ListenerHandler mapping, String extendName) {

        List<ListenerHandler> listenerMappings = eventMapping.get(baseName);

        if (listenerMappings == null){
            listenerMappings = new ArrayList<>();
            eventMapping.put(baseName,listenerMappings);
        }
        listenerMappings.add(mapping);
        Map<String, ListenerHandler> localMap;

        if (nameEventMapping.containsKey(baseName)) {
            localMap =nameEventMapping.get(baseName);
        }else {
            localMap = new HashMap<>();
        }
        if (localMap.containsKey(baseName+"-"+extendName)){
            throw new RuntimeException("命名重复");
        }
        localMap.put(extendName,mapping);
        nameEventMapping.put(baseName,localMap);
    }
}
