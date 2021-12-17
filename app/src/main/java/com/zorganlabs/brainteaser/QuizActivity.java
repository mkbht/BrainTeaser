package com.zorganlabs.brainteaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zorganlabs.brainteaser.models.Leaderboard;
import com.zorganlabs.brainteaser.models.Quiz;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    DatabaseReference quizRef;
    String categoryName;
    ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    TextView question;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    Button startQuiz;
    LinearLayout loadLayout;
    LinearLayout quizLayout;
    TextView questionCount;
    ProgressBar progressBar;
    ProgressBar progressBarSuccess;
    TextView progressStatus;
    TextView progressStatusSuccess;
    TextView category;
    int count = 0;
    int correctAnswer = 0;
    String answer;
    LinearLayout successLayout;
    Button gotoHome;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // attach layout to java file
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().hide();

        databaseHandler = new DatabaseHandler(this);

        // grab category name
        Intent intent = getIntent();
        if (intent != null) {
            categoryName = intent.getStringExtra("CATEGORY_NAME");
        }
        quizRef = FirebaseDatabase.getInstance().getReference().child("quiz").child(categoryName).child("questions");
        // get quiz questions
        fetchQuiz();

        // get elements by id
        fetchElementsById();

        // update category name
        category.setText(categoryName);

        // load quiz for first time
        startQuiz.setOnClickListener(view -> {
            loadQuestion(count);
            count++;
            loadLayout.setVisibility(View.GONE);
            quizLayout.setVisibility(View.VISIBLE);
            successLayout.setVisibility(View.GONE);
        });

        // choose answer
        option1.setOnClickListener(view -> {
            selectOption(1, count - 1);
            loadQuestion(count);
            count++;
        });
        option2.setOnClickListener(view -> {
            selectOption(2, count - 1);
            loadQuestion(count);
            count++;
        });
        option3.setOnClickListener(view -> {
            selectOption(3, count - 1);
            loadQuestion(count);
            count++;
        });
        option4.setOnClickListener(view -> {
            selectOption(4, count - 1);
            loadQuestion(count);
            count++;
        });

        // redirect to home
        gotoHome.setOnClickListener(view -> {
            Intent intent1 = new Intent(QuizActivity.this, HomeActivity.class);
            startActivity(intent1);
            finish();
        });
    }

    public void fetchElementsById() {
        // fetch all the elements by id
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        startQuiz = findViewById(R.id.startQuiz);
        gotoHome = findViewById(R.id.gotoHome);
        loadLayout = findViewById(R.id.loadLayout);
        quizLayout = findViewById(R.id.quizLayout);
        successLayout = findViewById(R.id.successLayout);
        progressBar = findViewById(R.id.progress_circular_id);
        progressBarSuccess = findViewById(R.id.progressBar);
        progressStatus = findViewById(R.id.textview_progress_status_id);
        progressStatusSuccess = findViewById(R.id.progressStatus);
        questionCount = findViewById(R.id.questionCount);
        category = findViewById(R.id.category);
    }

    private void fetchQuiz() {
        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot quizSnapShot : snapshot.getChildren()) {
                    // get Quiz and add to quizList
                    Quiz quiz = quizSnapShot.getValue(Quiz.class);
                    quizList.add(quiz);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadQuestion(int position) {
        // continue till all the questions are answered
        if (position < 10) {
            Quiz quiz = quizList.get(position);
            if (quiz != null) {
                // set question and options
                question.setText(quiz.getQuestion());
                option1.setText(quiz.getOption1());
                option2.setText(quiz.getOption2());
                option3.setText(quiz.getOption3());
                option4.setText(quiz.getOption4());
                // set progress
                progressBar.setProgress(correctAnswer * 10);
                progressStatus.setText(String.valueOf(correctAnswer));
                questionCount.setText((position + 1) + "/10");
                // fetch answer
                answer = quiz.getAnswer();
            }
        }
    }

    public void selectOption(int choice, int position) {
        Quiz quiz = quizList.get(position);
        String choiceAnswer;
        // get chosen answer
        switch (choice) {
            case 1:
                choiceAnswer = quiz.getOption1();
                break;
            case 2:
                choiceAnswer = quiz.getOption2();
                break;
            case 3:
                choiceAnswer = quiz.getOption3();
                break;
            case 4:
                choiceAnswer = quiz.getOption4();
                break;
            default:
                choiceAnswer = "";
                break;
        }

        if (position > 9) {
            // finish the process on last questions
            Intent intent1 = new Intent(QuizActivity.this, HomeActivity.class);
            startActivity(intent1);
            finish();
        } else if (position == 9) {
            if (choiceAnswer.equals(answer)) {
                correctAnswer++;
            }
            // hide questions and show success view
            loadLayout.setVisibility(View.GONE);
            quizLayout.setVisibility(View.GONE);
            successLayout.setVisibility(View.VISIBLE);
            progressBarSuccess.setProgress(correctAnswer * 10);
            progressStatusSuccess.setText(String.valueOf(correctAnswer));

            // save score to shared preference
            SharedPreferences sharedPref = getSharedPreferences("scores", Context.MODE_PRIVATE);
            int rewardPoints = sharedPref.getInt("REWARD_POINTS", 0);
            int correct = sharedPref.getInt("CORRECT", 0);
            int mistakes = sharedPref.getInt("MISTAKES", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("REWARD_POINTS", correctAnswer * 100 - (10 - correctAnswer) * 10 + rewardPoints);
            editor.putInt("CORRECT", correctAnswer + correct);
            editor.putInt("MISTAKES", (10 - correctAnswer) + mistakes);
            editor.commit();

            // save score to leaderboard
            Leaderboard leaderboard = new Leaderboard(correctAnswer, (10 - correctAnswer),  categoryName);
            boolean inserted = databaseHandler.insertLeaderBoard(leaderboard);
        } else {
            // increment the count of correct ans if it matches
            if (choiceAnswer.equals(answer)) {
                correctAnswer++;
            }
        }
    }
}