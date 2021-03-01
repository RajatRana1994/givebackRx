package com.app.givebackrx.appcode.selectCharityModule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.charityDetail.CharityDetailActivity
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.CharityData
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.utils.extension.*
import kotlinx.android.synthetic.main.activity_select_charity.*
import kotlinx.android.synthetic.main.common_toolbar.*
import javax.inject.Inject

class SelectCharityActivity : BaseActivity(), View.OnClickListener, ISelectCharityView,
    PairListCLickListener, AdapterView.OnItemSelectedListener {

    var pages = 1
    var currentPage = 1

    companion object {
        var signup_extra : HashMap<String, Any>? = null
    }

    val mCharityCategories by lazy { mutableListOf<String>() }
    lateinit var mCategoriesAdapter: ArrayAdapter<String>
    val mCharityAlpha by lazy { mutableListOf<String>() }
    lateinit var mAlphaAdapter: ArrayAdapter<String>
    val mCharityState by lazy { mutableListOf<String>() }
    lateinit var mStateAdapter: ArrayAdapter<String>
    val mCharityList by lazy { mutableListOf<CharityData>() }
    val charityAdapter by lazy { SelectCharityAdapter(
        this,
        mCharityList,
        this,
        false
    ) }

    @Inject
    lateinit var presenter: SelectCharityPresenter<ISelectCharityView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_charity)
        presenter.onAttach(this)
        ivBack.setOnClickListener(this)
        tvSkip.setOnClickListener(this)
        tvTitle.setText("Select a Charity")
        setAdapters()
        editText.addTextChangedListener {
            getCharities(it)
        }
        getCharities(editText.text.toString())

        rvCharity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (pages > currentPage)
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mCharityList.size - 2) {
                        getCharities(editText.text.toString())
                    }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })
    }

    private fun setAdapters() {
        mCharityState.add("Select State")
        mCharityAlpha.add("Sort by")
        mCharityCategories.add("Select Category")
        mCategoriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mCharityCategories)
        mAlphaAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mCharityAlpha)
        mStateAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mCharityState)
        rvCharity.adapter = charityAdapter
        spinnerAlpha.adapter = mAlphaAdapter
        spinnerCategory.adapter = mCategoriesAdapter
        spinnerState.adapter = mStateAdapter
    }
     fun setListerersForSpinner(){
         spinnerAlpha.onItemSelectedListener = this
         spinnerCategory.onItemSelectedListener = this
         spinnerState.onItemSelectedListener = this
     }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> finish()
            R.id.tvSkip -> {
                AllinOneDialog(ttle = "Select a Charity",
                    msg = "A charity must be selected to register as a give<b>back</b>Rx member. Please choose a charity or agree to give<b>back</b>Rx's default charity. Thank you.",
                    onLeftClick = {/*btn No click*/ },
                    onRightClick = {/*btn Yes click*/
                        //perform postsignup
                        signup_extra!!.put("charity_id", "")
                        signup_extra!!.put("next_page", true)
                        if (isInternetConnected())
                            presenter.signup(signup_extra!!)
                    }
                )
            }
        }
    }

    override fun onGotCharityList(it: CharityListEntity) {
        try {
            if (it.data.pages.size>0)
            pages = it.data.pages[0]
            currentPage = it.data.current_page?:0

        if (currentPage == 1) {
            mCharityState.clear()
            mCharityState.add("Select State")
            mCharityState.addAll(it.data.states)
            mStateAdapter.notifyDataSetChanged()

            mCharityAlpha.clear()
            mCharityAlpha.add("Sort by")
            mCharityAlpha.addAll(it.data.sort_by_alphabet)
            mAlphaAdapter.notifyDataSetChanged()

            mCharityCategories.clear()
            mCharityCategories.add("Select Category")
            mCharityCategories.addAll(it.data.categories)
            mCategoriesAdapter.notifyDataSetChanged()

            mCharityList.clear()
        }
        } catch (e: Exception) {
        }
        if (it.data.charities.size > 0) {
            it.data.charities.forEach {
                it.selected = false
                mCharityList.add(it)
            }
        }else {
            toast(it.message, false)
        }
        charityAdapter.notifyDataSetChanged()
        setListerersForSpinner()
    }

    override fun onSignupResponse(it: SignInWithUserDetailEntity) {
        Intent(this@SelectCharityActivity, VerifyOTPActivity::class.java).apply {
            putExtra("value_extra", if (signup_extra!!.get("type")!!.equals("phone")) signup_extra!!.get("phone") as String else signup_extra!!.get("email") as String)
            putExtra("type_extra", signup_extra!!.get("type") as String)
            putExtra("page_extra", "signUp")
            putExtra("otp_extra", it.data.otp.toString())
            startActivity(this)
        }

    }

    override fun onGeneratedToken(lastAction: String) {
    }

    override fun onFirstListClick(item: Any, position: Int) {
        AllinOneDialogWithThreeBtn(ttle = "${mCharityList[position].charity_name}",
            msg = "You have selected this charity to receive donations, would you like to proceed?",
            btnBottom = "View Charity Card",
            onLeftClick = {/*btn No click*/ },
            onRightClick = {/*btn Yes click*/
                //perform postsignup
                signup_extra!!.put("charity_id", mCharityList[position].charity_id)
                signup_extra!!.put("next_page", true)
                if (isInternetConnected())
                    presenter.signup(signup_extra!!)
            },onBottomClick = {
                onSecondListClick(mCharityList[position],position)
            }
        )
    }

    override fun onSecondListClick(item: Any, position: Int) {
        Intent(this@SelectCharityActivity, CharityDetailActivity::class.java).apply {
            putExtra("charity_id_extra", mCharityList[position].charity_id)
            putExtra("charity_name_extra", mCharityList[position].charity_name)
            startActivity(this)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View, p2: Int, p3: Long) {
        getCharities(editText.text.toString())
    }

    fun getCharities(charity_name: String) {
        var alpha = ""
        var Category = ""
        var State = ""
        if ((spinnerAlpha.selectedItem as String).equals("Sort by").not()) {
            alpha = spinnerAlpha.selectedItem as String
        }
        if ((spinnerCategory.selectedItem as String).equals("Select Category").not()) {
            Category = spinnerCategory.selectedItem as String
        }
        if ((spinnerState.selectedItem as String).equals("Select State").not()) {
            State = spinnerState.selectedItem as String
        }
        if (isInternetConnected())
            presenter.getCharityList(
                alpha,
                currentPage.toString(),
                "",
                Category,
                State,
                "",
                charity_name
            )
    }
}
