package com.app.givebackrx

import android.app.Activity
import android.app.Service
import android.content.Context
import android.os.StrictMode
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.app.givebackrx.appcode.profileModule.ProfileFragment
import com.app.givebackrx.data.entity.SavePharmacyFavData
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject
import com.app.givebackrx.di.component.DaggerAppComponent
import com.google.android.libraries.places.api.Places
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*


class GBRxApp : MultiDexApplication(), /*HasServiceInjector,*/ HasActivityInjector {

    companion object{
        var ON_CLICK_DELAY=1*1000
        var lastTimeClicked=0L
        var primary_Color: String="#BE2233"
        var mSavePharmacyFavData: SavePharmacyFavData?=null
        var secondary_Color: String="#BE2233"
        var ternary_color: String="#EC505E"
        val pound = Currency.getInstance("GBP").symbol
    }

//    @Inject
//    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityDispatchingAndroidInjector

//    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    operator fun get(context: Context): GBRxApp {
        return context.applicationContext as GBRxApp
    }


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        FirebaseAnalytics.getInstance(this)
//        FirebaseCrashlytics.getInstance()
        Places.initialize(applicationContext, getString(R.string.google_mapkey))

        // Create a new PlacesClient instance
//        val placesClient = Places.createClient(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    data class StateModel(var state:String,var code:String)


    fun addStates(activity: Activity): MutableList<StateModel> {
        val listingJson=loadJSONFromAsset(activity)
        val list= mutableListOf<StateModel>()
        val json= JSONArray(listingJson)
        for (i in 0 until json.length()) {
            val jsonObj= JSONObject(json.get(i).toString())
            list.add(
                StateModel(
                    jsonObj.getString("name"),
                    jsonObj.getString("abbreviation")
                )
            )
        }
        return list
    }

    fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = context.assets.open("statesjson")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}