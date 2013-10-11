package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.cocoatouch.foundation.NSArray;
import org.robovm.cocoatouch.foundation.NSURL;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIWindow;

import screensframework.ControlledScreen;
import screensframework.ScreensController;

public class MainController implements Initializable, ControlledScreen {
	final java.net.URL fxUrl = getClass().getClassLoader().getResource("application/DefaultListCell.fxml");

	@FXML private ListView<JSONObject> fxList;
	
	
	class ItemCell extends ListCell<JSONObject> {

		AnchorPane cellPane;
		Label title;
		Label subTitle;
		
		public ItemCell() {
			super();
			try {
				cellPane = FXMLLoader.load(fxUrl);

				title = FXUtils.getChildByID(cellPane, "title");
				subTitle = FXUtils.getChildByID(cellPane, "subtitle");
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}

		@Override
		protected void updateItem(JSONObject value, boolean empty) {
			super.updateItem(value, empty);
		
			if( value!=null ) {
				cellPane.setOpacity(1);
				try {
					title.setText( value.getString("title") );
					subTitle.setText( value.getString("framework") );
				} catch (JSONException e) {
					e.printStackTrace(System.err);
				}
			}
			else {
				cellPane.setOpacity(0);
			}
			setGraphic(cellPane);
		}

		@Override
		public void updateIndex(int index) {
			super.updateIndex(index);
					
		}
		
	};
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			
		final ObservableList<JSONObject> items = FXCollections.observableArrayList (); // ("Single", "Double", "Suite", "Family App");
		fxList.setItems(items);
		
		
		try {

		    fxList.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>() {
		        @Override
		        public ListCell<JSONObject> call(ListView<JSONObject> param) {
		            return new ItemCell();
		        }
		    });
			
		    fxList.getSelectionModel().selectedItemProperty().addListener(
		              new ChangeListener<JSONObject>() {
		                  public void changed(ObservableValue<? extends JSONObject> ov, 
		                		  JSONObject old_val, JSONObject new_val) {
		                	  System.err.println( "select item");
		                	  
		                	  //parentController.setScreen( Main.SCREEN_FIDDLE_PREVIEW );
		                	  
		              		// --> START COCOACODE
  
		                	  try {
								UIApplication.getSharedApplication().openURL( new NSURL(new_val.getString("url").concat("show")));
							} catch (JSONException e) {
								e.printStackTrace(System.err);
							}
		              		// <-- END COCOACODE
  
		              }
		          });
		   			
			final java.net.URL jsFiddleUrl = new java.net.URL("http://jsfiddle.net/api/user/bsorrentino/demo/list.json");
			
			final java.io.InputStream is = jsFiddleUrl.openStream();
			
			final StringBuilder sb = new StringBuilder();
			
			int c ;
			while(  (c= is.read())!= -1 ) {
				sb.append((char)c);			
			}
			

			System.err.println( );
			System.err.println( sb.toString() );
			System.err.println( );
			
			final JSONArray result = new JSONArray(sb.toString());

			System.err.println( );
			System.err.println( result.toString(4) );
			System.err.println( );
			
			for( int i=0; i < result.length(); ++i ) {
				
				final JSONObject item = (JSONObject) result.get(i);
				
				items.add( item );
			}
			

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private ScreensController parentController;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		parentController = screenPage;
		
	}

}
