import com.firebase.client.*;

public class Database {
	
	public static void main(String args[]) {
		//MUCH BETTER
		System.out.println("Hi there!");
		
		//BEGIN FIREBASE SHENANIGANS
		Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
		
		//write
		database.setValue("Do you have data?  you'll love firebase");
		
		database.setValue("I do have data!");
		
		/*database.addValueEventListener(new ValueEventListener() {

		    @Override
		    public void onDataChange(DataSnapshot snap) {
		        System.out.println(snap.getName() + " -> " + snap.getValue());
		    }

		    @Override public void onCancelled() { }
		});*/
	}
}
