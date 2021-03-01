package com.app.givebackrx.utils.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.*
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.app.givebackrx.GBRxApp.Companion.ON_CLICK_DELAY
import com.app.givebackrx.GBRxApp.Companion.lastTimeClicked
import com.app.givebackrx.R
import com.app.givebackrx.appcode.web.WebActivity
import com.app.givebackrx.utils.spanClickable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.util.*
import kotlin.reflect.KClass


fun AppCompatActivity.goToActivity(cls: KClass<*>) {
    startActivity(Intent(this, cls.java))
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

fun AppCompatActivity.addMainFragment(container: Int, frag: Fragment) {
    try {
        supportFragmentManager.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    supportFragmentManager.addFragment(container, frag)
}

fun AppCompatActivity.addMainFragment(container: View, frag: Fragment) {
    try {
        supportFragmentManager.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    supportFragmentManager.addFragment(container.id, frag)
}

fun Fragment.addMainFragment(container: View, frag: Fragment) {
    try {
        fragmentManager?.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    fragmentManager!!.addFragment(container.id, frag)
}

fun Fragment.addMainFragment(container: Int, frag: Fragment) {
    try {
        fragmentManager?.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    fragmentManager!!.addFragment(container, frag)
}

fun AppCompatActivity.changeMainFragment(container: View, frag: Fragment) {
    try {
        supportFragmentManager.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    supportFragmentManager.replaceFragment(container.id, frag)
}

fun AppCompatActivity.changeMainFragment(container: Int, frag: Fragment) {
    try {
        supportFragmentManager.executePendingTransactions()
    } catch (e: java.lang.Exception) {

    }
    supportFragmentManager.replaceFragment(container, frag)
}

fun AppCompatActivity.bottomSheetDialog(layout: Int, clicks: () -> Unit) {
    val mBottomSheetDialog = BottomSheetDialog(this)
    val sheetView: View = layoutInflater.inflate(layout, null)
    mBottomSheetDialog.setContentView(sheetView)
    clicks()
    mBottomSheetDialog.dismissWithAnimation = true
    mBottomSheetDialog.show()
}

fun Fragment.bottomSheetDialog(
    layout: Int,
    clicks: (sheetView: View, sheet: BottomSheetDialog) -> Unit
) {
    val mBottomSheetDialog = BottomSheetDialog((context as AppCompatActivity))
    val sheetView: View = (context as AppCompatActivity).layoutInflater.inflate(layout, null)
    mBottomSheetDialog.setContentView(sheetView)
    clicks(sheetView, mBottomSheetDialog)
    mBottomSheetDialog.dismissWithAnimation = true
    mBottomSheetDialog.show()
}

fun Fragment.changeMainFragment(container: View, frag: Fragment) {
    fragmentManager!!.replaceFragment(container.id, frag)
}

fun Fragment.changeTheme(themeid: Int) {
    (context as AppCompatActivity).setTheme(themeid)
}


fun Fragment.goToActivity(cls: KClass<*>, openAsNew: Boolean = false) {
    val intent = Intent(context, cls.java).apply {
        flags = if (openAsNew) {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        } else {
            Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }
    startActivity(intent)
    (context as AppCompatActivity).overridePendingTransition(
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )
    if (openAsNew) {
        (context as AppCompatActivity).finish()
    }
}

fun Double.priceFormat(): String {
    val nf =
        DecimalFormat("################################################.###########################################");
    return nf.format(this);
}

fun String.priceFormat(): String {
    val nf =
        DecimalFormat("################################################.###########################################");
    return nf.format(this.toDouble());
}

fun Context.goToActivity(cls: KClass<*>, openAsNew: Boolean = false) {
    val intent = Intent(this, cls.java).apply {
        if (openAsNew) {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
    startActivity(intent)
    if (openAsNew) {
        (this as AppCompatActivity).finish()
    }
    (this as AppCompatActivity).overridePendingTransition(
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )
}

fun View.getLifeCycle(): LifecycleOwner {
    var context = context
    while (context !is LifecycleOwner) {
        context = (context as ContextWrapper).baseContext
    }
    return context
}

fun AppCompatActivity.makeToast(message: String?) {
    var msg = message
    if ("null" == msg) msg = "Something went wrong, Please Try Later!"

    AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle(getString(R.string.app_name))
        .setMessage(msg)
        .setPositiveButton("Ok") { d, w ->
            d.dismiss()
        }.show()

}

fun Fragment.makeToast(message: String?) {
    if (context != null)
        context!!.makeToast(message)
}

fun Context.makeToast(message: String?) {
    (this as AppCompatActivity).makeToast(message)
}

var internetAlertDialog: AlertDialog? = null
fun Context.internetErrorDialog(forceExit: Boolean = false) {
    try {
        if (internetAlertDialog == null) {
            internetAlertDialog = createInternetAlertDialog(this, forceExit)
        } else if (internetAlertDialog?.isShowing == true) {
            internetAlertDialog?.dismiss()
        }
        internetAlertDialog?.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun createInternetAlertDialog(context: Context, forceExit: Boolean): AlertDialog {
    return AlertDialog.Builder(context)
        .setCancelable(false)
        .setTitle(context.getString(R.string.app_name))
        .setMessage("Please Check You Internet connection")
        .setPositiveButton(if (forceExit) "Exit" else "Ok") { d, w ->
            if (forceExit) {
                (context as? AppCompatActivity)?.finish()
                (context as? AppCompatActivity)?.finishAffinity()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            d.dismiss()
        }.create()
}

fun AppCompatActivity.createAlertDialog(
    view: View,
    poBtn: String,
    negBtn: String,
    msg: String,
    call: (View) -> Unit
): AlertDialog {
    val dialog = AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle(getString(R.string.app_name))
        .setMessage(msg)
        .setPositiveButton(poBtn) { d, w ->
            call(view)
            d.dismiss()
        }.setNegativeButton(negBtn) { d, w ->
            d.dismiss()
        }.create()
    return dialog
}

fun Fragment.createAlertDialog(
    view: View,
    poBtn: String,
    negBtn: String,
    msg: String,
    call: (View) -> Unit
): AlertDialog {
    val dialog = AlertDialog.Builder(context!!)
        .setCancelable(false)
        .setTitle(getString(R.string.app_name))
        .setMessage(msg)
        .setPositiveButton(poBtn) { d, w ->
            call(view)
            d.dismiss()
        }.setNegativeButton(negBtn) { d, w ->
            d.dismiss()
        }.create()
    return dialog
}

fun AppCompatActivity.toast(message: String?, success: Boolean = true) =
    Snackbar.make(findViewById<View>(android.R.id.content), message!!, Snackbar.LENGTH_SHORT)
        .apply {
            setBackgroundTint(resources.getColor(if (success) R.color.tab_selected_color else R.color.red_color))
            setActionTextColor(resources.getColor(R.color.white_color))
            show()
        }


fun Fragment.toast(message: String?, success: Boolean = true) = context!!.toast(message, success)

fun Context.toast(message: String?, success: Boolean = true) =
    (this as AppCompatActivity).toast(message, success)

fun Editable?.trimString(): String = this.toString().trim()

fun EditText.getStringText(): String = this.text.toString().trim()

fun EditText.clear() = setText("")

fun AppCompatActivity.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

}

fun Fragment.isInternetAvailable(): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val hideSoftInputFromWindow =
            imm?.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
                ?: false
        if (!hideSoftInputFromWindow) imm?.hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}


fun AppCompatActivity.internetNotAvailableException() {
    makeToast("internet Not Available Exception")
}

fun Fragment.internetNotAvaliableException() {
    makeToast("internet Not Available Exception")
}

fun Fragment.hideKeyboard() {
    val activity = context as AppCompatActivity
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun AppCompatActivity.showOkDialog(
    title: String,
    message: String,
    okClickListener: DialogInterface.OnClickListener?
) {
    val aDialog = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)

    if (okClickListener == null) {
        aDialog.setPositiveButton("OK") { dialog, _ -> dialog?.dismiss() }
    } else {
        aDialog.setPositiveButton("OK", okClickListener)
    }

    aDialog.show()
}

fun Fragment.showOkDialog(
    title: String = "",
    message: String?,
    okClickListener: DialogInterface.OnClickListener?
) {
    val aDialog = AlertDialog.Builder(context!!)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)

    if (okClickListener == null) {
        aDialog.setPositiveButton("OK") { dialog, _ -> dialog?.dismiss() }
    } else {
        aDialog.setPositiveButton("OK", okClickListener)
    }
    aDialog.show()
}

fun EditText.makeEmpty() {
    setText("")
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.setEndDrawableClickLisetnier(click: (View) -> Unit) {
    this.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >=
                    this@setEndDrawableClickLisetnier.right - this@setEndDrawableClickLisetnier.compoundDrawables[drawableRight].bounds.width() - 30
                ) {
                    click(this@setEndDrawableClickLisetnier)
                    return true
                }
            }
            return false
        }
    })
}

fun View.setOnSingleClickListener(onSafeClick: (View) -> Unit) {
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < ON_CLICK_DELAY) {
            return@setOnClickListener
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(this)
    }
}

fun View.setOnSingleClickListener(onSafeClick: View.OnClickListener) {
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < ON_CLICK_DELAY) {
            return@setOnClickListener
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick.onClick(this)
    }
}


fun EditText.addTextChangedListener(result: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //    result.onTextChanged(s.trimString())
            result(s.trimString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d("beforeTextChanged", s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("onTextChanged", s.toString())
        }
    })
}

fun View.switchVisibility(goneView: View? = null) {
    if (goneView != null) goneView.visibility = View.GONE
    visibility = View.VISIBLE
}

fun View.onOffVisibility(on: Boolean = true) {
    if (on.not()) visibility = View.GONE else visibility = View.VISIBLE
}

fun View.onOffCardVisibility(on: Boolean = true) {
    if (on.not()) visibility = View.INVISIBLE else visibility = View.VISIBLE
}

/*
fun <M, B : ViewDataBinding> RecyclerView.setMyAdapter(@LayoutRes layoutRes: Int, list: List<M>? = emptyList()): GRecyclerAdapter<M, B> {
    val adapter = GRecyclerAdapter<M, B>(layoutRes)
    return adapter.submitList(list)
}*/

/*fun <M, B : ViewDataBinding> RecyclerView.setMyAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: (B, M, Int) -> Unit
): GRecyclerAdapter<M, B> {
    val adapter = GRecyclerAdapter(
        layoutRes,
        object : GRecyclerHolderListener<M, B> {
            override fun populateItemHolder(binding: B, data: M, position: Int) {
                listner(binding, data, position)
            }
        })

    return adapter.submitList(list)
}*/

//fun Spinner.setSpinnerSelectedListner(listener: SpinnerItemSelectedListener) {
//    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//            listener.onItemSelected(parent, view, position, id)
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>) {
//            Log.d("onNothingSelected", "onNothingSelected")
//        }
//    }
//}

fun View.converToBitMap(): Bitmap {

    //context: Context, view: View

    val displayMetrics = DisplayMetrics()
    (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
    layoutParams =
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
    buildDrawingCache()
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    draw(canvas)

    return bitmap
}

inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.qualifiedName, victim.ordinal)

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.qualifiedName, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }

inline fun <reified T : Enum<T>> Bundle.putExtra(victim: T) =
    putInt(T::class.qualifiedName, victim.ordinal)

inline fun <reified T : Enum<T>> Bundle.getEnumExtra(): T? =
    getInt(T::class.qualifiedName, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }

fun <T> MutableLiveData<T?>.observeNN(@NonNull owner: LifecycleOwner, obs: (t: T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            obs(it)
            this.postValue(null)
        }
    })
}

//fun LatLng?.distanceTo(destination: LatLng): String {
//    val pk = (180f / Math.PI)
//
//    val a1 = (this?.latitude ?: 0.0) / pk
//    val a2 = (this?.longitude ?: 0.0) / pk
//    val b1 = destination.latitude / pk
//    val b2 = destination.longitude / pk
//
//    val t1 = cos(a1) * cos(a2) * cos(b1) * cos(b2)
//    val t2 = cos(a1) * sin(a2) * cos(b1) * sin(b2)
//    val t3 = sin(a1) * sin(b1)
//    val tt = acos(t1 + t2 + t3)
//
//    val meters = 6366000 * tt
//    val miles = ((meters / 1609.34) * 100).toInt().toDouble() / 100.0f
//    return miles.toString()
//}

fun String.substringMax(@IntRange(from = 1) strLength: Int): String {
    if (isEmpty()) return this
    if (length < strLength) return this
    return substring(0, strLength)
}

fun TextView.makeClickable(text: String, start: Int, end: Int, action: (View) -> Unit) {
    val spannableString = SpannableString(text)
    spannableString.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    this.text = spannableString
    this.setOnClickListener {
        action(it)
    }
}

fun String.openInBrowser(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(this)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun String.openInMail(context: Context) {

    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@renteeapp.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Rentee App Support")
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.toCamelCase(): String {
    if (isEmpty()) return this
    val last = subSequence(1, length).toString()
    val first = this.toCharArray()[0].toString().toUpperCase(Locale.getDefault())
    return first + last.toLowerCase(Locale.getDefault())
}

public fun AppCompatActivity.AllinOneDialog(
    ttle: String = "", msg: String = "", btnLeft: String = "No",
    btnRight: String = "Yes", onLeftClick: () -> Unit, onRightClick: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    val viewDialog = this.layoutInflater.inflate(R.layout.custom_dialog, null)
    viewDialog.background = ContextCompat.getDrawable(
        this,
        R.drawable.shadow_border_bg_more_rounded
    )
    builder.setView(viewDialog)
    val alertDialog = builder.create()

    if (ttle.isEmpty()) viewDialog.findViewById<TextView>(R.id.tvHeading).visibility = View.GONE
    if (msg.isEmpty()) viewDialog.findViewById<TextView>(R.id.tvMessage).visibility = View.GONE
    if (btnLeft.isEmpty()) viewDialog.findViewById<TextView>(R.id.btnCancel).visibility = View.GONE
    if (btnRight.isEmpty()) viewDialog.findViewById<TextView>(R.id.btnConfirm).visibility =
        View.GONE

    viewDialog.findViewById<TextView>(R.id.tvHeading).text = ttle
    viewDialog.findViewById<TextView>(R.id.tvMessage).text =
        if (msg.contains("</")) Html.fromHtml(msg) else msg

    viewDialog.findViewById<TextView>(R.id.btnCancel).apply {
        text = btnLeft
        setOnClickListener {
            alertDialog.cancel()
            onLeftClick()
        }
    }
    viewDialog.findViewById<TextView>(R.id.btnConfirm).apply {
        text = btnRight
        setOnClickListener {
            alertDialog.cancel()
            onRightClick()
        }
    }
    alertDialog.setOnDismissListener {
        onLeftClick()
    }
    alertDialog.setCancelable(true)
    alertDialog.show()

    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val displayWidth = displayMetrics.widthPixels
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(alertDialog.window?.getAttributes())
    val dialogWindowWidth = (displayWidth * 0.9f).toInt()
    layoutParams.width = dialogWindowWidth
    alertDialog.window?.attributes = layoutParams
}

public fun Fragment.AllinOneDialog(
    ttle: String = "", msg: String = "", btnLeft: String = "No",
    btnRight: String = "Yes", onLeftClick: () -> Unit, onRightClick: () -> Unit
) {
    (context as AppCompatActivity).AllinOneDialog(
        ttle,
        msg,
        btnLeft,
        btnRight,
        onLeftClick,
        onRightClick
    )
}

public fun AppCompatActivity.AllinOneDialogWithThreeBtn(
    ttle: String = "",
    msg: String = "",
    btnLeft: String = "No",
    btnRight: String = "Yes",
    btnBottom: String = "View",
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onBottomClick: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    val viewDialog = this.layoutInflater.inflate(R.layout.custom_dialog_with_three_btn, null)
    viewDialog.background = ContextCompat.getDrawable(
        this,
        R.drawable.shadow_border_bg_more_rounded
    )
    builder.setView(viewDialog)
    val alertDialog = builder.create()

    if (ttle.isEmpty()) viewDialog.findViewById<TextView>(R.id.tvHeading).visibility = View.GONE
    if (msg.isEmpty()) viewDialog.findViewById<TextView>(R.id.tvMessage).visibility = View.GONE
    if (btnLeft.isEmpty()) viewDialog.findViewById<TextView>(R.id.btnCancel).visibility = View.GONE
    if (btnRight.isEmpty()) viewDialog.findViewById<TextView>(R.id.btnConfirm).visibility =
        View.GONE

    viewDialog.findViewById<TextView>(R.id.tvHeading).text = ttle
    viewDialog.findViewById<TextView>(R.id.tvMessage).text = msg

    viewDialog.findViewById<TextView>(R.id.btnCancel).apply {
        text = btnLeft
        setOnClickListener {
            alertDialog.cancel()
            onLeftClick()
        }
    }
    viewDialog.findViewById<TextView>(R.id.btnConfirm).apply {
        text = btnRight
        setOnClickListener {
            alertDialog.cancel()
            onRightClick()
        }
    }
    viewDialog.findViewById<TextView>(R.id.btnViewCard).apply {
        text = btnBottom
        setOnClickListener {
            alertDialog.cancel()
            onBottomClick()
        }
    }
    alertDialog.setOnDismissListener {
        onLeftClick()
    }
    alertDialog.setCancelable(true)
    alertDialog.show()

    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val displayWidth = displayMetrics.widthPixels
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(alertDialog.window?.getAttributes())
    val dialogWindowWidth = (displayWidth * 0.9f).toInt()
    layoutParams.width = dialogWindowWidth
    alertDialog.window?.attributes = layoutParams
}

public fun Fragment.AllinOneDialogWithThreeBtn(
    ttle: String = "",
    msg: String = "",
    btnLeft: String = "No",
    btnRight: String = "Yes",
    btnBottom: String = "Yes",
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onBottomClick: () -> Unit
) {
    (context as AppCompatActivity).AllinOneDialogWithThreeBtn(
        ttle,
        msg,
        btnLeft,
        btnRight,
        btnBottom,
        onLeftClick,
        onRightClick,
        onBottomClick
    )
}

fun EditText.delayWatcher(delay: Long = 1500, call: (String) -> Runnable) {
    Handler().apply {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.toString().isNotEmpty())
                    postDelayed(call(text.toString()), delay)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                removeCallbacks(call(text.toString()))
        })
    }
}

fun EditText.addWatcher(call: (String) -> Unit) {
    Handler().apply {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                call(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        })
    }
}

fun EditText.onDrawableClick(call: () -> Unit) {
    setOnTouchListener { view, event ->
        if (event.getAction() === MotionEvent.ACTION_UP) {
            val drawableRight: Drawable? = compoundDrawables[2]
            if (drawableRight != null) {
                //The x-axis coordinates of this click event, if > current control width - control right spacing - drawable actual display size
                if (event.getX() >= width - paddingRight - drawableRight.getIntrinsicWidth()) {
                    //Set up to click the EditText icon on the right to lose focus.
                    // Prevent clicking EditText icon on the right side of EditText to get focus and pop-up the soft keyboard
                    call.invoke()
                }
            }
        }
        return@setOnTouchListener true
    }
}

fun EditText.KeyEventSearch(action: (String) -> Unit) {
    setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(
            v: View,
            keyCode: Int,
            event: KeyEvent
        ): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        action(text.toString())
                        return true
                    }
                    else -> {
                    }
                }
            }
            return false
        }
    })
}

fun AppCompatActivity.sendEmail(
    Heading: String = "",
    titl: String = "",
    terms: String = "",
    onSend: (email: String) -> Unit
):AlertDialog {
    val builder = AlertDialog.Builder(this)
    val viewDialog = this.layoutInflater.inflate(R.layout.email_send_dialog, null)
    builder.setView(viewDialog)
    viewDialog.background = ContextCompat.getDrawable(
        this,
        R.drawable.shadow_border_bg_more_rounded
    )
    val alertDialog = builder.create()
    viewDialog.findViewById<TextView>(R.id.tvHeading).text = Heading
    if (titl.isNotEmpty()) viewDialog.findViewById<TextView>(R.id.tvEmailAddress).text = titl
    viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { alertDialog.dismiss() }
    val edtEmail = viewDialog.findViewById<EditText>(R.id.edtEmail)
    val edtPhone = viewDialog.findViewById<EditText>(R.id.edtPhone)
    val cbTerms = viewDialog.findViewById<CheckBox>(R.id.cbTerms)
    if (titl.isNotEmpty()) {
        edtEmail.onOffVisibility(false)
        edtPhone.onOffVisibility(true)
    }
    edtPhone.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
        if (source.length > 0) {
            if (!Character.isDigit(source[0])) return@InputFilter "" else {
                if (dstart == 0) {
                    return@InputFilter "($source"
                } else if (dstart == 3) {
                    return@InputFilter "$source) "
                } else if (dstart == 9) return@InputFilter "-$source"
                else if (dstart >= 14) return@InputFilter ""
            }
        }
        null
    })

    if (terms.isEmpty()) {
        cbTerms.apply {
            spanClickable(cbTerms.text.toString(), 65, 83, {
                cbTerms.isChecked = !cbTerms.isChecked
                startActivity(
                    Intent(
                        this@sendEmail,
                        WebActivity::class.java
                    ).putExtra("name", "Terms of Service")
                        .putExtra("url", "https://www.givebackrx.com/terms_and_condition")
                )
            }, 88, 102) {
                cbTerms.isChecked = !cbTerms.isChecked
                startActivity(
                    Intent(
                        this@sendEmail,
                        WebActivity::class.java
                    ).putExtra("name", "Privacy Policy")
                        .putExtra("url", "https://www.givebackrx.com/privacyPolicy")
                )
            }
        }
    } else {
        cbTerms.text = terms
//        cbTerms.text = Html.fromHtml(getString(R.string.terms_silver,"silver"))
    }

    //16  66-84 89-103
    viewDialog.findViewById<TextView>(R.id.btnSend).setOnClickListener {
        if (edtPhone.visibility == View.GONE) {
            if (edtEmail.text.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                    .matches().not()
            ) {
                toast("Valid email is required!", false)
            } else if (cbTerms.isChecked.not()) {
                toast("Authorize Terms & conditions and Privacy policy!", false)
            } else {
                if (alertDialog.getWindow()?.getCurrentFocus() != null) {
                    alertDialog.getWindow()!!.getCurrentFocus()!!.clearFocus();
                }
                hideKeyboardDialog(alertDialog.currentFocus)
//                alertDialog.dismiss()
                onSend(edtEmail.text.toString())
            }
        } else {
            if (edtPhone.text.isEmpty() && edtPhone.text.length < 14) {
                toast("Valid phone is required!", false)
            } else if (cbTerms.isChecked.not()) {
                toast("Authorize Terms & conditions and Privacy policy!", false)
            } else {
                if (alertDialog.window?.currentFocus != null) {
                    alertDialog.window!!.currentFocus!!.clearFocus();
                }
                hideKeyboardDialog(alertDialog.currentFocus)
//                alertDialog.dismiss()
                onSend(edtPhone.text.toString())
            }
        }
    }
    alertDialog.setOnDismissListener {
        hideKeyboardDialog(alertDialog.currentFocus)
    }
    alertDialog.setCancelable(true)
    alertDialog.show()

    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val displayWidth = displayMetrics.widthPixels
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(alertDialog.window?.getAttributes())
    val dialogWindowWidth = (displayWidth * 0.9f).toInt()
    layoutParams.width = dialogWindowWidth
    alertDialog.window?.attributes = layoutParams
    return alertDialog
}


fun Fragment.sendEmail(
    Heading: String = "",
    titl: String = "",
    terms: String = "",
    onSend: (email: String) -> Unit
) :AlertDialog{
   return (context as AppCompatActivity).sendEmail(Heading, titl, terms, onSend)
}

fun AppCompatActivity.cardPopup(
    isGold: Boolean,
    isWhite: Boolean,
    mPcn: String,
    mCardCharity: String,
    mMemberId: String,
    mMemberName: String,
    mCardCharityName: String,
    mBin: String,
    mGroup: String,
    isFront: Boolean
) {
    val builder = AlertDialog.Builder(this)
    val viewDialog = this.layoutInflater.inflate(
        if (isWhite) R.layout.white_card_popup_layout else R.layout.card_popup_layout,
        null
    )
    builder.setView(viewDialog)
    val alertDialog = builder.create()

    if (isWhite.not() && isGold) {
        viewDialog.findViewById<ConstraintLayout>(R.id.clFrontView).background =
            ContextCompat.getDrawable(this, R.drawable.gold_card_gradient_bg)
        viewDialog.findViewById<ConstraintLayout>(R.id.clBackView).background =
            ContextCompat.getDrawable(this, R.drawable.gold_card_gradient_bg)
    }

    viewDialog.findViewById<TextView>(R.id.mTvPcn).text = mPcn
    viewDialog.findViewById<ImageView>(R.id.mIvCardCharityImageFront).loadOrigImage(mCardCharity)
    viewDialog.findViewById<TextView>(R.id.mTvMemberId).text = mMemberId
    viewDialog.findViewById<TextView>(R.id.mTvMemberName).apply {
        text = mMemberName
        onOffVisibility(mMemberName.replace("null", "").isNotEmpty())
        viewDialog.findViewById<TextView>(R.id.textView318)
            .onOffVisibility(mMemberName.replace("null", "").isNotEmpty())
    }
    viewDialog.findViewById<TextView>(R.id.mTvCardCharityName).text = mCardCharityName
    viewDialog.findViewById<TextView>(R.id.mTvBin).text = mBin
    viewDialog.findViewById<TextView>(R.id.mTvGroup).text = mGroup
    viewDialog.findViewById<ConstraintLayout>(R.id.clFrontView).onOffCardVisibility(isFront)
    viewDialog.findViewById<ConstraintLayout>(R.id.clBackView).onOffCardVisibility(isFront.not())
    viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { alertDialog.dismiss() }

    alertDialog.setCancelable(true)
    alertDialog.show()

    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val displayWidth = displayMetrics.widthPixels
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(alertDialog.window?.getAttributes())
    val dialogWindowWidth = (displayWidth * 0.98f).toInt()
    layoutParams.width = dialogWindowWidth
    alertDialog.window?.attributes = layoutParams

}

fun Fragment.cardPopup(
    isGold: Boolean,
    isWhite: Boolean,
    mPcn: String,
    mCardCharity: String,
    mMemberId: String,
    mMemberName: String,
    mCardCharityName: String,
    mBin: String,
    mGroup: String,
    isFront: Boolean
) {
    (context as AppCompatActivity).cardPopup(
        isGold,
        isWhite,
        mPcn,
        mCardCharity,
        mMemberId,
        mMemberName,
        mCardCharityName,
        mBin,
        mGroup,
        isFront
    )
}


fun Fragment.upgradeCard(onUpgrade: () -> Unit) {
    val mBuilder = Dialog(context as AppCompatActivity)
    mBuilder.setContentView(R.layout.upgrade_gold_card_dialog);
    mBuilder.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation;
    mBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
    mBuilder.window!!.setGravity(Gravity.CENTER)
    mBuilder.window!!.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    mBuilder.findViewById<Button>(R.id.btnUpgrade).setOnClickListener {
        mBuilder.dismiss()
        onUpgrade()
    }
    mBuilder.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
        mBuilder.dismiss()
    }
    mBuilder.show();
}


public fun AppCompatActivity.hideKeyboardDialog(view: View? = null) {
    try {
        val imm =
            (this).getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null)
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    } catch (e: Exception) {
        e.printStackTrace();
    }
}

public fun Fragment.hideKeyboardDialog(view: View? = null) {
    try {
        val imm =
            ((context as AppCompatActivity)).getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null)
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    } catch (e: Exception) {
        e.printStackTrace();
    }
}


fun Window.statusBarTheme(color: String) {
    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        ) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            statusBarColor = Color.parseColor(color ?: "#0001FE")
            val darkness =
                1 - (0.299 * Color.red(
                    Color.parseColor(
                        color ?: "#0001FE"
                    )
                ) + 0.587 * Color.green(
                    Color.parseColor(color ?: "#0001FE")
                ) + 0.114 * Color.blue(
                    Color.parseColor(
                        color ?: "#0001FE"
                    )
                )) / 255;
            if (darkness < 0.5) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                };
            }
        }
    } catch (e: Exception) {
    }
}

