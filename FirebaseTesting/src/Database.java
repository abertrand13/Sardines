import com.firebase.client.*;
import java.util.Map;
import java.util.HashMap;

public class Database {
	
	public static void main(String args[]) {
		//MUCH BETTER
		System.out.println("Hi there!");
		
		//BEGIN FIREBASE SHENANIGANS
		Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
		
		//write
		database.setValue("Do you have data?  you'll love firebase");
		
		database.setValue("Data Table");
		database.child("String1").setValue("This is String1");
		database.child("String2").setValue("This is String2");
		
		Map<String, Object> mappy = new HashMap<String, Object>();
		mappy.put("Map from one thing", "to another!");
		mappy.put("Vadim", "Khayms");
		database.child("Map").setValue(mappy);
		
		database.child("How").child("far").child("can").child("I").child("go?").setValue("Far!");
		
		//SAMPLE PLAYER DATABASE
		Firebase gameRef = database.child("Game ID UE45H8A");
		gameRef.setValue("SHITZ");
		
		Firebase game2Ref = database.child("Game ID R45THG8");
		game2Ref.setValue("MOAR SHITZ");
		
		game2Ref.child("player1").setValue("Test."); //'folders' can't have values
		game2Ref.child("player1").child("name").setValue("LIL JON");
		game2Ref.child("player1").child("lat").setValue("2");
		game2Ref.child("player1").child("long").setValue("3");
		game2Ref.child("player1").child("ready").setValue("FUCK YEA");
		
		System.out.println(game2Ref.child("player1").getName());
		System.out.println(game2Ref.child("player1").child("name"));
		
		//READING DATA (CONSTANTLY)
		/*database.addValueEventListener(new ValueEventListener() {

		    @Override
		    public void onDataChange(DataSnapshot snap) {
		        //System.out.println(snap.getName() + " -> " + snap.getValue());
		    	//System.out.println("");
		    }

		    @Override
		    public void onCancelled() {
		    	System.out.println("Listener was cancelled");
		    }
		});*/
		
		/*database.addChildEventListener(new ChildEventListener() {
		    @Override
		    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
		        //String userName = snapshot.getName();
		        //GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
		        //Map<String, Object> userData = snapshot.getValue(t);
		        //System.out.println("User " + userName + " has entered the chat");
		    	System.out.println("woop woop!");
		    }

		    @Override
		    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

		    }

		    @Override
		    public void onChildRemoved(DataSnapshot snapshot) {

		    }

		    @Override
		    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

		    }

		    @Override
		    public void onCancelled() {

		    }
		});*/
	}
}
