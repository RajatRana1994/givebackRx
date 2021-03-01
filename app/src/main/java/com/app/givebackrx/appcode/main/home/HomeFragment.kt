package com.app.givebackrx.appcode.main.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailModule.LoginEmailActivity
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.pharmacy.PharmacyFragment
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.profileModule.ProfileFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.AppVersionEntity
import com.app.givebackrx.data.entity.SearchDrugData
import com.app.givebackrx.data.entity.SearchDrugEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.localdb.FreqSearchDataBs
import com.app.givebackrx.localdb.FreqSearchEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), SingleListCLickListener, IHomeView {
    /////////////
    private val TRIGGER_AUTO_COMPLETE: Int = 100
    private val AUTO_COMPLETE_DELAY: Long = 500
    private var handler: Handler? = null
    private val autoSuggestAdapter: AutoSuggestAdapter by lazy {
        AutoSuggestAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1
        )
    }
    var mHomeImage = true
    var CheckVersion=true
    val isPortalUser by lazy {
        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
    }
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity)
            mainActivity = context
    }

    //////////////
    val searchDrugData by lazy { arrayListOf<String>() }
    val searchDrugAllData by lazy { arrayListOf<SearchDrugData>() }

    val autocompleteAdapter by lazy {
        AutocompleteArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            searchDrugData
        )
    }
    val listPopupWindow by lazy { ListPopupWindow(requireActivity()) }

    val runnable by lazy {
        Runnable {
            if (isInternetConnected()) {
                searchDrugData.clear()
                autoSuggestAdapter.notifyDataSetChanged()
                presenter.searchDrugs(edtSearch.text.toString())
            }
        }
    }
    var mFreqSearchList = mutableListOf<FreqSearchEntity>()
    val mFrequentSearchAdapter by lazy { FrequentSearchAdapter(mFreqSearchList, this) }

    @Inject
    lateinit var presenter: HomePresenter<IHomeView>
    val searchDb by lazy { FreqSearchDataBs.freqSearchDb(requireActivity()).freqSearchDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            ivUserImage.switchVisibility(tvLogin)
            tvSignup.onOffVisibility(false)
            ivUserImage.loadImageRadius(mAppUtils.getUserData(mPrefs)!!.profile_pic ?: "", 100)
            ivUserImage.setOnClickListener {
                mainActivity.addMainFragment(
                    R.id.homeContainer,
                    ProfileFragment()
                )
            }
        } else tvLogin.switchVisibility(ivUserImage)
        if (CheckVersion)
        presenter.appVersion()
        //HomeImage
        homeBg.setImageResource(if (mHomeImage) R.drawable.ic_bg_home else R.drawable.home_bg_female)
        mHomeImage = mHomeImage.not()
        tvLogin.setOnSingleClickListener {
            edtSearch.setText("")
            goToActivity(LoginEmailPasswordActivity::class)
        }
        rvFrequentSearches.adapter = mFrequentSearchAdapter
        notifyLocalList()

        getGBRxCard.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    PreSignUpCheckActivity::class.java
                )
            )
        }
        tvSignup.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    PreSignUpCheckActivity::class.java
                )
            )
        }
        if (isPortalUser) {
            getGBRxCard.onOffVisibility(
                mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)
                    .not()
            )
//            getGBRxGoldCard.onOffVisibility(false)
            getGBRxGoldCard.onOffVisibility(
                mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN) && mPrefs.getKeyValue(
                    PreferenceConstants.MEMBER_TYPE
                ).toLowerCase(Locale.ENGLISH).equals("gold").not()
            )
        } else {
            getGBRxCard.onOffVisibility(false)
            getGBRxGoldCard.onOffVisibility(false)
        }

        getGBRxGoldCard.setOnClickListener {
            upgradeCard {
                val args = Bundle()
                args.putString("from_dashboard", "no")
                val paymentFragment = PaymentFragment()
                paymentFragment.setArguments(args)
                mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
            }
        }
        handler = Handler(Handler.Callback { msg ->
            if (msg.what === TRIGGER_AUTO_COMPLETE) {
                if (edtSearch != null)
                    if (!TextUtils.isEmpty(edtSearch.getText())) {
                        if (isInternetConnected()) presenter.searchDrugs(
                            edtSearch.getText().toString()
                        )
                    } else {
                        if (listPopupWindow.isShowing) listPopupWindow.dismiss()
                    }
            }
            false
        })

        edtSearch.addWatcher {
            if (it.isEmpty() && listPopupWindow.isShowing) listPopupWindow.dismiss()
            handler?.removeMessages(TRIGGER_AUTO_COMPLETE);
            handler?.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
        }

        /* edtSearch.setAdapter(autocompleteAdapter)
         edtSearch.delayWatcher { runnable }
         edtSearch.setDropDownBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.rounded_spinner))*/
    }


    private fun addLocally(searchDrugData: SearchDrugData) {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                var lastId = if (searchDb.getAll().isEmpty()) 0L else searchDb.getAll()
                    .get(searchDb.getAll().size - 1).id
                if (searchDb.getAll().size > 12) lastId = 0L
                searchDb.insert(
                    FreqSearchEntity(
                        id = if (lastId == 0L) 0L else (lastId + 1).toLong(),
                        drug_generic_name = searchDrugData.drug_generic_name,
                        display_drug_name = searchDrugData.display_drug_name,
                        drug_name = searchDrugData.drug_name
                    )
                )
            }
        }
        notifyLocalList()
    }

    private fun notifyLocalList() {
        mFreqSearchList.clear()
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val searchList = (searchDb.getAll() as MutableList<FreqSearchEntity>)
                requireActivity().runOnUiThread {
                    mFreqSearchList.addAll(searchList)
                    mFreqSearchList.sortByDescending { it.createdAt }
                    mFrequentSearchAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDrugSearched(it: SearchDrugEntity) {
        searchDrugData.clear()
        searchDrugAllData.clear()
        it.data.forEach {
            searchDrugData.add(it.display_drug_name)
            searchDrugAllData.add(it)
        }
//        if (searchDrugData.size > 0) {
//            autoSuggestAdapter.setData(searchDrugData)
//            edtSearch.setAdapter(autoSuggestAdapter)
//        }
        autocompleteAdapter.notifyDataSetChanged()
        if (listPopupWindow.isShowing.not())
            suggestionDialog()
    }

    fun suggestionDialog() {
        listPopupWindow.setAdapter(autocompleteAdapter)
        listPopupWindow.anchorView = edtSearch
        listPopupWindow.isModal = false
        listPopupWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.shape_rounded
            )
        )
        listPopupWindow.width = ListPopupWindow.WRAP_CONTENT
        listPopupWindow.height = ListPopupWindow.WRAP_CONTENT
        listPopupWindow.animationStyle = android.R.style.Animation_Toast
        listPopupWindow.promptPosition - ListPopupWindow.POSITION_PROMPT_ABOVE
        listPopupWindow.verticalOffset = -24
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            addLocally(searchDrugAllData[i])
            edtSearch.setText("")
            PharmacyFragment.drug_generic_name = searchDrugAllData[i].drug_generic_name
            PharmacyFragment.drug_name = searchDrugAllData[i].drug_name
            GBRxApp.mSavePharmacyFavData=null
            mainActivity.addMainFragment(R.id.homeContainer, PharmacyFragment())
            listPopupWindow.dismiss()
        }
//        if (listPopupWindow.isShowing) listPopupWindow.dismiss()
        listPopupWindow.show()
        if (edtSearch.hasFocus().not())
            edtSearch.requestFocus()
    }


    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onSingleListClick(item: Any, position: Int) {
        PharmacyFragment.drug_generic_name = mFreqSearchList[position].drug_generic_name
        PharmacyFragment.drug_name = mFreqSearchList[position].drug_name
        GBRxApp.mSavePharmacyFavData=null
        mainActivity.addMainFragment(R.id.homeContainer, PharmacyFragment())
    }

    override fun onAppVersionResp(it: AppVersionEntity) {
//        it.data.version="2"
        CheckVersion=false
        try {
            if (checkVersionCode() < it.data.version.toDouble() && checkVersionCode() >0) {
                AllinOneDialog(getString(R.string.app_name),
                    "New version of givebackRx is available on store. Please update your app with latest version.",
                    btnLeft = "Cancel",
                    btnRight = "Update",
                    onLeftClick = {},
                    onRightClick = {
                        if (isInternetConnected()) {
                            val i = Intent(android.content.Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.app.givebackrx"));
                            startActivity(i);
                        }
                    })
            }
        } catch (e: Exception) {

        }
    }

    private fun checkVersionCode(): Float {
        try {
            val pInfo = requireActivity().getPackageManager()
                .getPackageInfo(requireActivity().packageName, 0)
            return pInfo.versionCode.toFloat()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0.0f
    }

    override fun onResume() {
        super.onResume()
        MainActivity.canClose = true
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            ivUserImage.switchVisibility(tvLogin)
            ivUserImage.loadImageRadius(mAppUtils.getUserData(mPrefs)!!.profile_pic ?: "", 100)
            ivUserImage.setOnClickListener {
                mainActivity.addMainFragment(
                    R.id.homeContainer,
                    ProfileFragment()
                )
            }
        } else tvLogin.switchVisibility(ivUserImage)
    }


}