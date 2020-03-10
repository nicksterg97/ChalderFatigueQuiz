package com.example.xdfg.questionnaire;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QuizActivity extends AppCompatActivity {
    private Button answer1, answer2, answer3, answer4, answer5;
    private TextView score, question;
    private int myscore = 0;
    int question_num = 0;
    private QuizLibrary q = new QuizLibrary();
    final Context context = this;
    private Button button;
    private EditText result;
    private EditText editTextResult;
    private String promptName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);



        score = (TextView) findViewById(R.id.score); //to EditText pou mpainei to score tou xristi
        question = (TextView) findViewById(R.id.question); //to editext pou mpainei i erotisi
        //result = (EditText) findViewById(R.id.editTextResult);
        editTextResult = (EditText) findViewById(R.id.editTextResult); //to editText pou vazei o xristis to onoma tou sto prompt
        answer1=(Button) findViewById(R.id.choice1);
        answer2=(Button) findViewById(R.id.choice2);
        answer3=(Button) findViewById(R.id.choice3);
        answer4=(Button) findViewById(R.id.choice4);
        answer5=(Button) findViewById(R.id.choice5);

        question.setText(q.getQuestion(question_num));
        question_num++;

        starterPrompt();


        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateQuestion(question_num);
                question_num++;
                myscore = myscore + 1;
                score.setText(Integer.toString(myscore));
            }

        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion(question_num);
                question_num++;
                myscore = myscore + 2;
                score.setText(Integer.toString(myscore));

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateQuestion(question_num);
                question_num++;

                myscore = myscore + 3;
                score.setText(Integer.toString(myscore));

            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateQuestion(question_num);
                question_num++;
                myscore = myscore + 4;
                score.setText(Integer.toString(myscore));


            }
        });
        answer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion(question_num);
                question_num++;
                updateQuestion(question_num);
                question_num++;

            }
        });
    }


    public void starterPrompt() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput); //to editText gia na valei onoma o xristis sto prompt

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                promptName = (userInput.getText()).toString();
                                if(promptName==null || promptName.equals(""))
                                {
                                    starterPrompt();
                                }
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    public File exportData() {
        // Create a ZephyrLogs folder in external storage if it doesn't already exist
        File zephyrlogFolder = new File(Environment.getExternalStorageDirectory(), "QuestionnaireActs");

        boolean dirExists = zephyrlogFolder.exists();
        //if the directory doesn't exist, create it
        if (!dirExists) {
            dirExists = zephyrlogFolder.mkdirs();
            //if it still doesn't exist, give up and exit
            if (!dirExists) {
                Toast.makeText(this, "Could not create ZephyrLogs directory!", Toast.LENGTH_LONG).show();
            }
        }


        //create a data file and write into it
        File file = new File(zephyrlogFolder, "Questionnaire_"+promptName+".txt");
        try {
            FileWriter writer;
            if(!file.exists()){
                boolean created = file.createNewFile();
                if (!created) throw new IOException("Could not create data file");
                writer = new FileWriter(file, true);
                //if this is a new file, write the CSV format at the top
                writer.write("Ονοματεπώνυμο: "+promptName+"\n"+"Score: "+myscore+"\n\n");
            } else {
                writer = new FileWriter(file, true);
            }
            writer.write("Ονοματεπώνυμο: "+promptName+"\n"+"Score: "+myscore+"\n\n");
            writer.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Could not create logging file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Unsupported encoding exception thrown trying to write file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "IO Exception trying to write to data file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return file;
    }


    public void updateQuestion(int question_num) {
        if (question_num == 14) {
            exportData();
            Toast.makeText(this, "Επιτυχής Δημιουργία Αρχείου!", Toast.LENGTH_LONG).show();

            if(myscore>50)
            {
                Toast.makeText(this, "Είστε υπερβολικά κουρασμένος δεν μπορείτε να συμμετάσχετε στο quiz!", Toast.LENGTH_LONG).show();
                return;
            }
            else
            {
                Toast.makeText(this, "Mπορείτε να συμμετάσχετε στο quiz!", Toast.LENGTH_LONG).show();
                return;
            }


        }
        else if(question_num<=13) {
            question.setText(q.getQuestion(question_num));
        }
        else
        {
            Toast.makeText(this, "Τέλος Ερωτηματολογίου!", Toast.LENGTH_LONG).show();
        }


    }



}
