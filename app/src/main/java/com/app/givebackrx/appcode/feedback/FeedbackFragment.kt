package com.app.givebackrx.appcode.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.givebackrx.R
import com.app.givebackrx.appcode.settings.FeedbackModule.FeedbackPresenter
import com.app.givebackrx.appcode.settings.FeedbackModule.IFeedbackView
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.utils.extension.makeEmpty
import com.app.givebackrx.utils.extension.toast
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.android.synthetic.main.fragment_feedback.*
import javax.inject.Inject


class FeedbackFragment : BaseFragment(), IFeedbackView {

    @Inject
    lateinit var presenter: FeedbackPresenter<IFeedbackView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnSubmit.setOnClickListener {
            if (ratingBar.rating < 0.5)
                toast(getString(R.string.ratetheapp), false)
            else if (edtFeedback.text.isEmpty())
                toast(getString(R.string.enterthesuggestion), false)
            else {
                if (isInternetConnected()) {
                    presenter.feedback(ratingBar.rating.toString(),edtFeedback.text.toString())
                }
            }
        }
    }

    override fun onFeedbackResp(it: SignInWithUserDetailEntity) {
        toast(it.message)
        if (ratingBar.rating.toInt()==5){
            showPlayStoreRatingPrompt()
        }
        ratingBar.rating=0f
        edtFeedback.makeEmpty()

    }

    private fun showPlayStoreRatingPrompt() {
        val reviewManager=ReviewManagerFactory.create(requireActivity())
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = request.result
                reviewInfo?.let {
                    val flow = reviewManager.launchReviewFlow(requireActivity(), it)
                    flow.addOnSuccessListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        toast(
                            getString(R.string.thanksforfeedback),
                            true
                        )
                    }
                    flow.addOnFailureListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        toast("${it.message}", false)
                    }
                    flow.addOnCompleteListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        toast(getString(R.string.rated), true)
                    }
                }
            } else {
                // There was some problem, continue regardless of the result.
            }
        }

    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }
}