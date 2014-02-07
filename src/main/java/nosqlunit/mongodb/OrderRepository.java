package nosqlunit.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * Simple example for a business-like MongoDB repository.
 * 
 * @author Tobias Trelle
 */
public class OrderRepository extends MongoRepository {
	
	public OrderRepository(DB database, String collection) {
		super(database, collection);
	}
	
	public void insert(int type, String description) {
		DBObject order = new BasicDBObject();
		
		order.put("type", type);
		order.put("desc", description);
		
		getCollection().insert(order);
	}
	
}
