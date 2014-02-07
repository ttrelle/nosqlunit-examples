package nosqlunit.redis;

import static com.lordofthejars.nosqlunit.redis.RemoteRedisConfigurationBuilder.newRemoteRedisConfiguration;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.redis.RedisRule;

/**
 * This test demonstrate how to use NoSQLUnit for Redis using a remote redis server.
 * 
 * @author Tobias Trelle
 */
public class RemoteRedisTest {

	@ClassRule
    public static RedisRule redisRule = new RedisRule(newRemoteRedisConfiguration().host("localhost").port(6379).build());
	
	@Test
	@UsingDataSet(locations="keyvalue.json" ,loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void should_find_string() {
		// given
		KeyValueRepository repo = new KeyValueRepository( getJedisInstance() );
		
		// when
		String value = repo.getValue("hello");
		
		// then
		assertThat( value, is("redis") );
	}

	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	@ShouldMatchDataSet(location="keyvalue.json")
	public void should_insert_string() {
		// given
		KeyValueRepository repo = new KeyValueRepository( getJedisInstance() );
		
		// when
		repo.setValue("hello", "redis");
		
		// then: should match data
	}
	
	private Jedis getJedisInstance() {
		return (Jedis)redisRule.getDatabaseOperation().connectionManager();
	}
	
}
