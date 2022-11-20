package com.gmf.gamersfieldesports.Playerprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gmf.gamersfieldesports.Authentication.LoginActivity;
import com.gmf.gamersfieldesports.R;
import com.gmf.gamersfieldesports.Userdetails.MyMatchesActivity;
import com.gmf.gamersfieldesports.Userdetails.WalletActivity;

public class UseraccountActivity extends AppCompatActivity {

    private TextView userPlayername;
    private TextView userPlayermail;
    private TextView usercountry;
    private TextView userwallet;
    private TextView usermatches;
    private Button redeem;
    private Button ecgDiamonds;
    private Button gameinfo;
    private Button transactions;
    private Button helpdesk;
    private Button youtubebtn;
    private Button privacybtn;
    private Button logout;
    private CardView uwalletCard;
    private CardView uMatchCard;

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mcurrentuser = mAuth.getCurrentUser();
    String userid = mcurrentuser.getUid();
    private DocumentReference playerref = db.collection("users").document(userid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useraccount);

        //referance
        userPlayername = findViewById(R.id.userplayername);
        userPlayermail = findViewById(R.id.userplayermail);
        userwallet = findViewById(R.id.v_userwallet);
        usermatches = findViewById(R.id.v_usermatch);
        usercountry = findViewById(R.id.userplayercountry);
        redeem = findViewById(R.id.redeem);
        ecgDiamonds = findViewById(R.id.ecgDiamonds);
        gameinfo = findViewById(R.id.gameinfo);
        transactions = findViewById(R.id.transactions);
        helpdesk = findViewById(R.id.helpdesk);
        youtubebtn = findViewById(R.id.youtubebtn);
        privacybtn = findViewById(R.id.privacybtn);
        logout = findViewById(R.id.logout);
        uwalletCard = findViewById(R.id.u_walletcard);
        uMatchCard = findViewById(R.id.u_matchcard);

        playerref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    userPlayername.setText(documentSnapshot.getString("name"));
                    userPlayermail.setText(documentSnapshot.getString("Mail"));
                    usercountry.setText(documentSnapshot.getString("Country"));
                    userwallet.setText(documentSnapshot.getString("Balance"));
                    usermatches.setText(documentSnapshot.getString("MatchesPlayed"));
                }else{
                    Toast.makeText(UseraccountActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //wallet Card
        uwalletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletIntent = new Intent(UseraccountActivity.this, WalletActivity.class);
                startActivity(walletIntent);
            }
        });
        //my matches card
        uMatchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mymatchIntent = new Intent(UseraccountActivity.this, MyMatchesActivity.class);
                startActivity(mymatchIntent);
            }
        });
        //gameinfo
        gameinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameinfo = new Intent(UseraccountActivity.this, GameinfoActivity.class);
                startActivity(gameinfo);
            }
        });
        //transactions
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transDailyIntent = new Intent(UseraccountActivity.this,TransactionsActivity.class);
                startActivity(transDailyIntent);
            }
        });
        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(UseraccountActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
            }
        });
        //reddem
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redeemintent = new Intent(UseraccountActivity.this,RedeemActivity.class);
                startActivity(redeemintent);
            }
        });
        //exchange Diamonds
        ecgDiamonds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ecgDiamonds = new Intent(UseraccountActivity.this, dExchangeActivity.class);
                startActivity(ecgDiamonds);
            }
        });
        //helpdesk
        helpdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent helpintent = new Intent(UseraccountActivity.this,HelpdeskActivity.class);
                startActivity(helpintent);
            }
        });
        //youtybe
        youtubebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setData(Uri.parse("https://youtube.com/channel/UCHq0-Iy522b8SYIP1JWn4bQ"));
                startActivity(youtubeIntent);
            }
        });
        //privacybtn
        privacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privacyIntent = new Intent(Intent.ACTION_VIEW);
                privacyIntent.setData(Uri.parse("https://gamersfield.ml/privacypolicy.html"));
                startActivity(privacyIntent);
            }
        });

    }
}