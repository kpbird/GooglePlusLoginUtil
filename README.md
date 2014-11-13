Login with Google Plus Utility
----------
### Pre-requisite###

Official Document: https://developers.google.com/+/mobile/android/sign-in

Tutorial from AndroidHive : http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/

Note: Please read AndroidHive article for initial setup & other process



### Usage ###

#### Step 1: Import "GooglePlusLoginUtils.java" class in your package
#### Step 2: Implement callback listener
	public class LoginActivity extends Activity implements GooglePlusLoginUtils.GPlusLoginStatus 

#### Step 3: Declare callback method
	public void OnSuccessGPlusLogin(Bundle profile) {
		Log.i(TAG,profile.getString(GooglePlusLoginUtils.NAME));       
		Log.i(TAG,profile.getString(GooglePlusLoginUtils.EMAIL));
		Log.i(TAG,profile.getString(GooglePlusLoginUtils.PHOTO));
		Log.i(TAG,profile.getString(GooglePlusLoginUtils.PROFILE));
    }

#### Step 4: Declare object of GooglePlusLoginUtils.java
	private GooglePlusLoginUtils gLogin;

#### Step 5: Initialize object in onCreate method
	 gLogin = new GooglePlusLoginUtils(this, R.id.activity_login_gplus); // Id of login button
        gLogin.setLoginStatus(this);
#### Step 6: Declare few supporting methods 

	@Override
	protected void onStart() {
	        super.onStart();
	        gLogin.connect();
	    }
	    @Override
	    protected void onStop() {
	        super.onStop();
	        gLogin.disconnect();
	    }
	    @Override
	    protected void onActivityResult(int requestCode, int responseCode,
	                                    Intent intent) {
	        gLogin.onActivityResult(requestCode, responseCode, intent);
	
	    }

### FAQ###
Q: What is problem of above approach ?
A: You activity has lot of scattered code for Google Pluse login, It will be more complex and unmanageable when you will have another social network login like Facebook and Twitter.

Q: How I will receive response for login success?
A: You need to implement interface "GPlusloginStatus", on successful login you will receive callback "OnSuccessGPlusLogin"

Q: What will be received on login success ?
A: You will have Display name, Email , Photo and Profile url.

Q: Do I need to handle login failure / error ?
A: No, GooglePlusLoginUtils.java will handle login failure, error and display appropriate message on screen.   


   
