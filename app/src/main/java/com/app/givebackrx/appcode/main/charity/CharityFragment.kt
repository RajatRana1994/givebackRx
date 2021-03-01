package com.app.givebackrx.appcode.main.charity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.charitydetail.CharityDetailFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.profileModule.ProfileFragment
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityActivity
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityAdapter
import com.app.givebackrx.appcode.settings.mycharity.MyCharityFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.CharityData
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import kotlinx.android.synthetic.main.fragment_charity.*
import javax.inject.Inject


class CharityFragment : BaseFragment(), ICharityView, PairListCLickListener, View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    //Update Charity=> Would you like to select this charity?
    var pages = 1
    var currentPage = 1
    var selectedPosition = -1

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charity, container, false)
    }


    val mCharityCategories by lazy { mutableListOf<String>() }
    lateinit var mCategoriesAdapter: ArrayAdapter<String>
    val mCharityAlpha by lazy { mutableListOf<String>() }
    lateinit var mAlphaAdapter: ArrayAdapter<String>
    val mCharityState by lazy { mutableListOf<String>() }
    lateinit var mStateAdapter: ArrayAdapter<String>
    val mCharityCity by lazy { mutableListOf<String>() }
    lateinit var mCityAdapter: ArrayAdapter<String>
    val mCharityList by lazy { mutableListOf<CharityData>() }
    val charityAdapter by lazy {
        SelectCharityAdapter(
            requireActivity(),
            mCharityList,
            this,
            mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity")
        )
    }
    var get_value = ""

    @Inject
    lateinit var presenter: CharityPresenter<ICharityView>


    override fun setUp(view: View) {
        val frag_value = arguments
        if (frag_value!!.containsKey("from_dashboard")) {
            get_value = frag_value!!.getString("from_dashboard").toString()
            if (get_value.equals("yes")) {
                ivBack.visibility = View.VISIBLE
                tvTitleCenter.visibility = View.VISIBLE
                tvTitle.visibility = View.INVISIBLE
            } else {
                ivBack.visibility = View.INVISIBLE
                tvTitleCenter.visibility = View.INVISIBLE
                tvTitle.visibility = View.VISIBLE
            }
        }


        presenter.onAttach(this)
        setAdapters()
        editText.delayWatcher(300) {
            Runnable {
                getCharities(it)
            }
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

        ivBack.setOnClickListener {
            Intent("UPDATE_USER_CHARITY").apply {
                putExtra("updated", true)
                LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                requireActivity().onBackPressed()
            }
        }

        tvLogin.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
        tvSignup.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
        tvSignup.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    PreSignUpCheckActivity::class.java
                )
            )
        }

        tvLogin.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    LoginEmailPasswordActivity::class.java
                )
            )
        }
        ivUserImage.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
            ivUserImage.loadImageRadius(mAppUtils.getUserData(mPrefs)!!.profile_pic ?: "", 100)
        ivUserImage.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                ProfileFragment()
            )
        }

    }

    private fun setAdapters() {
        mCharityState.add("Select State")
        mCharityAlpha.add("Sort by")
        mCharityCity.add("Select City")
        mCharityCategories.add("Select Category")
        mCategoriesAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            mCharityCategories
        )
        mAlphaAdapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mCharityAlpha
            )
        mStateAdapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mCharityState
            )
        mCityAdapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mCharityCity
            )
        rvCharity.adapter = charityAdapter
        spinnerAlpha.adapter = mAlphaAdapter
        spinnerCategory.adapter = mCategoriesAdapter
        spinnerCity.adapter = mCityAdapter
        spinnerState.adapter = mStateAdapter
    }

    fun setListerersForSpinner() {
        spinnerAlpha.onItemSelectedListener = this
        spinnerCategory.onItemSelectedListener = this
        spinnerCity.onItemSelectedListener = this
        spinnerState.onItemSelectedListener = this
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvSkip -> {
                AllinOneDialog(ttle = "You have not selected a charity.",
                    msg = "Our current defualt charity is Giveback Charity. Would you like to proceed with that charity for now? You can change it in the future.",
                    onLeftClick = {/*btn No click*/ },
                    onRightClick = {/*btn Yes click*/
                        //perform postsignup
                        SelectCharityActivity.signup_extra!!.put("charity_id", "")
                        SelectCharityActivity.signup_extra!!.put("next_page", true)
//                        if (isInternetConnected())
//                            presenter.signup(SelectCharityActivity.signup_extra!!)
                    }
                )
            }
        }
    }


    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    companion object;

    override fun onFirstListClick(item: Any, position: Int) {
        val login = mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)
        AllinOneDialogWithThreeBtn(ttle = if (login) "Update Charity" else "Select Charity",
            msg = if (login) "Would you like to select this charity?" else "Please login to select this charity for donations.",
            btnLeft = "Cancel",
            btnRight = if (login) "Update" else "Sign In",
            btnBottom = "View Charity Card",
            onLeftClick = {/*btn No click*/ },
            onRightClick = {/*btn Yes click*/
                if (login) {
                    selectedPosition = position
                    if (isInternetConnected())
                        presenter.selectCharity(mCharityList[position].charity_id)
                } else {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            LoginEmailPasswordActivity::class.java
                        )
                    )
                }
            },
            onBottomClick = {
                onSecondListClick(mCharityList[position], position)
            }
        )
    }

    override fun onSecondListClick(item: Any, position: Int) {
        val frag = CharityDetailFragment()
        frag.arguments = Bundle().apply {
            putString("charity_id_extra", mCharityList[position].charity_id)
            putString("charity_name_extra", mCharityList[position].charity_name)
        }
        mainActivity.addMainFragment(R.id.homeContainer, frag)
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 == spinnerCategory) {
            spinnerAlpha.setSelection(0)
            spinnerCity.setSelection(0)
            spinnerState.setSelection(0)
            getCharities(editText.text.toString())
        } else getCharities(editText.text.toString())

    }

    override fun onGotCharityList(it: CharityListEntity) {
        try {
            if (it.data.pages.size > 0)
                pages = it.data.pages[0]
            currentPage = it.data.current_page ?: 0
            if (currentPage == 1) {
                mCharityCity.clear()
                mCharityCity.add("Select City")
                mCharityCity.addAll(it.data.cities)
                mCityAdapter.notifyDataSetChanged()

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
            mCharityList.addAll(it.data.charities)
        } else {
            toast(it.message, false)
        }
        charityAdapter.notifyDataSetChanged()

        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
        setListerersForSpinner()
        Intent("UPDATE_USER_CHARITY").apply {
            putExtra("updated", true)
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
        }
    }

    fun getCharities(charity_name: String) {
        var alpha = ""
        var Category = ""
        var city = ""
        var State = ""
        if ((spinnerAlpha.selectedItem as String).equals("Sort by").not()) {
            alpha = spinnerAlpha.selectedItem as String
        }
        if ((spinnerCategory.selectedItem as String).equals("Select Category").not()) {
            Category = spinnerCategory.selectedItem as String
        }
        if ((spinnerCity.selectedItem as String).equals("Select City").not()) {
            city = spinnerCity.selectedItem as String
        }
        if ((spinnerState.selectedItem as String).equals("Select State").not()) {
            State = spinnerState.selectedItem as String
        }
        if (isInternetConnected())
            presenter.getCharityList(
                alpha,
                currentPage.toString(),
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) mPrefs.getKeyValue(
                    PreferenceConstants.MEMBER_TYPE
                ) else
                    "",
                Category,
                State,
                city,
                charity_name
            )
    }


    override fun onSelectedCharity(it: SuccessMessageEntity) {
        if (get_value.equals("yes")) {
            Intent("UPDATE_USER_CHARITY").apply {
                putExtra("updated", true)
                LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                requireActivity().onBackPressed()
            }
        } else {
            getCharities(editText.text.toString())
            Intent("UPDATE_CHARITY").apply {
                putExtra("updated", true)
                LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
            }
        }
        toast(it.message)
        mainActivity.addMainFragment(R.id.homeContainer, MyCharityFragment())
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("UPDATE_CHARITY")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("updated")) {
                if (p1.getBooleanExtra("updated", true)) {
                    getCharities(editText.text.toString())
                }
            }
        }
    }
}