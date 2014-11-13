package com.kpbird.googlepluslogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GooglePlusLoginUtils implements ConnectionCallbacks, OnConnectionFailedListener,OnClickListener {
	private String TAG = "GooglePlusLoginUtils";
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	private static final int PROFILE_PIC_SIZE = 400;
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHOTO = "photo";
    public static final String PROFILE= "profile";

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	
	private SignInButton btnSignIn;
	private Context ctx;
	private GPlusLoginStatus loginstatus;
	public interface GPlusLoginStatus{
		public void OnSuccessGPlusLogin(Bundle profile);
	}
	
	public GooglePlusLoginUtils(Context ctx,int btnRes){
		Log.i(TAG, "GooglePlusLoginUtils");
		this.ctx= ctx;
		this.btnSignIn =(SignInButton) ((Activity)ctx).findViewById(btnRes);
		btnSignIn.setOnClickListener(this);
		 // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        
        
	}

	public void setLoginStatus(GPlusLoginStatus loginStatus){
		this.loginstatus = loginStatus;
	}
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "onConnectionFailed");
        Log.i(TAG,"Error Code "+ result.getErrorCode());
		if (!result.hasResolution()) {
	        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity)ctx,0).show();
	        return;
	    }
	 
	    if (!mIntentInProgress) {
	        // Store the ConnectionResult for later usage
	        mConnectionResult = result;
	        
	        if (mSignInClicked) {
	            // The user has already clicked 'sign-in' so we attempt to
	            // resolve all
	            // errors until the user is signed in, or they cancel.
	            resolveSignInError();
	        }
	    }		
	}
	public void setSignInClicked(boolean value){
		mSignInClicked  =value;
	}
	public void setIntentInProgress(boolean value){
		mIntentInProgress = value;
	}
	public void connect(){
        mGoogleApiClient.connect();
	}
	public void reconnect(){
		if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
	}
	public void disconnect(){
		if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
	}
	private void signInWithGplus() {
		Log.i(TAG, "signInWithGplus");
	    if (!mGoogleApiClient.isConnecting()) {
	        mSignInClicked = true;
	        resolveSignInError();
	    }
	}
	private void resolveSignInError() {
		Log.i(TAG, "resolveSignInError");
	    if (mConnectionResult.hasResolution()) {
	        try {
	            mIntentInProgress = true;
	            mConnectionResult.startResolutionForResult((Activity)ctx, RC_SIGN_IN);
	        } catch (SendIntentException e) {
	            mIntentInProgress = false;
	            mGoogleApiClient.connect();
	        }
	    }
	}
	@Override
	public void onConnected(Bundle arg0) {
		Log.i(TAG, "onConnected");
		 mSignInClicked = false;
		    Toast.makeText(ctx, "User is connected!", Toast.LENGTH_LONG).show();
		 
		    // Get user's information
		    getProfileInformation();
		 
	}
	@Override
	public void onConnectionSuspended(int arg0) {
		Log.i(TAG, "onConnectionSuspended");
		mGoogleApiClient.connect();
	}

	private void getProfileInformation() {
		Log.i(TAG, "getProfileInformation");
	    try {
	        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
	            Person currentPerson = Plus.PeopleApi
	                    .getCurrentPerson(mGoogleApiClient);
	            String personName = currentPerson.getDisplayName();
	            String personPhotoUrl = currentPerson.getImage().getUrl();
	            String personGooglePlusProfile = currentPerson.getUrl();
	            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
	            
	            Log.e(TAG, "Name: " + personName + ", plusProfile: "
	                    + personGooglePlusProfile + ", email: " + email
	                    + ", Image: " + personPhotoUrl);
	 
	 
	            // by default the profile url gives 50x50 px image only
	            // we can replace the value with whatever dimension we want by
	            // replacing sz=X
	            personPhotoUrl = personPhotoUrl.substring(0,
	                    personPhotoUrl.length() - 2)
	                    + PROFILE_PIC_SIZE;
	            
	            Bundle profile = new Bundle();
	            profile.putString(NAME, personName);
	            profile.putString(EMAIL, email);
	            profile.putString(PHOTO, personPhotoUrl);
	            profile.putString(PROFILE, personGooglePlusProfile);
	            
	            loginstatus.OnSuccessGPlusLogin(profile);
	 
	         //   new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
	 
	        } else {
	            Toast.makeText(ctx,
	                    "Person information is null", Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	@Override
	public void onClick(View v) {
		signInWithGplus();
	}
	public void onActivityResult(int requestCode,int responseCode,Intent intent){
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != ((Activity)ctx).RESULT_OK) {
				setSignInClicked(false);
			}
			setIntentInProgress(false);
			reconnect();
		}
	}

	
}
