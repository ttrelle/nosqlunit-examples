package nosqlunit.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * A simple base class for MongoDB based repositories.
 * 
 * @author Tobias trelle
 */
public class MongoRepository {

	private DBCollection collection;
	
	public MongoRepository(DB database, String collection) {
		this.collection = database.getCollection(collection);
	}
	
	public List<DBObject> findAll() {
		return find("{}");
	}
	
	public List<DBObject> find(String criteria) {
		final List<DBObject> result = new ArrayList<DBObject>();
		final DBCursor crsr;
		
		crsr = collection.find( (DBObject)JSON.parse(criteria) );
		while ( crsr.hasNext() ) {
			result.add(crsr.next());
		}
		
		return result;
	}
	
	protected DBCollection getCollection() {
		return collection;
	}
	
}
