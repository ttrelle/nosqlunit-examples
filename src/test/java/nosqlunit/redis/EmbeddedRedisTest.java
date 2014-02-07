package nosqlunit.redis;

import static com.lordofthejars.nosqlunit.redis.EmbeddedRedis.EmbeddedRedisRuleBuilder.newEmbeddedRedisRule;
import static com.lordofthejars.nosqlunit.redis.RedisRule.RedisRuleBuilder.newRedisRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.redis.EmbeddedRedis;
import com.lordofthejars.nosqlunit.redis.RedisRule;

/**
 * This test demonstrate how to use NoSQLUnit for Redis in embedded mode.
 * 
 * @author Tobias Trelle
 */
public class EmbeddedRedisTest {

	@ClassRule
    public static EmbeddedRedis embeddedRedis = newEmbeddedRedisRule().build();
	
	@Rule
	public RedisRule redisRule = newRedisRule().defaultEmbeddedRedis();
	
	@Test
	@UsingDataSet(locations="data.json" ,loadStrategy = LoadStrategyEnum.INSERT)
	public void should_find_string() {
		Jedis jedis = new Jedis("localhost");
		String value;
		
		value = jedis.get("hello");
		
		assertThat( value, is("redis") );
	}
	
}
