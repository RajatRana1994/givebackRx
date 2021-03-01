package com.app.givebackrx.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.givebackrx.listeners.CustomSpinnerListener;
import com.app.givebackrx.R;
import com.app.givebackrx.utils.DialogFile;

import java.util.List;
import javax.inject.Inject;

public class CustomListDialog {
    @Inject
    DialogFile mDialogsUtil;

    Dialog dialog;
    Context context;
    CustomSpinnerListener listener;
    List<String> mList;
    ListView rvListing;
    String value = "", warning = "";
    public ArrayAdapter<String> adapter;


    public CustomListDialog(Context context, List<String> mList,
                            CustomSpinnerListener listener, DialogFile mDialogsUtil) {
        this.context = context;
        this.listener = listener;
        this.mList = mList;
        this.mDialogsUtil = mDialogsUtil;
        showCustomDialog();
    }

    private void showCustomDialog() {
        dialog = mDialogsUtil.showDialogFix(context, R.layout.view_custom_list_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        rvListing = dialog.findViewById(R.id.rvListing);
        rvListing.setVisibility(View.VISIBLE);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mList);
        rvListing.setAdapter(adapter);
        rvListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(mList.get(position));
                dialog.dismiss();
            }
        });
    }

    public void showDialog() {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}
