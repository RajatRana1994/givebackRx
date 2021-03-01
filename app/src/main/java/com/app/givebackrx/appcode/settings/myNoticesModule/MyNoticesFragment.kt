package com.app.givebackrx.appcode.main.settings.myNoticesModule


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.settings.myNoticesModule.IMyNoticesView
import com.app.givebackrx.appcode.settings.myNoticesModule.MyNoticesPresenter
import com.app.givebackrx.appcode.settings.myNoticesModule.NoticeAdapter
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyNoticeItem
import com.app.givebackrx.data.entity.NoticeEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addWatcher
import com.app.givebackrx.utils.extension.onDrawableClick
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_my_notices.*
import java.lang.Exception
import javax.inject.Inject


class MyNoticesFragment : BaseFragment(), IMyNoticesView, AdapterView.OnItemSelectedListener,
    SingleListCLickListener {

    val mNoticeList = mutableListOf<MyNoticeItem>()
    val mNoticeAdapter by lazy { NoticeAdapter(mNoticeList, this) }

    var count = 0

    @Inject
    lateinit var presenter: MyNoticesPresenter<IMyNoticesView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_notices, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvNotices.adapter = mNoticeAdapter
//        if (isInternetConnected())
//            presenter.notices(spinnerTasks.selectedItem.toString())
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        spinnerTasks.onItemSelectedListener = this
        ivSearch.setOnClickListener {
            etSearch.onOffVisibility(etSearch.visibility == View.GONE)
            closeSearch.onOffVisibility(closeSearch.visibility == View.GONE)
        }

        etSearch.addWatcher {
            mNoticeAdapter.filter.filter(it)
        }

         closeSearch.setOnClickListener {
            closeSearch.onOffVisibility(false)
            etSearch.onOffVisibility(false)
        }

        rvNotices.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (count > 8)
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mNoticeList.size - 2) {
                        if (isInternetConnected()) presenter.notices(spinnerTasks.selectedItem.toString())
                    }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })
    }


    override fun onNoticeResp(it: NoticeEntity) {
        count = it.count
        mNoticeList.addAll(it.data.my_notices)
        mNoticeAdapter.notifyDataSetChanged()
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){}
        }catch (e: Exception){}
    }

    override fun onNoticeAcknowledged(it: NoticeEntity, position: Int) {
        mNoticeList[position].notice_status = "Acknowledged"
        mNoticeAdapter.notifyItemChanged(position)
        toast(it.message)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){}
        }catch (e: Exception){}
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (isInternetConnected())
            presenter.notices(spinnerTasks.selectedItem.toString())
    }

    override fun onSingleListClick(item: Any, position: Int) {
        if (isInternetConnected())
            presenter.acknowledgeNotices(mNoticeList[position].notice_id, position)
    }


}
