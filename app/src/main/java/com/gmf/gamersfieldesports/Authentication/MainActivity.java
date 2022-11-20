package com.gmf.gamersfieldesports.Authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gmf.gamersfieldesports.COD.CodActivity;
import com.gmf.gamersfieldesports.Chess.ChessActivity;
import com.gmf.gamersfieldesports.Freefire.freefireActivity;
import com.gmf.gamersfieldesports.Ludo.LudoActivity;
import com.gmf.gamersfieldesports.Playerprofile.GameinfoActivity;
import com.gmf.gamersfieldesports.Playerprofile.RedeemActivity;
import com.gmf.gamersfieldesports.Playerprofile.UseraccountActivity;
import com.gmf.gamersfieldesports.Pubg.PubgActivity;
import com.gmf.gamersfieldesports.Pubglite.pubgliteActivity;
import com.gmf.gamersfieldesports.R;
import com.gmf.gamersfieldesports.Userdetails.WalletActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    //private DocumentReference updateref;
    // private boolean update;


    private DocumentReference userdocref;
    private DocumentReference notificationref;

    private CardView mcardview1;
    private CardView mcardview2;
    private CardView mcardview3;
    private CardView mcardview4;
    private CardView mcardview5;
    private CardView mcardview6;

    private TextView username;
    private TextView usermail;
    private TextView userbalance;
    private TextView newNotifications;

    private String uname;
    private String umail;
    private String ubalance;
    private String unotify;

    private Button userprofile;
    private Button userRedeem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startapp splash screen diable
        StartAppSDK.init(this, "202945693", true);
        StartAppAd.disableSplash();

        mcardview1 = findViewById(R.id.mainCard1);
        mcardview2 = findViewById(R.id.mainCard2);
        mcardview3 = findViewById(R.id.mainCard3);
        mcardview4 = findViewById(R.id.mainCard4);
        mcardview5 = findViewById(R.id.mainCard5);
        mcardview6 = findViewById(R.id.mainCard6);

        userprofile = findViewById(R.id.userproflilebtn);
        userRedeem = findViewById(R.id.userredeem);
        username = findViewById(R.id.userName);
        userbalance = findViewById(R.id.userbalance);
        usermail = findViewById(R.id.usermail);
        newNotifications = findViewById(R.id.main_notify);

        //rateapp
        AppRate.with(this)
                .showRateDialog(this);

        //buttons
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(MainActivity.this, UseraccountActivity.class);
                startActivity(userIntent);
            }
        });
        userRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(MainActivity.this, RedeemActivity.class);
                startActivity(userIntent);
            }
        });

        mcardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pubgintent = new Intent(MainActivity.this, PubgActivity.class);
                startActivity(pubgintent);
            }
        });
        mcardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pubgliteIntent = new Intent(MainActivity.this, pubgliteActivity.class);
                startActivity(pubgliteIntent);
            }
        });
        mcardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent freefireIntent = new Intent(MainActivity.this, freefireActivity.class);
                startActivity(freefireIntent);
            }
        });
        mcardview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codintent = new Intent(MainActivity.this, CodActivity.class);
                startActivity(codintent);
            }
        });
        mcardview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chessintent = new Intent(MainActivity.this, ChessActivity.class);
                startActivity(chessintent);
            }
        });
        mcardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ludointent = new Intent(MainActivity.this, LudoActivity.class);
                startActivity(ludointent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_logout:
                mAuth.signOut();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            case R.id.main_wallet:
                Intent walletintent= new Intent(MainActivity.this, WalletActivity.class);
                startActivity(walletintent);
                return true;
            case R.id.main_account:
                Intent userIntent = new Intent(MainActivity.this, UseraccountActivity.class);
                startActivity(userIntent);
                return true;
            case R.id.main_yt:
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setData(Uri.parse("https://youtube.com/channel/UCHq0-Iy522b8SYIP1JWn4bQ"));
                startActivity(youtubeIntent);
                return true;
            case R.id.main_gameinfo:
                Intent gameinfo = new Intent(MainActivity.this, GameinfoActivity.class);
                startActivity(gameinfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser==null){
            Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginintent);
            finish();
        }else{

            //get user id
            userId = mCurrentUser.getUid();

            userdocref = db.collection("users").document(userId);
            notificationref = db.collection("Update").document("notification");

            userdocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        uname = documentSnapshot.get("name").toString();
                        umail = documentSnapshot.get("Mail").toString();
                        ubalance = documentSnapshot.get("Balance").toString();

                        //set user name and mail
                        username.setText(uname);
                        usermail.setText(umail);
                        userbalance.setText(ubalance);

                    }else{
                        Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            notificationref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        unotify = documentSnapshot.get("notify").toString();

                        //set notification
                        newNotifications.setText(unotify);
                    }else{
                        Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}