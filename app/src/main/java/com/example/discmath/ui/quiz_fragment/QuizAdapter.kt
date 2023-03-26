package com.example.discmath.ui.quiz_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.quizzes.*
import com.google.android.material.bottomsheet.BottomSheetDialog

private fun loadAnswerInto(quiz: FourChoicesQuiz, answerIndex: Int,
                           imageView: ImageView, loader: RequestManager) {
    val solutionData = quiz.solutionsUrl[answerIndex]
    if (solutionData !is Map<*, *>) {
        throw Exception("Incorrect quiz data format")
    }
    val url: String = solutionData[SOLUTION_URL_KEY] as String
    loadImageIntoViewFrom(url, imageView, loader)
}

fun loadAnswerInto(quiz: FourChoicesQuiz, answerIndex: Int, imageView: ImageView,
                   loader: RequestManager, makeVisible: Boolean) {
    loadAnswerInto(quiz, answerIndex, imageView, loader)
    if (makeVisible) {
        imageView.visibility = View.VISIBLE
    }
}

fun loadAnswersIntoViews(quiz: FourChoicesQuiz, imageViews: Array<ImageView>,
                         loader: RequestManager, makeVisible: Boolean) {
    for((index, value) in imageViews.withIndex()) {
        loadAnswerInto(quiz, index, value, loader, makeVisible)
    }
}

class QuizAdapter(private val dataSet: MutableList<Quiz>,
                  private val correctAnswerCallback:  (() -> Unit),
                private val incorrectAnswerCallback: (() -> Unit)):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    abstract class QuizViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var problemImage: ImageView
    }

    class YesNoViewHolder(view: View): QuizViewHolder(view) {

        val yesOptionButton: Button
        val noOptionButton: Button

        init {
            problemImage = view.findViewById(R.id.problem_image_yes_no_quiz)
            yesOptionButton = view.findViewById(R.id.yes_button_yes_no_quiz)
            noOptionButton = view.findViewById(R.id.no_button_yes_no_quiz)
        }

    }

    class MultipleChoiceViewHolder(view: View): QuizViewHolder(view) {

        val firstOption: ImageView
        val secondOption: ImageView
        val thirdOption: ImageView
        val fourthOption: ImageView

        var hasAttempted: Boolean = false

        init {
            problemImage = view.findViewById(R.id.problem)
            firstOption = view.findViewById(R.id.answer1)
            secondOption = view.findViewById(R.id.answer2)
            thirdOption = view.findViewById(R.id.answer3)
            fourthOption = view.findViewById(R.id.answer4)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].quizType.ordinal
    }

    fun nextQuiz() {
        dataSet.removeFirst()
        notifyItemRemoved(0)
    }

    fun isTheLastQuiz(): Boolean {
        return dataSet.size == 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            QuizType.MULTIPLE_CHOICE.ordinal -> {
                val view = inflater.
                inflate(R.layout.fragment_quiz_multiple_choice, parent, false)
                MultipleChoiceViewHolder(view)
            }
            QuizType.YES_NO.ordinal -> {
                val view = inflater.
                        inflate(R.layout.fragment_yes_no_quiz, parent, false)
                YesNoViewHolder(view)
            }
            else -> {throw java.lang.RuntimeException()}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO: Consider this line carefully
        val loader: RequestManager = Glide.with(holder.itemView)

        val quiz: Quiz = dataSet[position]
        if (holder !is QuizViewHolder) {
            throw Exception("Unknown holder")
        }
        quiz.loadProblemInto(imageView = holder.problemImage,
            loader = loader,
            makeVisible = true)
        when (holder) {
            is MultipleChoiceViewHolder -> {
                quiz as FourChoicesQuiz
                val imageViews = arrayOf(holder.firstOption, holder.secondOption,
                    holder.thirdOption, holder.fourthOption)
                loadAnswersIntoViews(quiz, imageViews, loader, true)

                for ((index, solutionData) in quiz.solutionsUrl.withIndex()) {
                    val imageView = imageViews[index]
                    if (solutionData !is Map<*, *>) {
                        throw Exception("Incorrect quiz data format")
                    }
                    if (index == quiz.correctAnswerIndex) {
                        imageView.setOnClickListener {
                            if (!holder.hasAttempted) {
                                correctAnswerCallback()
                                Toast.makeText(holder.itemView.context,
                                    "Correct!", Toast.LENGTH_SHORT).show()
                            } else {
                                incorrectAnswerCallback()
                                Toast.makeText(holder.itemView.context,
                                    "Incorrect!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        holder.hasAttempted = true
                        val explanation: String = (solutionData[SOLUTION_EXPLANATION_KEY] as String?)
                            ?: DEFAULT_EXPLANATION
                        val dialog = BottomSheetDialog(imageView.context)

                        imageView.setOnClickListener {
                            dialog.setContentView(R.layout.incorrect_answer_dialog_layout)
                            val explanationTextView =
                                dialog.findViewById<TextView>(R.id.explanation_text)
                            explanationTextView?.text = explanation
                            dialog.show()
                            imageView.isClickable = false
                            imageView.alpha = FADED_ANSWER_ALPHA
                        }
                    }
                }

            }
            is YesNoViewHolder -> {
                quiz as YesNoQuiz
                val correctAnswer: Button
                val wrongAnswer: Button
                if (quiz.correctAnswerIndex == 1) {
                    correctAnswer = holder.yesOptionButton
                    wrongAnswer = holder.noOptionButton
                } else {
                    correctAnswer = holder.noOptionButton
                    wrongAnswer = holder.yesOptionButton
                }
                correctAnswer.setOnClickListener {
                    correctAnswerCallback()
                    Toast.makeText(holder.itemView.context,
                        "Correct!", Toast.LENGTH_SHORT).show()
                }
                wrongAnswer.setOnClickListener {
                    incorrectAnswerCallback()
                    Toast.makeText(holder.itemView.context,
                        "Incorrect!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

}