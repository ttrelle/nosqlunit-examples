package nosqlunit.mongodb;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * Example test for NoSQLUnit for MongoDB using embedded mode (based on the Fongo mock implementation).
 * 
 * @author Tobias Trelle
 */
public class EmbeddedMongoDBTest {

	protected static final String DB_NAME = "test";
	
	// Manage the mongod instance
	@ClassRule
	public static InMemoryMongoDb mongod = newInMemoryMongoDbRule().build();	
	
	// Manage connection
	@Rule
	public MongoDbRule mongoRule = newMongoDbRule().defaultEmbeddedMongoDb(DB_NAME);
	
	/** Unit under test. */
	private OrderRepository repo;
	
	@Before public void setUp() {
		repo = new OrderRepository(getDatabase(), "orders");
	}
	
	@Test
	@UsingDataSet(locations = "orders.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void should_find_all_orders() {
		// given: data set orders.json
		
		// when
		List<DBObject> orders = repo.findAll();
		
		// then
		assertThat( orders, notNullValue() );
		assertThat( orders.size(), is(2) );
	}

	@Test
	@UsingDataSet(locations = "orders.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	@ShouldMatchDataSet(location = "orders_updated.json")
	public void should_update_one_order() {
		// given: data set orders.json
		
		// when
		int updated = repo.update("{type: 42}", "{$set: {desc: \"something else\"}}");
		
		// then
		assertThat( updated, is(1) );
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	@ShouldMatchDataSet(location = "orders.json")
	public void should_insert_orders() {
		// given: empty collection
		
		// when
		repo.insert(4711, "1st order");
		repo.insert(42, "2nd order");
		
		// then: should match data
	}
	
	private DB getDatabase() {
		// uses old Mongo driver API
		return mongoRule.getDatabaseOperation().connectionManager().getDB(DB_NAME);
	}
	
}
