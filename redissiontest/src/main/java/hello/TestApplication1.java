package hello;



import com.game.common.relation.room.Room;
import hello.redisson.RedissonConfig;
import org.redisson.api.RLocalCachedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;



/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = {"hello","model"})
public class TestApplication1 {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestApplication1.class, args);
//        RLocalCachedMap<String ,Object> map = context.getBean(RLocalCachedMap.class);
        RedissonConfig redissonConfig = context.getBean(RedissonConfig.class);
        RLocalCachedMap<String, Room> map = redissonConfig.cachedMap();
        Room temp = map.get("test");
        if (temp != null){
            System.out.println("temp room id "+temp.getRoomNum());
        }
        Room t = new Room();
        t.setRoomNum(1234);
        map.put("test",t);
        t.setRoomNum(2324);
        System.out.println(map.get("test").getRoomNum());
    }
}
