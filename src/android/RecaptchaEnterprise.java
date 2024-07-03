package com.andreszs.grecaptcha;

import android.app.Application;
import android.content.Context;
import android.view.View;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;

import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.google.android.recaptcha.RecaptchaAction;

public class RecaptchaEnterprise extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("verify")) {
            try {
                JSONObject arg_object = args.getJSONObject(0);
                String sitekeyAndroid = arg_object.getString("sitekeyAndroid");
                this.verify(sitekeyAndroid, callbackContext);
            } catch (JSONException e) {
                callbackContext.error("Verify called without providing sitekeyAndroid");
            }
            return true;
        }
        return false;
    }

    private void verify(final String sitekeyAndroid, final CallbackContext callbackContext) {
        if (sitekeyAndroid.isEmpty()) {
            callbackContext.error("Verify called without providing sitekeyAndroid");
            return;
        }

        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                initializeRecaptchaClient(sitekeyAndroid, callbackContext);
            }
        });
    }

    private void initializeRecaptchaClient(String sitekeyAndroid, final CallbackContext callbackContext) {
        Application appContext = (Application) cordova.getActivity().getApplicationContext(); // Cast to Application

        Recaptcha.getTasksClient(appContext, sitekeyAndroid)
                .addOnSuccessListener(new OnSuccessListener<RecaptchaTasksClient>() {
                    @Override
                    public void onSuccess(RecaptchaTasksClient recaptchaTasksClient) {
                        recaptchaTasksClient.executeTask(RecaptchaAction.LOGIN)
                                .addOnSuccessListener(cordova.getActivity(), new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String token) {
                                        if (!token.isEmpty()) {
                                            callbackContext.success(token);
                                        } else {
                                            callbackContext.error("Response token was empty.");
                                        }
                                    }
                                })
                                .addOnFailureListener(cordova.getActivity(), new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        if (e instanceof ApiException) {
                                            // An error we know about occurred.
                                            ApiException apiException = (ApiException) e;
                                            int statusCode = apiException.getStatusCode();
                                            String message = CommonStatusCodes.getStatusCodeString(statusCode);
                                            callbackContext.error(message);
                                        } else {
                                            // A different, unknown type of error occurred.
                                            callbackContext.error(e.getMessage());
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        callbackContext.error("Failed to initialize RecaptchaTasksClient: " + e.getMessage());
                    }
                });
    }
}
