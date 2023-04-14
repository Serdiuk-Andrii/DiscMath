package com.example.discmath.ui.quiz_fragment

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.quizzes.*
import com.example.set_theory.RPN.RPN
import com.example.set_theory.RPN.SetEvaluator
import com.example.set_theory.RPN.SetTheoryOperatorComparator
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Set<Char>.getSetRepresentation(): String {
    return if (this.isEmpty()) "∅" else '{' + this.joinToString() + '}'
}

private fun loadAnswerInto(quiz: FourChoicesQuiz, answerIndex: Int,
                           imageView: ImageView, loader: RequestManager) {
    val solutionData = quiz.solutionsUrl[answerIndex]
    if (solutionData !is Map<*, *>) {
        throw Exception("Incorrect quiz data format")
    }
    val url: String = solutionData[SOLUTION_URL_KEY] as String
    loadImageIntoViewFrom(url, imageView, loader)
}

const val CORRECT_ANSWER_TITLE = "Правильно!"
const val CORRECT_ANSWER_DESCRIPTION = "Так тримати!"

const val INCORRECT_ANSWER_TITLE = "Неправильно!"

const val OK = "OK"

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

    class SetEquationViewHolder(view: View): QuizViewHolder(view) {

        val setsRecyclerView: RecyclerView
        val universalSetPermanentEditText: TextView
        val universalSetAdditionalEditText: EditText
        val verifyButton: Button

        lateinit var leftSetRPN: RPN<Set<Char>>
        lateinit var rightSetRPN: RPN<Set<Char>>

        init {
            problemImage = view.findViewById(R.id.set_problem)
            universalSetPermanentEditText = view.findViewById(R.id.universal_set_permanent)
            universalSetAdditionalEditText = view.findViewById(R.id.universal_set_additional)
            setsRecyclerView = view.findViewById(R.id.sets_recycler_view)
            verifyButton = view.findViewById(R.id.set_verify_button)
        }
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

    class ImageQuizTextAnswerViewHolder(view: View): QuizViewHolder(view) {

        val editTextAnswer: EditText
        val verifyButton: Button
        var buttonHasNotMoved: Boolean

        init {
            problemImage = view.findViewById(R.id.text_answer_image_problem)
            editTextAnswer = view.findViewById(R.id.text_answer)
            verifyButton = view.findViewById(R.id.text_answer_verify_button)
            buttonHasNotMoved = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].quizType.ordinal
    }

    fun nextQuiz(): Quiz {
        dataSet.removeFirst()
        notifyItemRemoved(0)
        return dataSet[0]
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
            QuizType.SET_EQUATION.ordinal -> {
                val view = inflater.
                        inflate(R.layout.set_quiz, parent, false)
                SetEquationViewHolder(view)
            }
            QuizType.IMAGE_PROBLEM_TEXT_ANSWER.ordinal -> {
                val view = inflater.
                    inflate(R.layout.image_problem_text_answer, parent, false)
                ImageQuizTextAnswerViewHolder(view)
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
        val context = holder.itemView.context
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
                            } else {
                                incorrectAnswerCallback()
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
                }
                wrongAnswer.setOnClickListener {
                    incorrectAnswerCallback()
                }
            }
            is SetEquationViewHolder -> {
                quiz as SetEquationQuiz
                val comparator =
                    SetTheoryOperatorComparator()
                // Building the reverse polish notation for the both sides of the equation
                holder.leftSetRPN =
                    RPN(quiz.leftEquation, comparator)
                holder.rightSetRPN =
                    RPN(quiz.rightEquation, comparator)
                // Setting the click listener
                holder.verifyButton.setOnClickListener {
                    // Putting the entered values into the map
                    val map: MutableMap<Char, MutableSet<Char>> = HashMap()
                    holder.setsRecyclerView.forEach {
                        val editText: EditText = it.findViewById(R.id.edit_set_values)
                        map[editText.hint[0]] = editText.text.toString().
                            replace(REGEX_TO_REMOVE, "").toSet().toMutableSet()
                    }
                    val evaluator = SetEvaluator()
                    // If there is the universal set, read the given values
                   if (quiz.isUniversalSetRequired) {
                        var universalSet: Set<Char> = holder.universalSetPermanentEditText.
                        text.toString().replace(REGEX_TO_REMOVE, "").toCharArray().toSet()
                        val additionalElements: Set<Char> = holder.universalSetAdditionalEditText.
                            text.toString().replace(REGEX_TO_REMOVE, "").toCharArray().toSet()
                        universalSet = universalSet.union(additionalElements)
                        evaluator.addComplementToUniversalSet(universalSet)
                    }
                    val copy = mutableMapOf<Char, Set<Char>>()
                    for (entry in map) {
                        val copySet = entry.value.toSet()
                        copy[entry.key] = copySet
                    }
                    // Then, evaluate the expressions
                    val leftEvaluated: Set<Char> = holder.leftSetRPN.evaluate(evaluator,
                        copy as Map<Char, Set<Char>>
                    )
                    val rightEvaluated: Set<Char> = holder.rightSetRPN.evaluate(evaluator,
                        map as Map<Char, Set<Char>>
                    )
                    if (leftEvaluated == rightEvaluated) {
                        showSuccessDialog(context, CORRECT_ANSWER_TITLE, CORRECT_ANSWER_DESCRIPTION)
                    } else {
                        val leftSide: String = leftEvaluated.getSetRepresentation()
                        val rightSide: String = rightEvaluated.getSetRepresentation()
                        showFailureDialog(context, INCORRECT_ANSWER_TITLE, "$leftSide ≠ $rightSide")
                    }
                }
                val sets: Array<Char> = holder.leftSetRPN.operandsNames.
                    union(holder.rightSetRPN.operandsNames).toTypedArray()
                if (!quiz.isUniversalSetRequired) {
                    holder.universalSetPermanentEditText.visibility = View.GONE
                    holder.universalSetAdditionalEditText.visibility = View.GONE
                    holder.setsRecyclerView.adapter = SimpleSetInputAdapter(sets)
                } else {
                    val additionalElementsTextWatcher = UniversalSetAdditionalElementsTextWatcher(
                        holder.universalSetAdditionalEditText)
                    holder.universalSetAdditionalEditText.addTextChangedListener(
                        additionalElementsTextWatcher
                    )
                    val universalSetObserver = UniversalSetObserver(
                        holder.universalSetPermanentEditText, additionalElementsTextWatcher)
                    additionalElementsTextWatcher.setObserver(universalSetObserver)
                    holder.setsRecyclerView.adapter = BoundToUniversalSetInputAdapter(sets,
                        universalSetObserver)

                }
            }
            is ImageQuizTextAnswerViewHolder -> {
                quiz as ImageQuizTextAnswer
                holder.verifyButton.setOnClickListener {
                    val answer: String = holder.editTextAnswer.text.toString()
                    if (answer.isBlank()) {
                        val from = if(holder.buttonHasNotMoved) 0F else 180F
                        val to = if(holder.buttonHasNotMoved) 180F else 360F
                        val animation: Animation = RotateAnimation(from, to,
                            holder.verifyButton.pivotX, holder.verifyButton.pivotY)
                        animation.duration = 1500
                        animation.fillAfter = true
                        holder.buttonHasNotMoved = !holder.buttonHasNotMoved
                        holder.verifyButton.startAnimation(animation)
                    }
                    else if (answer == quiz.correctAnswer) {
                        showSuccessDialog(context, CORRECT_ANSWER_TITLE, CORRECT_ANSWER_DESCRIPTION)
                    } else {
                        showFailureDialog(context, INCORRECT_ANSWER_TITLE,
                            "$answer ≠ ${quiz.correctAnswer}")
                    }
                }
            }
        }
    }

    private fun showSuccessDialog(context: Context, title: String, description: String) {
        showDialog(context, title, description, correctAnswerCallback)
    }

    private fun showFailureDialog(context: Context, title: String, description: String) {
        showDialog(context, title, description, incorrectAnswerCallback)
    }

    private fun showDialog(context: Context, title: String, description: String,
                           callback:  (() -> Unit)) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title).setMessage(description)
            .setPositiveButton(OK) { dialog, _ ->
                dialog.dismiss()
                callback()
            }
            .create()
        alertDialog.show()
    }

    override fun getItemCount(): Int = dataSet.size
}
