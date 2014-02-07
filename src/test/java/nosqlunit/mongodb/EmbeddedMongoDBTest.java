package nosqlunit.mongodb;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
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

	private static final String DB_NAME = "test";
	
	// Manage the mongod instance
	@ClassRule
	public static InMemoryMongoDb inMemoryMongoDb = newInMemoryMongoDbRule().build();	
	
	// Manage connection
	@Rule
	public MongoDbRule embeddedMongoDbRule = newMongoDbRule().defaultEmbeddedMongoDb(DB_NAME);
	
	@Test
	@UsingDataSet(locations = "orders.json", loadStrategy = LoadStrategyEnum.INSERT)
	public void should_find_all_orders() {
		// given
		OrderRepository repo = createOrderRepository();
		
		// when
		List<DBObject> orders = repo.findAll();
		
		// then
		assertThat(orders, notNullValue());
		assertThat(orders.size(), is(2));
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	@ShouldMatchDataSet(location = "orders.json")
	public void should_insert_orders() {
		// given
		OrderRepository repo = createOrderRepository();
		
		// when
		repo.insert(4711, "1st order");
		repo.insert(42, "2nd order");
		
		// then: should match data
	}
	
	private OrderRepository createOrderRepository() {
		return new OrderRepository(getDatabase(), "orders");	
	}
	
	private DB getDatabase() {
		// uses old Mongo driver API
		return embeddedMongoDbRule.getDatabaseOperation().connectionManager().getDB(DB_NAME);
	}
	
}
