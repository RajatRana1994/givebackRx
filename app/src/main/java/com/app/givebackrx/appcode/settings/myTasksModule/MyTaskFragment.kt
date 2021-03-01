package com.app.givebackrx.appcode.main.settings.myTasksModule


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.settings.myTasksModule.IMyTaskView
import com.app.givebackrx.appcode.settings.myTasksModule.MyTaskPresenter
import com.app.givebackrx.appcode.settings.myTasksModule.TaskAdapter
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyTaskItem
import com.app.givebackrx.data.entity.TaskEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addWatcher
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_my_task.*
import java.lang.Exception
import javax.inject.Inject


class MyTaskFragment : BaseFragment(), IMyTaskView, SingleListCLickListener,
    AdapterView.OnItemSelectedListener {
    var count = 0
    val mTaskList = mutableListOf<MyTaskItem>()
    val mTaskAdapter by lazy { TaskAdapter(mTaskList, this) }

    @Inject
    lateinit var presenter: MyTaskPresenter<IMyTaskView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_task, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvTasks.adapter = mTaskAdapter
        if (isInternetConnected())
            presenter.task(spinnerTasks.selectedItem.toString())
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        ivSearch.setOnClickListener {
            etSearch.onOffVisibility(etSearch.visibility == View.GONE)
            closeSearch.onOffVisibility(closeSearch.visibility == View.GONE)
        }

        etSearch.addWatcher {
            mTaskAdapter.filter.filter(it)
        }

        closeSearch.setOnClickListener {
            closeSearch.onOffVisibility(false)
            etSearch.onOffVisibility(false)
        }

        spinnerTasks.onItemSelectedListener = this

        rvTasks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (count > 8)
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mTaskList.size - 2) {
                        if (isInternetConnected()) presenter.task(spinnerTasks.selectedItem.toString())
                    }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })
    }


    override fun onTaskResp(it: TaskEntity) {
        count = it.count
        mTaskList.clear()
        it.data.my_tasks.forEach {
            it.task_status =
                if (spinnerTasks.selectedItemPosition == 0) "Mark As Complete" else if (spinnerTasks.selectedItemPosition == 1) "Task Completed" else "Overdue"
            mTaskList.add(it)
        }
        mTaskAdapter.notifyDataSetChanged()
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

    override fun onSingleListClick(item: Any, position: Int) {
        if (isInternetConnected())
            presenter.markCompleteTask(mTaskList[position].task_id, position)

    }

    override fun onMarkTaskCompleted(it: TaskEntity, position: Int) {
        mTaskAdapter.notifyItemRemoved(position)
        toast(it.message)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){}
        }catch (e: Exception){}
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (isInternetConnected())
            presenter.task(spinnerTasks.selectedItem.toString())
    }


}
