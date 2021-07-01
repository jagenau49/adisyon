package com.catsoftware.adisyon.activities;

//TODO: continue to translate

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.catsoftware.adisyon.R;
import com.catsoftware.adisyon.db.AppDatabase;

import java.util.Random;

public class ResetDBActivity extends AppCompatActivity {
    TextView tvRndNum1, tvRndNum2;
    EditText etAnswer;
    Button btDeleteConfirm, btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_db);

        //Layout views assigned
        tvRndNum1 =findViewById(R.id.tvRndNum1);
        tvRndNum2 =findViewById(R.id.tvRndNum2);
        etAnswer =findViewById(R.id.etAnswer);
        btDeleteConfirm =findViewById(R.id.btDeleteConfirm);
        btCancel =findViewById(R.id.btCancelReset);

        //views updated
        int rdNum1= generateRandomNumber();
        int rdNum2= generateRandomNumber();
        int solution=rdNum1+rdNum2;
        tvRndNum1.setText(""+rdNum1);
        tvRndNum2.setText(""+rdNum2);
        btDeleteConfirm.setOnClickListener(v -> {
            String answerOfQuestion= etAnswer.getText().toString();
            if(answerOfQuestion.equals("")){ //user sent empty answer
                Toast.makeText(ResetDBActivity.this, "Bitte korrigieren Sie Ihre Antwort!", Toast.LENGTH_LONG).show();

            }else{ //user sent an answer
                int answerOfUser=Integer.parseInt(answerOfQuestion);
                if(answerOfUser==solution){ //user entered right answer
                    resetDb();

                }else {//falsh or empty answer
                    Toast.makeText(ResetDBActivity.this, "Bitte korrigieren Sie Ihre Antwort!", Toast.LENGTH_LONG).show();

                }

            }


        });
        btCancel.setOnClickListener(v -> finish());
    }
    public void resetDb() {

        //checks if user sure
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("Alle Daten werden gelöscht?");
        mAlert.setMessage("Sind Sie sicher? Diese Transaktion kann nicht rückgängig gemacht werden.");
        mAlert.setPositiveButton("Ja", (dialog, which) -> {

            AppDatabase db = AppDatabase.getDbInstance(this);
            db.clearAllTables();
            Intent intent=new Intent(ResetDBActivity.this, MainActivity.class);
            startActivity(intent);


            Toast.makeText(this, "Alle daten wurden gelöscht.", Toast.LENGTH_LONG).show();
            finish();
        });
        mAlert.setNegativeButton("Abbruch", (dialog, which) -> {
            //cancel

            finish();
        });
        mAlert.show();


    }
    private int generateRandomNumber() {
        Random random=new Random();
        return random.nextInt(90)+10;

    }
}