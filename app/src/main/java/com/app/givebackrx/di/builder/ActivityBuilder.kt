package com.app.givebackrx.di.builder

import com.app.givebackrx.appcode.charityDetail.CharityDetailActivity
import com.app.givebackrx.appcode.charityDetail.CharityDetailModule
import com.app.givebackrx.appcode.charitysignee.CharitySigneeActivity
import com.app.givebackrx.appcode.charitysignee.CharitySigneeModule
import com.app.givebackrx.appcode.drugDetail.DrugDetailProvider
import com.app.givebackrx.appcode.editmycharity.EditCharityProvider
import com.app.givebackrx.appcode.forgot.ForgotPassActivity
import com.app.givebackrx.appcode.forgot.ForgotPassModule
import com.app.givebackrx.appcode.howitworksModule.HowItWorksActivity
import com.app.givebackrx.appcode.loginEmailModule.LoginEmailActivity
import com.app.givebackrx.appcode.loginEmailModule.LoginEmailModule
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordModule
import com.app.givebackrx.appcode.loginPhoneNumberModule.LoginPhoneActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.charity.CharityProvider
import com.app.givebackrx.appcode.main.charitydetail.CharityDetailProvider
import com.app.givebackrx.appcode.main.home.HomeProvider
import com.app.givebackrx.appcode.main.pharmacy.PharmacyProvider
import com.app.givebackrx.appcode.main.settings.SettingsProvider
import com.app.givebackrx.appcode.mycart.MyCartProvider
import com.app.givebackrx.appcode.mycharitydetail.MyCharityDetailProvider
import com.app.givebackrx.appcode.orderSummary.addPaymentModule.AddNewPaymentProvider
import com.app.givebackrx.appcode.orderSummary.billingModule.BillingProvider
import com.app.givebackrx.appcode.orderSummary.reviewModule.ReviewProvider
import com.app.givebackrx.appcode.orderSummary.shippingModule.ShipmentProvider
import com.app.givebackrx.appcode.orderSummary.thankYouModule.ThankProvider
import com.app.givebackrx.appcode.payment.PaymentProvider
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.preSignUpModule.PreSignupCheckModule
import com.app.givebackrx.appcode.presignuphone.PreSignupPhoneActivity
import com.app.givebackrx.appcode.presignuphone.PreSignupPhoneModule
import com.app.givebackrx.appcode.printedcard.PrintedCharityProvider
import com.app.givebackrx.appcode.profileModule.ProfileProvider
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityActivity
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityModule
import com.app.givebackrx.appcode.selectaddress.SelectAddressProvider
import com.app.givebackrx.appcode.selectaddress.addnewaddress.AddNewAddressProvider
import com.app.givebackrx.appcode.selectaddress.paymentsModule.SelectPaymentProvider
import com.app.givebackrx.appcode.settings.FeedbackModule.FeedbackProvider
import com.app.givebackrx.appcode.settings.changePasswordModule.ChangePasswordProvider
import com.app.givebackrx.appcode.settings.dashboard.DashboardProvider
import com.app.givebackrx.appcode.settings.favouriteModule.FavouriteProvider
import com.app.givebackrx.appcode.settings.help.HelpProvider
import com.app.givebackrx.appcode.settings.myNoticesModule.MyNoticesProvider
import com.app.givebackrx.appcode.settings.myTasksModule.MyTaskProvider
import com.app.givebackrx.appcode.settings.mycharity.MyCharityProvider
import com.app.givebackrx.appcode.settings.nearme.NearMeProvider
import com.app.givebackrx.appcode.settings.purchase.RecentPurchaseProvider
import com.app.givebackrx.appcode.settings.referralsModule.ReferralProvider
import com.app.givebackrx.appcode.settings.updateCardModule.UpdateCardProvider
import com.app.givebackrx.appcode.signupModule.SignUpActivity
import com.app.givebackrx.appcode.signupModule.SignupModule
import com.app.givebackrx.appcode.store.StoreProvider
import com.app.givebackrx.appcode.storecheckout.StoreCheckoutProvider
import com.app.givebackrx.appcode.storedetail.StoreDetailProvider
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPActivity
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract fun bindHowItWorksActivity(): HowItWorksActivity

    @ContributesAndroidInjector(modules = [(CharitySigneeModule::class)])
    abstract fun bindCharitySigneeActivity(): CharitySigneeActivity

    @ContributesAndroidInjector(modules = [(ForgotPassModule::class)])
    abstract fun bindForgotPassActivity(): ForgotPassActivity

    @ContributesAndroidInjector(modules = [(LoginEmailPasswordModule::class)])
    abstract fun bindLoginEmailPasswordActivity(): LoginEmailPasswordActivity

    @ContributesAndroidInjector(modules = [(LoginEmailModule::class)])
    abstract fun bindLoginActivity(): LoginEmailActivity

    @ContributesAndroidInjector(modules = [(LoginEmailModule::class)])
    abstract fun bindLoginPhoneActivity(): LoginPhoneActivity

    @ContributesAndroidInjector(modules = [(PreSignupCheckModule::class)])
    abstract fun bindPreSignUpCheckActivity(): PreSignUpCheckActivity

    @ContributesAndroidInjector(modules = [(PreSignupPhoneModule::class)])
    abstract fun bindPreSignupPhoneActivity(): PreSignupPhoneActivity

    @ContributesAndroidInjector(modules = [(SelectCharityModule::class)])
    abstract fun bindSelectCharityActivity(): SelectCharityActivity

    @ContributesAndroidInjector(modules = [(SignupModule::class)])
    abstract fun bindSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [(VerifyOTPModule::class)])
    abstract fun bindVerifyOTPActivity(): VerifyOTPActivity

    @ContributesAndroidInjector(modules = [(CharityDetailModule::class)])
    abstract fun bindCharityDetailActivity(): CharityDetailActivity

    @ContributesAndroidInjector(modules = [(HomeProvider::class), (PharmacyProvider::class), (DrugDetailProvider::class),
        (CharityProvider::class), (CharityDetailProvider::class),
        (SettingsProvider::class),(DashboardProvider::class),(MyNoticesProvider::class),
        (MyTaskProvider::class),(ReferralProvider::class),(UpdateCardProvider::class),
        (MyCharityProvider::class),(HelpProvider::class),(NearMeProvider::class),
        (ProfileProvider::class),(ChangePasswordProvider::class),(MyCharityDetailProvider::class),
        (EditCharityProvider::class),(StoreProvider::class),(PaymentProvider::class),
        (FavouriteProvider::class),(RecentPurchaseProvider::class),(FeedbackProvider::class)
        ,(StoreDetailProvider::class),(MyCartProvider::class),(StoreCheckoutProvider::class),(ThankProvider::class)
        ,(ShipmentProvider::class),(BillingProvider::class),(ReviewProvider::class),(SelectPaymentProvider::class),(AddNewPaymentProvider::class)
        ,(SelectAddressProvider::class),(AddNewAddressProvider::class),(PrintedCharityProvider::class)])
    abstract fun bindMainActivity(): MainActivity

}