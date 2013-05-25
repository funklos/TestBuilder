package uibuilder;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.gdata.Escaper;

import helpers.OptionsArrayAdapter;
import helpers.OptionsHolder;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class PreferencesActivity extends Activity implements OnItemClickListener, OnClickListener
{
	private ViewFlipper flipper;
	private ListView optionsList;
	private OptionsHolder[] optionListItems;
	
	//Sign up
	private TextView cloudAccountStatus;
	private EditText signUpUserName, signUpUserPass, signUpUserMail;
	private Button signUp, signIn, signOut, signInExisting;
	
	//Sign in
	private EditText signInUserName, signInUserPass;

	
	private OptionsArrayAdapter optionsListAdapter;
	
	private Animation
	slide_top_in,
	slide_bottom_in,
	slide_top_out,
	slide_bottom_out,
	slide_left_in,
	slide_right_in,
	slide_left_out,
	slide_right_out
	;
	
	private boolean signedIn = false;

	@Override
	public void onBackPressed()
	{
		if (activeListItemPos == 0)
		{
			super.onBackPressed();
			overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out);
		}
		else
		{
			flipper.setInAnimation(slide_bottom_out);
			flipper.setOutAnimation(slide_top_in);
			
			if (!signedIn)
			{
				Log.d("back check", "not signed in");
				flipper.setDisplayedChild(SHOW_SIGN_UP);
			}
			else
			{
				Log.d("back check", "signed in");
				flipper.setDisplayedChild(SHOW_SIGNED_IN);
			}
			
			optionsList.getChildAt(0).setActivated(true);
			optionsList.setItemChecked(0, true);
			activeListItemPos = 0;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.preferences_root);
		
		Intent startIntent = getIntent();
			
		setupMainUI();
		setupOptionsPage();
		
		setupCloudAccountPage();
		
		setupActionBar();
		
		checkExistingAccount();
	}

	
	private void checkExistingAccount()
	{
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) 
		{
		  // do stuff with the user
			signedIn = true;
			flipper.setDisplayedChild(SHOW_SIGNED_IN);
			cloudAccountStatus.setText(currentUser.getUsername());
		} 
		else 	
		{
		  // show the signup or login screen
			flipper.setDisplayedChild(SHOW_SIGN_UP);
		}
		
	}
	
	private void setupCloudAccountPage()
	{
		cloudAccountStatus = (TextView) findViewById(R.id.preferences_cloud_account_status);
		
		signUpUserName = (EditText) findViewById(R.id.preferences_cloud_account_signup_username);
		signUpUserMail = (EditText) findViewById(R.id.preferences_cloud_account_signup_usermail);
		signUpUserPass = (EditText) findViewById(R.id.preferences_cloud_account_signup_userpassword);
		
		signUp = (Button) findViewById(R.id.preferences_cloud_account_signup_signup_request);
		signUp.setOnClickListener(this);
		
		signIn = (Button) findViewById(R.id.preferences_cloud_account_signin_signin_request);
		signIn.setOnClickListener(this);
		
		signOut = (Button) findViewById(R.id.preferences_cloud_account_signedin_signout_request);
		signOut.setOnClickListener(this);
		
		signInExisting = (Button) findViewById(R.id.preferences_cloud_account_signup_signin_request);
		signInExisting.setOnClickListener(this);
		
		signInUserName = (EditText) findViewById(R.id.preferences_cloud_account_signin_username);
		signInUserPass = (EditText) findViewById(R.id.preferences_cloud_account_signin_userpassword);
	}
	
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		setupAnimations();
		
		optionsList.setItemChecked(0, true);
		
		//optionsList.getChildAt(0).setActivated(true);
		activeListItemPos = 0;
	}

	
	private void setupOptionsPage()
	{
		optionsList = (ListView) findViewById(R.id.project_edit_optionsscreen_optionslist);
		
		String[] optionsListNames = getResources().getStringArray(R.array.prime_preferences_names);
		String[] optionsListDescs = getResources().getStringArray(R.array.prime_preferences_descriptions);
		
		optionListItems = OptionsHolder.getOptions(optionsListNames, optionsListDescs);
		optionsListAdapter = new OptionsArrayAdapter(getApplicationContext(), 0, optionListItems);
		
		optionsList.setAdapter(optionsListAdapter);
		optionsList.setOnItemClickListener(this);
		
	}
	
	private static final int SHOW_SIGN_UP = 0, SHOW_SIGNED_IN = 3, SHOW_SIGN_IN = 2;

	private void setupMainUI()
	{
		flipper = (ViewFlipper) findViewById(R.id.project_edit_flipper);
		flipper.addView(getLayoutInflater().inflate(R.layout.activity_preferences_cloud_account_signup, null));
		flipper.addView(getLayoutInflater().inflate(R.layout.activity_preferences_cloud_settings, null));
		
		//additional, not reachable via item click mapping
		flipper.addView(getLayoutInflater().inflate(R.layout.activity_preferences_cloud_account_signin, null));
		flipper.addView(getLayoutInflater().inflate(R.layout.activity_preferences_cloud_account_signedin, null));
	}
	
	/**
	 * customize actionbar to match the overall ui-style of the app
	 */
	private void setupActionBar()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
		bar.setSubtitle("");
		bar.setTitle("Prime Preferences");
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}
	
	/**
	 * 
	 */
	private void setupAnimations()
	{
		slide_top_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_top_in);
        slide_bottom_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_bottom_in);
        slide_top_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_top_out);
        slide_bottom_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_bottom_out);
        
        slide_left_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_left_in);
    	slide_right_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_right_in);
    	slide_left_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_left_out);
    	slide_right_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_right_out);
	}


	
	private int activeListItemPos;

	@Override
	public void onItemClick(AdapterView<?> list, View item, int id, long arg3)
	{
		// TODO Auto-generated method stub
		Log.d("edit list item is", String.valueOf(id));
		
		switch (list.getId())
		{
		case R.id.project_edit_optionsscreen_optionslist:
			
			if (id != activeListItemPos)
			{
				if (id > activeListItemPos)
				{
					setFlipperMovement(MOVE_FORWARD);
				}
				else
				{
					setFlipperMovement(MOVE_BACK);
				}
				
				if(signedIn && id == 0)
				{
					flipper.setDisplayedChild(SHOW_SIGNED_IN);
				}
				else
				flipper.setDisplayedChild(id);
				
				//optionsList.setItemChecked(activeListItemPos, false);
				optionsList.setItemChecked(id, true);
				
				//optionsList.getChildAt(activeListItemPos).setActivated(false);
				item.setActivated(true);
				activeListItemPos = id;
			}
			break;

			
		default:
			break;
		}
	}

	private static final int MOVE_BACK = 0X00, MOVE_FORWARD = 0X01, MOVE_DOWN = 0X02, MOVE_UP = 0X03;
	
	private void setFlipperMovement(int direction)
	{
		switch (direction)
		{
		case MOVE_BACK:
			
			flipper.setInAnimation(slide_top_in);
			flipper.setOutAnimation(slide_bottom_out);
			break;

		case MOVE_FORWARD:
			
			flipper.setInAnimation(slide_bottom_in);
			flipper.setOutAnimation(slide_top_out);
			break;
			
		case MOVE_DOWN:
			
			flipper.setInAnimation(slide_right_in);
			flipper.setOutAnimation(slide_left_out);
			break;
			
		case MOVE_UP:
			
			flipper.setInAnimation(slide_left_in);
			flipper.setOutAnimation(slide_right_out);
			break;

		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.preferences_cloud_account_signup_signup_request:
			
			String name = signUpUserName.getText().toString();
			String pass = signUpUserPass.getText().toString();
			String mail = signUpUserMail.getText().toString();
			
			if (name.length() >= 4 && pass.length() >= 4 && mail.length() >= 8)
			{
				Log.d("signup", "requested");
				
				ParseUser user = new ParseUser();
				user.setUsername(name);
				user.setPassword(pass);
				user.setEmail(mail);
				
				user.signUpInBackground(new SignUpCallback() {
					  public void done(ParseException e) {
					    if (e == null) 
					    {
					      // Hooray! Let them use the app now.
					    	setFlipperMovement(MOVE_DOWN);
					    	flipper.setDisplayedChild(SHOW_SIGNED_IN);
					    	signedIn = true;
					    	
					    } else 
					    {
					      // Sign up didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    	cloudAccountStatus.setText("Error");
					    }
					  }
					});
			}
			break;
			
		case R.id.preferences_cloud_account_signedin_signout_request:
			
			ParseUser.logOut();
			signedIn = false;
			
			setFlipperMovement(MOVE_UP);
			flipper.setDisplayedChild(SHOW_SIGN_UP);
			break;
			
		case R.id.preferences_cloud_account_signup_signin_request:
			
			setFlipperMovement(MOVE_DOWN);
			flipper.setDisplayedChild(SHOW_SIGN_IN);
			break;
			
		case R.id.preferences_cloud_account_signin_signin_request:
			
			String existingName = signInUserName.getText().toString();
			String existingPass = signInUserPass.getText().toString();
			
			ParseUser.logInInBackground(existingName, existingPass, new LogInCallback() 
			{
				  public void done(ParseUser user, ParseException e) 
				  {
				    if (user != null) 
				    {
				    	cloudAccountStatus.setText(ParseUser.getCurrentUser().getUsername());
				    	setFlipperMovement(MOVE_DOWN);
						flipper.setDisplayedChild(SHOW_SIGNED_IN);
						signedIn = true;
						
				    } 
				    else 
				    {
				    
				    }
				  }
				});
			
			break;

		default:
			break;
		}
		
	}
}
