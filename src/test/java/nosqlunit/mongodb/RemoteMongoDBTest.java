package nosqlunit.mongodb;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * Example test for NoSQLUnit for MongoDB using a remote mongod server.
 * 
 * @author Tobias Trelle
 */
public class RemoteMongoDBTest {

	private static final String DB_NAME = "test";
	
	@Rule // use already running "remote" instance
	public MongoDbRule mongoRule = new MongoDbRule(
			mongoDb().databaseName(DB_NAME).host("localhost").port(27017) .build()
	);	
	
	/** Unit under test. */
	private OrderRepository repository;		
	
	@Test
	@UsingDataSet(locations = "orders.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
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
