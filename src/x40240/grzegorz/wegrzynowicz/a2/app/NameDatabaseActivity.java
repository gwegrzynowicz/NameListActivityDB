package x40240.grzegorz.wegrzynowicz.a2.app;

import x40240.grzegorz.wegrzynowicz.a2.app.model.PersonInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.Spinner;

public class NameDatabaseActivity extends Activity
{
	private static final String LOGTAG = NameDatabaseActivity.class.getSimpleName();
	
	private EditText     firstnameEdit;
	private EditText     lastnameEdit;
	private RadioButton  maleButton;
	private RadioButton  femaleButton;
	private CheckBox     emplCheck;
	private Spinner      countrySpinner;
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	
	    //  Not used, but demonstrates how we can track clicks on the EditText field
	    View.OnClickListener onClickListener = new View.OnClickListener()
	    {
	        public void onClick (View v) 
	        {
	            Log.d(LOGTAG, "firstnameText: Got Click!");
	        }
	    };
	    
	    //  Demonstrates how we can monitor/respond to each key typed into the EditText field
	    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
	        @Override
	        public boolean onKey (View view, int keyCode, KeyEvent event) {
	            if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                switch (keyCode) {
	                    case KeyEvent.KEYCODE_DPAD_CENTER:
	                    case KeyEvent.KEYCODE_ENTER:
	                        Log.d(LOGTAG, view.getClass().getSimpleName()+": Key event!");
	                        return true;
	                }
	            }
	            return false;
	        }
	    };
	    
	    firstnameEdit = (EditText) findViewById(R.id.firstname_edit);
	    firstnameEdit.setOnClickListener(onClickListener);
	    firstnameEdit.setOnKeyListener(onKeyListener);
	    
	    lastnameEdit = (EditText) findViewById(R.id.lastname_edit);
	    lastnameEdit.setOnClickListener(onClickListener);
	    lastnameEdit.setOnKeyListener(onKeyListener);
	    
	    maleButton = (RadioButton) this.findViewById(R.id.male_radio);
	    femaleButton = (RadioButton) this.findViewById(R.id.female_radio);
	    
	    emplCheck = (CheckBox) this.findViewById(R.id.check_box_text);
	    countrySpinner = (Spinner) findViewById(R.id.spinner1);
	}
	
	protected void onResume() 
	{
	    //  Called after onStart() as Activity comes to foreground.
	    super.onResume();
	    clearData();
	}
	
	//  This is the OnClickListener that is set in the XML layout as android:onClick="onOKButtonClick"
	public void onOKButtonClick(View view)
	{
	    PersonInfo personInfo = new PersonInfo();
	    personInfo.setFirstname(firstnameEdit.getText().toString());
	    personInfo.setLastname(lastnameEdit.getText().toString());
	    
	    if (maleButton.isChecked()) personInfo.setGender(PersonInfo.GENDER_MALE);
	    if (femaleButton.isChecked()) personInfo.setGender(PersonInfo.GENDER_FEMALE);
	    if (emplCheck.isChecked()) personInfo.setEmployed(PersonInfo.EMPLOYED);
	    personInfo.setCountry((String)countrySpinner.getItemAtPosition(countrySpinner.getSelectedItemPosition()));
	    
	    Log.d(LOGTAG, "firstname=" + personInfo.getFirstname());
	    Log.d(LOGTAG, "lastname=" + personInfo.getLastname());
	    Log.d(LOGTAG, "gender=" + personInfo.getGender());
	    Log.d(LOGTAG, "employed=" + personInfo.getEmployed());
	    Log.d(LOGTAG, "country=" + personInfo.getCountry());
	    
	    
	    // Intent myIntent = new Intent();
	    // This is an EXPLICIT intent -- i.e., we specify the target Activity by class name.
	    // myIntent.setClass (activityContext, NameListActivity.class);  
	    // This specific mechanism requires the POJO (i.e., PersonInfo) implement the Serializable marker interface.
	    // myIntent.putExtra("personInfo", personInfo);
	    // startActivity(myIntent);
	    
	    // This is an implementation of the IMPLICIT intent.
	    Intent myIntent = new Intent();
    	myIntent.setAction("x40240.grzegorz.wegrzynowicz.a2.app.VIEW");
    	myIntent.putExtra("personInfo", personInfo);
    	// Verify that the intent will resolve to an activity
    	if (myIntent.resolveActivity(getPackageManager()) != null) {
    		startActivity(myIntent);
    	}
	}
	
	//  This is the OnClickListener that is set in the XML layout as android:onClick="onClearButtonClick"
	public void onClearButtonClick(View view)
	{
		clearData();
	}
	
	//  This is the OnClickListener that is set in the XML layout as android:onClick="onClearButtonClick"
	public void onListButtonClick(View view)
	{
	    //Intent myIntent = new Intent();
	    //startActivity(myIntent.setClass (activityContext, NameListActivity.class));
		
		// IMPLICIT intent.
		Intent myIntent = new Intent();
    	myIntent.setAction("x40240.grzegorz.wegrzynowicz.a2.app.VIEW");
    	if (myIntent.resolveActivity(getPackageManager()) != null) {
    		startActivity(myIntent);
    	}
	}
	
	private void clearData() 
	{
	    firstnameEdit.setText(null);
	    lastnameEdit.setText(null);
	    maleButton.setChecked(false);
	    femaleButton.setChecked(false);
	    emplCheck.setChecked(false);
	}
}
