package nosqlunit.redis;

import static com.lordofthejars.nosqlunit.redis.ManagedRedis.ManagedRedisRuleBuilder.newManagedRedisRule;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.redis.ManagedRedis;

/**
 * This test demonstrate how to use NoSQLUnit for Redis in managed mode.
 * <p/>
 * Does not work on Windows OS.
 * 
 * @author Tobias Trelle
 */
public class ManagedRedisTest {

	private static final String REDIS_HOME = "/opt/redis-2.6";
	
	@ClassRule
    public static ManagedRedis redisRule = newManagedRedisRule().redisPath(REDIS_HOME).build();
	
	/** Unit under test. */
	private KeyValueRepository repository;
	
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
		return new Jedis("localhost");
	}	
	
}
