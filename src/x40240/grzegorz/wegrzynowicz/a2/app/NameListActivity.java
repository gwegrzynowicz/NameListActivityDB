package x40240.grzegorz.wegrzynowicz.a2.app;

import java.util.List;

import x40240.grzegorz.wegrzynowicz.a2.app.db.DBHelper;
import x40240.grzegorz.wegrzynowicz.a2.app.model.PersonInfo;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public final class NameListActivity extends ListActivity
	{
	private static final String LOGTAG = NameListActivity.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	private PersonInfo personInfo;  // data passed to us
	private ListAdapter listAdapter;
	private Context activityContext = this;
	
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    intitialize();
	    setListAdapter(listAdapter = new PersonInfoListAdapter(activityContext));
	}
	
	// Encapsulate non-UI related initialization here.
	private void intitialize()
	{
		// Get the Intent that started us.
	    Intent callingIntent = this.getIntent();
	    //  If we are being passed a Serialized POJO:
	    personInfo = (PersonInfo)callingIntent.getSerializableExtra("personInfo");
	}
	
	//  Life cycle Methods
	//  http://developer.android.com/reference/android/app/Activity.html
	public void onStart()
	{
	    //  Called after onCreate() OR onRestart()
	    //  Called after onStop() but process has not been killed.
	    if (DEBUG) Log.d (LOGTAG, "onRestart");
	    super.onRestart();
	}
	
	@Override
	public void onRestart() 
	{
	    //  Called after onStop() but process has not been killed.
	    if (DEBUG) Log.d (LOGTAG, "onRestart");
	    super.onRestart();
	}
	
	@Override
	protected void onResume()
	{
	    //  Called after onStart() as Activity comes to foreground.
	    if (DEBUG) Log.d (LOGTAG, "onResume");
	    super.onResume();
	    new UpdateDBTask().execute (personInfo);
	}
	
	@Override
	public void onPause()
	{
	    //  Called when Activity is placed in background
	    if (DEBUG) Log.d (LOGTAG, "onPause");
	    super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		//  The Activity is no longer visible
	    if (DEBUG) Log.d (LOGTAG, "onStop");
	    super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		//  The Activity is finishing or being destroyed by the system
	    if (DEBUG) Log.d (LOGTAG, "onDestroy");
	    DBHelper dbHelper = new DBHelper(activityContext);
        dbHelper.closeDB();
        super.onDestroy();
	}
	
	//  Dialog management
	private ProgressDialog progressDialog;
	protected static final int INDETERMINATE_DIALOG_KEY = 0;
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
	    switch (id) {
	    case INDETERMINATE_DIALOG_KEY:
	        progressDialog = new ProgressDialog(this);
	        return progressDialog;
	    }
	    return null;
	}
	
	@Override
	protected void onPrepareDialog (int id, Dialog dialog)
	{
	    if (DEBUG) {
	        Log.d(LOGTAG, "onPrepareDialog.threadId=" + Thread.currentThread().getId());
	        Log.d(LOGTAG, "onPrepareDialog.id=" + id);
	        Log.d(LOGTAG, "onPrepareDialog.dialog=" + dialog);
	    }
	    if (dialog instanceof ProgressDialog)
	    {
	        ((ProgressDialog)dialog).setMessage(getResources().getText(R.string.please_wait_label));
	        ((ProgressDialog)dialog).setIndeterminate(true);
	        ((ProgressDialog)dialog).setCancelable(true);
	    }
	}
	
	
	// http://developer.android.com/reference/android/os/AsyncTask.html
	private class UpdateDBTask extends AsyncTask<PersonInfo, Void, List<PersonInfo>>
	{
	    private final String LOGTAG = UpdateDBTask.class.getSimpleName();
	    private final boolean DEBUG = true;
	    
	    //  Runs on Main thread so we can manipulate the UI.
	    @Override
	    protected void onPreExecute()
	    {
	        showDialog(INDETERMINATE_DIALOG_KEY);
	    }
	    
	    //  Do all expensive operations here off the main thread.
	    @Override
	    protected List<PersonInfo> doInBackground (final PersonInfo...paramArrayOfParams)
	    {
	        if (DEBUG) 
	        	Log.d(LOGTAG, "**** doInBackground() STARTING");    
	    	DBHelper dbHelper = new DBHelper(activityContext);
	        PersonInfo personInfo = paramArrayOfParams[0];
	        if (personInfo != null)
	        	dbHelper.insert(personInfo);
	        List<PersonInfo> list = dbHelper.selectAll();
	        return list;
	    }
	
	    //  Runs on Main thread so we can manipulate the UI.
	    protected void onPostExecute(final List<PersonInfo> list)
	    {
	        ((PersonInfoListAdapter)listAdapter).setList(list);  // Must be done on main thread
	        dismissDialog(INDETERMINATE_DIALOG_KEY);
	    }
	}
	
	private class PersonInfoListAdapter extends BaseAdapter
	{
	    //  Remember our context so we can use it when constructing views.
	    private Context context;
	    
	    private List<PersonInfo> list;
	    private LayoutInflater   layoutInflater;
	
	    public PersonInfoListAdapter (Context context)
	    {
	        this.context = context;
	    }
	
	    // The number of items in the list.
	    public int getCount()
	    {
	        return list == null ? 0 : list.size();
	    }
	
	    // @see android.widget.ListAdapter#getItem(int)
	    public Object getItem (int position)
	    {
	        return list.get(position);
	    }
	
	    // Use the array index as a unique id.
	    public long getItemId (int position)
	    {
	        return position;
	    }
	
	    public void setList (List<PersonInfo> list)
	    {
	    	this.list = list;
	    	this.notifyDataSetChanged();
	    }
	    
	    // @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	    public View getView (int position, View convertView, ViewGroup parent)
	    {
	        ViewGroup listItem;
	        if (convertView == null)
	        {
	        	listItem = (ViewGroup) getLayoutInflator().inflate(R.layout.list_item, null);
	        }
	        else
	        {
	        	listItem = (ViewGroup) convertView;
	        }
	        PersonInfo personInfo = list.get(position);
	        TextView lastnameText = (TextView) listItem.findViewById(R.id.lastname_text);
	        TextView firstnameText = (TextView) listItem.findViewById(R.id.firstname_text);
	        TextView genderText = (TextView) listItem.findViewById(R.id.gender_text);
	        TextView employedText = (TextView) listItem.findViewById(R.id.check_box_text);
	        TextView countryText = (TextView) listItem.findViewById(R.id.country_text);
	        
	        lastnameText.setText(personInfo.getLastname());
	        firstnameText.setText(personInfo.getFirstname());
	        countryText.setText(personInfo.getCountry());
	        
	        String genderName = "";
	        Resources resources = context.getResources();
	        switch (personInfo.getGender())
	        {
		        case PersonInfo.GENDER_MALE:
		        	genderName = resources.getString(R.string.male_label);
		            break;
		        case PersonInfo.GENDER_FEMALE:
		        	genderName = resources.getString(R.string.female_label);
		            break;
		        default:
		        case PersonInfo.GENDER_UNKNOWN:
		            genderName = resources.getString(R.string.unknown_label);
		            break;
	        }
	        genderText.setText(genderName);
	        
	        String employedName = "";
	        switch (personInfo.getEmployed())
	        {
		        case PersonInfo.EMPLOYED:
		        	employedName = resources.getString(R.string.yes_label);
		            break;
		        default:
		        case PersonInfo.NOT_EMPLOYED:
		        	employedName = resources.getString(R.string.no_label);
	        }
	        employedText.setText(employedName);
	        
	        return listItem;
	    }
	    
	    private LayoutInflater getLayoutInflator()
	    {
	        if (layoutInflater == null)
	        {
	            layoutInflater = (LayoutInflater)
	                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        }
	        return layoutInflater;
	    }
	}
}
