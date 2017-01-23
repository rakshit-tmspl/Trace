package com.tmspl.trace.extra;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

public class GeocodeAddressIntentService extends IntentService
{

    private static final String TAG = MyApplication.APP_TAG + GeocodeAddressIntentService.class.getSimpleName();

    protected ResultReceiver resultReceiver;

    public GeocodeAddressIntentService()
    {
        super("AddressService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {



        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        List<Address> addresses = null;

        int fetchType = intent.getIntExtra(Constants.FETCH_TYPE_EXTRA, 0);
        Log.e(TAG, "fetchType == " + fetchType);

        if (fetchType == Constants.USE_ADDRESS_NAME)
        {
            String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);
            try
            {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e)
            {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }
        } else if (fetchType == Constants.USE_ADDRESS_LOCATION)
        {
            double latitude = intent.getDoubleExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, 0);
            double longitude = intent.getDoubleExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, 0);

            try
            {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ioException)
            {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException)
            {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + latitude + ", Longitude = " +
                        longitude, illegalArgumentException);
            }
        } else
        {
            errorMessage = "Unknown Type";
            Log.e(TAG, errorMessage);
        }

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        if (addresses == null || addresses.size() == 0)
        {
            if (errorMessage.isEmpty())
            {
                errorMessage = "Not Found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
        } else
        {
            /**            for (Address address : addresses) {
             String outputAddress = "";
             for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
             if(address.getMaxAddressLineIndex()==2){
             break;
             }
             outputAddress += " --- " + address.getAddressLine(i);
             }
             Log.e(TAG, outputAddress);
             }*/

            Address address = addresses.get(0);
            String addressFragments = "";

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
            {
                addressFragments = addressFragments + address.getAddressLine(i) + ", ";
            }
            Log.i(TAG, "Address Found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    addressFragments, address);
        }


    }

    private void deliverResultToReceiver(int resultCode, String message, Address address)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }

}
