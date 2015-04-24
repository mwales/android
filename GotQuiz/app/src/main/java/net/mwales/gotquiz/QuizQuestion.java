package net.mwales.gotquiz;

/**
 * Created by mwales on 4/20/15.
 */
public class QuizQuestion {

    public QuizQuestion(int questionId, boolean answerIsTrue)
    {
        setQuestionId(questionId);
        setAnswerTrue(answerIsTrue);
        setCheated(false);
    }
    public int getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(int questionId) {
        mQuestionId = questionId;
    }

    public boolean getAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    private int mQuestionId;

    private boolean mAnswerTrue;

    public boolean getCheated()
    {
        return mCheated;
    }

    public void setCheated(boolean cheated)
    {
        mCheated = cheated;
    }

    private boolean mCheated;

}
