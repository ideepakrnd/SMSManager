var smsplugin =  {
	isSupported:function(successCallback,errorCallback) {
		cordova.exec(successCallback, errorCallback, 'SMSPlugin', 'isSMSSupported', [{}]);
	},
	send:function(mobileNumber, smsBody, successCallback,errorCallback) {
		cordova.exec(
		successCallback, 
		errorCallback, 
		'SMSPlugin', 
		'sendSMS', 
		[{                 
                "mobileNumber": mobileNumber,
                "smsBody": smsBody
        }]
		);
	}
}
module.exports = smsplugin;