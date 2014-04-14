package nosqlunit.mongodb;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * Example test for NoSQLUnit for MongoDB using managed mode (spawning a single mongod process).
 * 
 * @author Tobias Trelle
 */
public class ManagedMongoDBTest {

	private static final String MONGODB_HOME = System.getProperty("mongodb.home", "/opt/mongodb-2.4.8");
	
	private static final String DB_NAME = "test";
	
	@ClassRule // Manage the mongod instance
	public static ManagedMongoDb mongod = newManagedMongoDbRule().mongodPath(MONGODB_HOME).build();	
	
	@Rule // Manage connection
	public MongoDbRule mongoRule = newMongoDbRule().defaultManagedMongoDb(DB_NAME);
	
	/** Unit under test. */
	private OrderRepository repository;	
	
	@Test
	@UsingDataSet(locations = "orders.json", loadStrategy = LoadStrategyEnum.INSERT)
	public void should_find_all_orders() {
		// given
		repository = createOrderRepository();
		
		// when
		List<DBObject> orders = repository.findAll();
		
		// then
		assertThat(orders, notNullValue());
		assertThat(orders.size(), is(2));
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	@ShouldMatchDataSet(location = "orders.json")
	public void should_insert_orders() {
		// given
		repository = createOrderRepository();
		
		// when
		repository.insert(4711, "1st order");
		repository.insert(42, "2nd order");
		
		// then: should match data
	}
	
	private OrderRepository createOrderRepository() {
		return new OrderRepository(getDatabase(), "orders");	
	}
	
	private DB getDatabase() {
		// uses old Mongo driver API
		return mongoRule.getDatabaseOperation().connectionManager().getDB(DB_NAME);
	}
	
}
