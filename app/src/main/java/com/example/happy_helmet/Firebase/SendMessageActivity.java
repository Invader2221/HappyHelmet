package com.example.happy_helmet.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.happy_helmet.MainActivity;
import com.example.happy_helmet.R;
import com.example.happy_helmet.Recycler.MyHelmetActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.happy_helmet.MainActivity.myHelmetDetails;

public class SendMessageActivity extends AppCompatActivity {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAMDXl73U:APA91bFECV534E3jLf8v5noX7tyLKdVnsPKpDAU56EVbkMYxtnKZEv8bdSAqMpOIWAO5CsD7fdqTQQYYjl0I0VYFusL2ZlSXAG2OZ-f0g8Kf_avWh5Og1tu384Xv62E3k9mBukPoUwaf";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    KProgressHUD progressHUD;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        getSupportActionBar().setTitle("Message");

        Button btnSend = findViewById(R.id.btnSend);
        TextView helmetIdText = findViewById(R.id.notification_title_text);
        TextView checkInText = findViewById(R.id.checkin_time_text);
        TextView receiverIDText = findViewById(R.id.receiver_id_text);

        progressHUD = KProgressHUD.create(SendMessageActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        final String helmetId = getIntent().getStringExtra("SCANED_HELMET_ID");
        helmetIdText.setText("Helmet Id : " + helmetId);


        String userID = "";
        String userEmail = "";
        if (myHelmetDetails != null) {
            for (int i = 0; i < myHelmetDetails.size(); i++) {

                if(myHelmetDetails.get(i).getHid().equals(helmetId)){
                    userID = myHelmetDetails.get(i).getUser();
                    userEmail = myHelmetDetails.get(i).getEmail();
                }
            }

        }
        receiverIDText.setText("Receiver Id : " + userEmail);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
        final String formattedDate = df.format(c.getTime());

        checkInText.setText("Current Time : " + formattedDate);

        final String finalUserID = userID;
        //final String finalUserEmail = userEmail;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!finalUserID.equals("")) {
                    TOPIC = "/topics/" + finalUserID;
                    NOTIFICATION_TITLE = "Helmet Store Request";
                    NOTIFICATION_MESSAGE = helmetId;

                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
                        notifcationBody.put("checkIN", formattedDate);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage());
                    }
                    sendNotification(notification);
                } else {
                    Toast.makeText(SendMessageActivity.this, "Error! No User found!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void sendNotification(JSONObject notification) {
        progressHUD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressHUD.dismiss();
                        Toast.makeText(SendMessageActivity.this, "Successfully sent", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressHUD.dismiss();
                        Toast.makeText(SendMessageActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(SendMessageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to go back!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


    }

}
