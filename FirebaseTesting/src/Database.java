import com.firebase.client.*;

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
		
		/*database.addValueEventListener(new ValueEventListener() {

		    @Override
		    public void onDataChange(DataSnapshot snap) {
		        System.out.println(snap.getName() + " -> " + snap.getValue());
		    }

		    @Override public void onCancelled() { }
		});*/
	}
}
