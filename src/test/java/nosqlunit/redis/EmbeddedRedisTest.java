package nosqlunit.redis;

import static com.lordofthejars.nosqlunit.redis.EmbeddedRedis.EmbeddedRedisRuleBuilder.newEmbeddedRedisRule;
import static com.lordofthejars.nosqlunit.redis.RedisRule.RedisRuleBuilder.newRedisRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
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
	
	/** Unit under test. */
	private KeyValueRepository repository;
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	@ShouldMatchDataSet(location="keyvalue.json")
	public void should_insert_string() {
		// given
		repository = new KeyValueRepository( getJedisInstance() );
		
		// when
		repository.setValue("hello", "redis");
		
		// then: should match data
	}
	
	@Test
	@UsingDataSet(locations="keyvalue.json" ,loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void should_find_string() {
		// given
		repository = new KeyValueRepository( getJedisInstance() );
		
		// when
		String value = repository.getValue("hello");
		
		// then
		assertThat( value, is("redis") );
	}

	
	private Jedis getJedisInstance() {
		return (Jedis)redisRule.getDatabaseOperation().connectionManager();
	}
	
}
