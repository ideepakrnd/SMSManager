package com.rancelab.smsmanager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSPlugin extends CordovaPlugin {

	public static final String ACTION_IS_SMS_SUPPORTED = "isSMSSupported";
	public static final String ACTION_SMS_SEND = "sendSMS";

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		try {
			Activity ctx = this.cordova.getActivity();

			// IS SMS Supported
			if (ACTION_IS_SMS_SUPPORTED.equals(action)) {
				if (ctx.getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_TELEPHONY)) {
					callbackContext.sendPluginResult(new PluginResult(
							PluginResult.Status.OK, true));
				} else {
					callbackContext.sendPluginResult(new PluginResult(
							PluginResult.Status.OK, false));
				}
			}

			// TO SEND SMS
			if (ACTION_SMS_SEND.equals(action)) {
				// callbackContext.sendPluginResult(new
				// PluginResult(PluginResult.Status.OK, true));
				try {
					// GET Mobile Number And Body
					JSONObject arg_object = args.getJSONObject(0);

					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";

					PendingIntent sentPI = PendingIntent
							.getBroadcast(ctx.getApplicationContext(), 0,
									new Intent(SENT), 0);

					PendingIntent deliveredPI = PendingIntent.getBroadcast(ctx
							.getApplicationContext(), 0, new Intent(DELIVERED),
							0);

					// ---when the SMS has been sent---
					ctx.registerReceiver(new BroadcastReceiver() {
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							// TODO Auto-generated method stub
							switch (getResultCode()) {
							case Activity.RESULT_OK:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"SEND --> RESULT_OK", Toast.LENGTH_LONG)
										.show();
								break;
							case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"SEND --> RESULT_ERROR_GENERIC_FAILURE",
										Toast.LENGTH_LONG).show();
								break;
							case SmsManager.RESULT_ERROR_NO_SERVICE:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"SEND --> RESULT_ERROR_NO_SERVICE",
										Toast.LENGTH_LONG).show();
								break;
							case SmsManager.RESULT_ERROR_NULL_PDU:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"SEND --> RESULT_ERROR_NULL_PDU",
										Toast.LENGTH_LONG).show();
								break;
							case SmsManager.RESULT_ERROR_RADIO_OFF:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"SEND --> RESULT_ERROR_RADIO_OFF",
										Toast.LENGTH_LONG).show();
								break;
							}
						}
					}, new IntentFilter(SENT));

					// ---when the SMS has been delivered---
					ctx.registerReceiver(new BroadcastReceiver() {
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							// TODO Auto-generated method stub
							switch (getResultCode()) {
							case Activity.RESULT_OK:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"DELIVERED --> RESULT_OK",
										Toast.LENGTH_LONG).show();
								break;
							case Activity.RESULT_CANCELED:
								Toast.makeText(
										cordova.getActivity()
												.getApplicationContext(),
										"DELIVERED --> RESULT_CANCELED",
										Toast.LENGTH_LONG).show();
								break;
							}
						}
					}, new IntentFilter(DELIVERED));

					// Get the default instance of the SmsManager
					SmsManager smsManager = SmsManager.getDefault();

					Toast.makeText(ctx.getApplicationContext(),
							arg_object.getString("smsBody").toString(),
							Toast.LENGTH_LONG).show();

					Toast.makeText(ctx.getApplicationContext(),
							arg_object.getString("mobileNumber").toString(),
							Toast.LENGTH_LONG).show();

					smsManager.sendTextMessage(
							arg_object.getString("mobileNumber").toString(),
							null, arg_object.getString("smsBody").toString(),
							sentPI, deliveredPI);

					callbackContext.sendPluginResult(new PluginResult(
							PluginResult.Status.OK, true));

				} catch (Exception ex) {

					Toast.makeText(ctx.getApplicationContext(),
							ex.getMessage(), Toast.LENGTH_LONG).show();

					callbackContext.sendPluginResult(new PluginResult(
							PluginResult.Status.JSON_EXCEPTION));

				}

			}

		} catch (Exception e) {
			// callbackContext.error(e.getMessage());
			callbackContext.sendPluginResult(new PluginResult(
					PluginResult.Status.JSON_EXCEPTION));
			return false;
		}
		return true;
	}
}
