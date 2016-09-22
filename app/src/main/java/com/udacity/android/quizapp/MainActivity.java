package com.udacity.android.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private EditText mNameText;

    private StringBuffer mMsgWhoAmI; // who am I?
    private StringBuffer mMsgImprove; // where can I improve?
    // As questão do nome EditView e do RadioButton são obrigatório e não valem pontos; as CheckBox valem pontos
    private int mScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        mNameText = (EditText) findViewById(R.id.firstName);
        mMsgWhoAmI  = new StringBuffer();
        mMsgImprove = new StringBuffer();
        mScore = 0;
    }


    public void agreeCheckOnClick(View view) {
        LinearLayout healthLayout = (LinearLayout) findViewById(R.id.healthLayout);
        LinearLayout jobsLayout   = (LinearLayout) findViewById(R.id.jobsLayout);
        LinearLayout selfLayout   = (LinearLayout) findViewById(R.id.selfLayout);
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        CheckBox agreeCheck = (CheckBox) findViewById(R.id.checkboxAgree);
        boolean isAgree = agreeCheck.isChecked() && validate(mNameText);
        Log.i(TAG, "isAgree: " + isAgree);
        visibility(healthLayout, isAgree);
        visibility(jobsLayout, isAgree);
        visibility(selfLayout, isAgree);
        visibility(buttonLayout, isAgree);
    }


    public void sumitOnClick(View view) {
        checkboxMarked();
        int rg = radioGroupMarked(mMsgWhoAmI);
        radioGroupMarked(mMsgImprove);

        String message = null;
        switch (view.getId()) {
            case R.id.submit:
                message = "Como estou?\n\n\n" + mMsgWhoAmI.toString() + "\n\n\nPontuação: " + mScore;
                break;
            case R.id.submitImprove:
                message = "Em primeira pessoa, como posso ser melhor?\n\n\n" + mMsgImprove.toString() + "\n\n\nPontuação: " + mScore;
                break;
        }

        String name = mNameText.getText().toString();
        if (rg != -1) {
            sendByEmail(name, message);
            if (mScore == 11) {
                Toast.makeText(this, name + ", Parabéns, você aceitou todas as questões :)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, name + ", você acertou " + mScore + "/11", Toast.LENGTH_LONG).show();
            }
        }

        mScore = 0;
    }


    private void sendByEmail(String name, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name) );
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    /**
     * Este metódo captura todas as CheckBox marcadas e não-marcadas
     * Obseve que não verifico quando cada uma das CheckBox's é tocado, mas sim
     * uma única vez, quando este método é chamado pela sumitOnClick
     * As questões certas são aquelas que são marcadas, quando se ganha pontos
     * As questões erradas são aquelas que não são marcadas, quando não se ganha pontos
     */
    private void checkboxMarked() {
        CheckBox checkBox1  = (CheckBox) findViewById(R.id.checkbox1);
        CheckBox checkBox2  = (CheckBox) findViewById(R.id.checkbox2);
        CheckBox checkBox3  = (CheckBox) findViewById(R.id.checkbox3);
        CheckBox checkBox4  = (CheckBox) findViewById(R.id.checkbox4);
        CheckBox checkBox5  = (CheckBox) findViewById(R.id.checkbox5);
        CheckBox checkBox6  = (CheckBox) findViewById(R.id.checkbox6);
        CheckBox checkBox7  = (CheckBox) findViewById(R.id.checkbox7);
        CheckBox checkBox8  = (CheckBox) findViewById(R.id.checkbox8);
        CheckBox checkBox9  = (CheckBox) findViewById(R.id.checkbox9);
        CheckBox checkBox10 = (CheckBox) findViewById(R.id.checkbox10);
        CheckBox checkBox11 = (CheckBox) findViewById(R.id.checkbox11);
        ArrayList<CheckBox> checkList = new ArrayList<>();
        checkList.add(checkBox1);
        checkList.add(checkBox2);
        checkList.add(checkBox3);
        checkList.add(checkBox4);
        checkList.add(checkBox5);
        checkList.add(checkBox6);
        checkList.add(checkBox7);
        checkList.add(checkBox8);
        checkList.add(checkBox9);
        checkList.add(checkBox10);
        checkList.add(checkBox11);
        for (CheckBox view : checkList) {
            if (view.isChecked()) {
                mMsgWhoAmI.append(view.getText() + " \n");
                mScore += 1;
            } else {
                mMsgImprove.append(view.getText() + " \n");
            }
        }
    }


    /**
     * As mensagem somente podem ser enviada por email se o usuário seleciona uma das alternativas do Autoconhecimento
     * Senão, retorna -1, um dialogo alerta
     * @param messageLine
     * @return
     */
    private int radioGroupMarked(StringBuffer messageLine) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int canSend = radioGroup.getCheckedRadioButtonId();
        Log.i(TAG, "canSend: " + canSend);
        if (canSend == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Autoconhecimento");
            builder.setMessage("Questão obrigatória: marque uma alternativa!");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            RadioButton rgButton = (RadioButton) findViewById(canSend);
            messageLine.append(getString(R.string.question_title));
            messageLine.append(" \n");
            messageLine.append(getString(R.string.question_self));
            messageLine.append(" \n");
            messageLine.append(rgButton.getText());
        }
        return canSend;
    }


    private void visibility(LinearLayout linear, boolean is) {
        linear.setVisibility(is ? View.VISIBLE: View.GONE);
    }


    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() < 1) {
            editText.setError("Vamos lá!");
            editText.requestFocus();
            return false;
        }
        return true;
    }


}
