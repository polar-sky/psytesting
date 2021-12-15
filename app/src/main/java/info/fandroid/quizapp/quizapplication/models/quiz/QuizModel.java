package info.fandroid.quizapp.quizapplication.models.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuizModel implements Parcelable {
    String question;
    ArrayList<String> answers;
    String questinCategoryId;
    ArrayList<String> backgroundColors;

    public QuizModel(String question, ArrayList<String> answers, String questinCategoryId) {
        this.question = question;
        this.answers = answers;
        this.questinCategoryId = questinCategoryId;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getQuestingCategoryId() {
        return questinCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeList(answers);
        dest.writeString(questinCategoryId);
        dest.writeList(backgroundColors);
    }

    protected QuizModel(Parcel in) {
        question = in.readString();
        in.readList(answers, QuizModel.class.getClassLoader());
        questinCategoryId = in.readString();
        in.readList(backgroundColors, QuizModel.class.getClassLoader());
    }

    public static Creator<QuizModel> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<QuizModel> CREATOR = new Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel source) {
            return new QuizModel(source);
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };

}