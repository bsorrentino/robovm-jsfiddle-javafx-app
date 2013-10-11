package application;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.cocoatouch.foundation.NSURL;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIPasteboard;

import screensframework.ControlledScreen;
import screensframework.ScreensController;
import application.notification.NotificationListener;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class MainController implements Initializable, ControlledScreen {
	final java.net.URL fxUrl = getClass().getClassLoader().getResource("application/DefaultListCell.fxml");

	@FXML private ListView<JSONObject> fxList;
	@FXML private TextArea fxCode;
	@FXML private ProgressIndicator fxProgress;
	@FXML private Button fxDropbox;
	@FXML private Label fxDropboxStatus;
	
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
		
		Main.notificationCenter.addNotificationListener( new NotificationListener<String>() {

			@Override
			public void onNotification(String event) {
				fxCode.setText(event);
				fxCode.setDisable(false);			
			}
		});
		
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
		   			
		    
		    final Service<Void> loadFiddleService = new Service<Void>() {
		        @Override
		        protected Task<Void> createTask() {
		          return new Task<Void>() {
		            @Override
		            protected Void call() throws Exception {

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
		    				
			            	updateProgress(i + 1, result.length());

			            	items.add( item );
		    			}
		            	
		              
		              return null;
		            }
		          };
		        }
		      };

		      //final Stage progressDialog = FXUtils.createProgressDialog(loadFiddleService, this.parentController.getPrimaryStage());
		      
		      //progressDialog.show();
		      //loadFiddleService.reset();
		      loadFiddleService.start();

		      
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	@FXML protected void onPasteFromClipboard( ActionEvent event ) {

		final String code = UIPasteboard.getGeneral().getString();
    	
    	System.err.printf( "get code [%s]\n", code) ;
    	
    	Main.notificationCenter.fire( code );
		
	}

	private DbxWebAuthNoRedirect webAuth = null;
	
	@FXML protected void onDropBoxOpen( ActionEvent event ) {
		
        final String APP_KEY = "0o92htvhe8gleu9";
        final String APP_SECRET = "dcyi9flowhn4zkh";

        final DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        final DbxRequestConfig config = new DbxRequestConfig(
            "cordova-poc/1.0", Locale.getDefault().toString());
		
		if( webAuth==null ) {
	        
	        webAuth = new DbxWebAuthNoRedirect(config, appInfo);		
	
	        System.err.println("start dropbox autentication");
	        
	        final String authorizeUrl = webAuth.start();
	        
	        System.err.println("1. Go to: " + authorizeUrl);
	        System.err.println("2. Click Allow (you might have to log in first)");
	        System.err.println("3. Copy the authorization code.");
	        //String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	
	        // --> START COCOACODE        
			UIApplication.getSharedApplication().openURL( new NSURL(authorizeUrl));
			// <-- END COCOACODE
		}
		else {
			
			fxProgress.setVisible(true);
			
			final Task<DbxClient> connectToDropboxTask = new Task<DbxClient>() {
				

				@Override
				protected DbxClient call() throws Exception {
					final String code = fxCode.getText().trim();
					
					System.err.printf( "get code [%s]\n", code) ;

					updateMessage("finalize authentcation");
					
					final DbxAuthFinish authFinish = webAuth.finish( code );

					updateMessage("connecting to dropbox");
					
					final DbxClient client = new DbxClient(config, authFinish.accessToken);
					
					updateMessage("connected to dropbox");
					
					System.err.println("Linked account: " + client.getAccountInfo().displayName);
					
					return client;
				}
				
			};
			
			try {
				
				final Service<DbxClient> connectToDropboxService = new Service<DbxClient>() {

					@Override
					protected Task<DbxClient> createTask() {
						
						
						return connectToDropboxTask ;
					}
					
				};
				
				fxDropbox.disableProperty().bind(connectToDropboxService.runningProperty());
				fxProgress.visibleProperty().bind(connectToDropboxService.runningProperty());
			    fxDropboxStatus.textProperty().bind(Bindings.format("%s", connectToDropboxService.stateProperty()));

			    connectToDropboxService.start();
				
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
			
		}
	}
	
	
	private ScreensController parentController;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		parentController = screenPage;
		
	}

}
