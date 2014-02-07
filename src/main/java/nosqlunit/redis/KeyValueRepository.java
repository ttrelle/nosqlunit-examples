package nosqlunit.redis;

import redis.clients.jedis.Jedis;

/**
 * Simple repository abstraction.
 * 
 * @author Tobias Trelle
 */
public class KeyValueRepository {

	private Jedis jedis;
	
	public KeyValueRepository(Jedis jedis) {
		this.jedis = jedis;
	}
	
	public void setValue(String key, String value) {
		jedis.set(key, value);
	}
	
	public String getValue(String key) {
		return jedis.get(key);
	}
	
}
