package com.app.givebackrx.data.network

import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.NetworkConstants
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


interface RestService {
//    @POST(NetworkConstants.auth_detail)
//    fun auth_detail(@Body backend: JsonObject): Observable<AuthDetailEntity>
//
//    @POST(NetworkConstants.oauth2_token)
//    fun generate_token(
//        @Query("grant_type") grant_type: String,
//        @Query("client_id") client_id: String,
//        @Query("client_secret") client_secret: String,
//        @Query("username") username: String,
//        @Query("password") password: String
//    ): Observable<GenerateTokenEntity>
//
//    @POST(NetworkConstants.employee_signup)
//    fun employee_signup(@Body backend: JsonObject): Observable<RegisterEntity>
//
//    @POST(NetworkConstants.employee_login)
//    fun employee_login(@Body backend: JsonObject): Observable<LoginEntity>
//
//    @POST(NetworkConstants.employee_verification)
//    fun employee_verification(@Body backend: JsonObject): Observable<UserProfileEntity>
//
//    @POST(NetworkConstants.client_benefits)
//    fun all_benefits(@Body backend: JsonObject): Observable<AllBenefitsEntity>
//
//
//    @POST(NetworkConstants.employee_details)
//    fun employee_details(@Body backend: JsonObject): Observable<EmployeeDetailEntity>
//
//
//    @POST(NetworkConstants.employee_edit)
//    fun employee_edit(@Body backend: JsonObject): Observable<EditProfileEntity>
//
//    @POST(NetworkConstants.employee_choices)
//    fun employee_choices(@Body backend: JsonObject): Observable<ChoiceEntity>
//
//    //logout
//    @POST(NetworkConstants.revoke)
//    fun revoke(@Query("token") token: String): Observable<String>
//
//    @POST(NetworkConstants.buying_option)
//    fun buying_option(@Body backend: JsonObject): Observable<BuyingOptionEntity>
//
//    @POST(NetworkConstants.monthly_allowance)
//    fun monthly_allowance(@Body backend: JsonObject): Observable<MonthlyAllowanceEntity>
//
//    @POST(NetworkConstants.eye_care)
//    fun eye_care(@Body backend: JsonObject): Observable<EyeCareEntity>
//
//    @POST(NetworkConstants.life_assurance_flex)
//    fun life_assurance_flex(@Body backend: JsonObject): Observable<LifeAssuranceFlexEnity>
//
//    @POST(NetworkConstants.holiday_trading)
//    fun holiday_trading(@Body backend: JsonObject): Observable<HolidayTradingEntity>
//
//    @POST(NetworkConstants.private_medical)
//    fun private_medical(@Body backend: JsonObject): Observable<PrivateMedicalEntity>
//
//    @POST(NetworkConstants.private_medical_selectoption)
//    fun private_medical_selectoption(@Body backend: JsonObject): Observable<PrivateMedicalOptionsEntity>
//
//    @POST(NetworkConstants.private_medical_updateplans)
//    fun private_medical_updateplans(@Body backend: JsonObject): Observable<PrivateMedicalOptionsEntity>
//
//    @POST(NetworkConstants.pension_detail)
//    fun pension_detail(@Body backend: JsonObject): Observable<GroupPensionEntity>
//
//    @POST(NetworkConstants.pension_detail_optIn)
//    fun pension_detail_optIn(@Body backend: JsonObject): Observable<PensionOptinEntity>
//
//    @POST(NetworkConstants.workplace_isa)
//    fun workplace_isa(@Body backend: JsonObject): Observable<WorkplaceISAEntity>
//
//    @POST(NetworkConstants.income_protection)
//    fun income_protection(@Body backend: JsonObject): Observable<IncomeProtectionEntity>
//
//    @POST(NetworkConstants.life_assurance_beneficiaries)
//    fun life_assurance_beneficiaries(@Body backend: JsonObject): Observable<AddBeneficiaryEntity>
//
//    @POST(NetworkConstants.group_cash_plan)
//    fun group_cash_plan(@Body backend: JsonObject): Observable<CashPlanEntity>
//
//    @POST(NetworkConstants.group_cashPlan_selectoption)
//    fun group_cashPlan_selectoption(@Body backend: JsonObject): Observable<CashPlanOptionsEntity>
//
//    @POST(NetworkConstants.group_cashplan_update)
//    fun group_cashplan_update(@Body backend: JsonObject): Observable<CashPlanOptionsEntity>
//
//    @POST(NetworkConstants.life_insurance)
//    fun life_insurance(@Body backend: JsonObject): Observable<LifeInsuranceEntity>
//
//    @POST(NetworkConstants.work_loan)
//    fun work_loan(@Body backend: JsonObject): Observable<WorkLoanEntity>
//
//    @POST(NetworkConstants.group_dental)
//    fun group_dental(@Body backend: JsonObject): Observable<GroupDentalEntity>
//
//
//    @POST(NetworkConstants.eye_care_issuevoucher)
//    fun eye_care_issuevoucher(@Body backend: JsonObject): Observable<EyeCareEntity>
//
//    @POST(NetworkConstants.adviser_meeting)
//    fun adviser_meeting(@Body backend: JsonObject): Observable<AdviserMeetingsEntity>

    @GET(NetworkConstants.drug)
    fun searchDrug(@Query("drug_name") drug_name: String): Observable<SearchDrugEntity>

    ////coupon?pricebook_entry_id=01uW000000QwZuvIAF&pharmacy_address_id=a07W0000004v7WPIAY&membership_type=Silver
    @GET(NetworkConstants.coupon)
    fun couponGeneration(@QueryMap map: HashMap<String, String>): Observable<CouponGenerationEntity>

    @GET(NetworkConstants.pharmacyList)
    fun pharmacyList(
        @QueryMap map: HashMap<String, String>/*@Query("drug_generic_name") drug_generic_name: String,@Query(" drug_name") drug_name: String,@Query("is_location") is_location: String
                     ,@Query("form") form: String,@Query("dosage") dosage: String,@Query("quantity") quantity: String,@Query("latitude") latitude: String,
                     @Query(" longitude") longitude: String,@Query("membership_type") membership_type: String*/
    ): Observable<PharmacyListEntity>

    @POST(NetworkConstants.preSignUpVerification)
    fun preSignUpVerification(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.couponViaEmail)
    fun couponViaEmail(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.couponViaPhone)
    fun couponViaPhone(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.textCard)
    fun textCard(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.favoriteMedication)
    fun addFavoriteMedication(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @Multipart
    @POST(NetworkConstants.charity)
    fun charitySignee(@PartMap backend: HashMap<String, RequestBody>): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.emailCard)
    fun emailCard(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @GET(NetworkConstants.postSignUpVerification)
    fun postSignUpVerification(
        @Query("phone") phone: String,
        @Query("email") email: String,
        @Query("referral_code") referral_code: String
    ): Observable<SuccessMessageEntity>

    @GET(NetworkConstants.forgotPassword)
    fun forgotPassword(
        @Query("type") type: String = "email",
        @Query("value") value: String
    ): Observable<SignInWithUserDetailEntity>

    @GET(NetworkConstants.address)
    fun getAllAddress(
            @Query("user_type") user_type: String,
            @Query("address_type") address_type: String)
    : Observable<AddressesListEntity>

    @GET(NetworkConstants.address)
    fun getAllAddressTwo(
        @Query("user_type") user_type: String,
        @Query("address_type") address_type: String,
        @Query("address_id") address_id: String)
            : Observable<AddressesListEntity>


    @GET(NetworkConstants.paymentCards)
    fun getCards()
    : Observable<PaymentResEntity>

    @DELETE(NetworkConstants.address)
    fun deleteAddress(
        @Query("address_id") address_id: String,
        @Query("user_type") user_type: String
    ): Observable<SuccessMessageEntity>

    @DELETE(NetworkConstants.paymentCards)
    fun deletePaymentCard(
        @Query("card_id") card_id: String
    ): Observable<SuccessMessageEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.selectPaymentCard)
    fun selectCard(
            @Field("catalog_order_id") catalog_order_id: String,
            @Field("card_id") card_id: String
    ): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.paymentCards)
    fun addNewCard(@Body backend: JsonObject): Observable<SuccessMessageEntity>


    @FormUrlEncoded
    @POST(NetworkConstants.payment)
    fun paymentCart(@FieldMap backend: HashMap<String,Any>): Observable<SuccessMessageEntity>

    @GET(NetworkConstants.charityCardList)
    fun getCharityCardList(@Query("membership_type") membership_type: String): Observable<PrintedCharityEntity>

    @POST(NetworkConstants.address)
    fun addNewAddress(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @PUT(NetworkConstants.address)
    fun updateNewAddress(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.defaultAddress)
    fun defaultAddress(
        @Field("address_id") address_id: String,
        @Field("addres_type") addres_type: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>

    @POST(NetworkConstants.signIn)
    fun signIn(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>

    @POST(NetworkConstants.signUp)
    fun signUp(@Body backend: HashMap<String, Any>): Observable<SignInWithUserDetailEntity>

    @POST(NetworkConstants.verifyOTP)
    fun verifyOTP(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>

    @POST(NetworkConstants.sendAgain)
    fun sendAgain(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>


    @GET(NetworkConstants.charity)
    fun charityList(
        @Query("sort_by_alphabet") sort_by_alphabet: String,
        @Query("page_number") page_number: String,
        @Query("membership_type") membership_type: String,
        @Query("sort_by_category") sort_by_category: String,
        @Query("state") state: String,
        @Query("city") city: String,
        @Query("charity_name") charity_name: String
    ): Observable<CharityListEntity>

    @GET(NetworkConstants.charityDetail)
    fun charityDetail(
        @Query("charity_id") charity_id: String,
        @Query("membership_type") membership_type: String
    ): Observable<CharityDetailEntity>

    @GET(NetworkConstants.charityDetail)
    fun charityDetail(
        @Query("charity_id") charity_id: String
    ): Observable<CharityDetailEntity>

    @GET(NetworkConstants.registeredCharityDetail)
    fun registeredCharityDetail(): Observable<MyCharityDetailEntity>

    @GET(NetworkConstants.dashboard)
    fun dashboard(@Query("user_type") user_type: String): Observable<DashboardEntity>

    @POST(NetworkConstants.invitebyReferral)
    fun invitebyReferral(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>

    @POST(NetworkConstants.faq)
    fun helpTicket(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>

//    @FormUrlEncoded
    @POST(NetworkConstants.goldCardPayment)
    fun goldCardPayment(
        @Body backend: JsonObject
//        @Field ("address_id")address_id: String="a0dW0000001nVDkIAM"
    ): Observable<PaymentEntity>

    @GET(NetworkConstants.goldSummary)
    fun getGoldSummary(
            @Query("coupon") coupon: String,
            @Query("coupon_type") coupon_type: String
    ): Observable<OrderSummaryModel>




    @GET(NetworkConstants.resend)
    fun resend(
        @Query("resend_button_id") resend_button_id: String,
        @Query("user_type") user_type: String
    ): Observable<DashboardEntity>

    @GET(NetworkConstants.favoriteMedication)
    fun getFavoriteMedication(
        @Query("membership_type") membership_type: String,
        @Query("user_type") user_type: String
    ): Observable<FavoriteMedicationEntity>

    @DELETE(NetworkConstants.favoriteMedication)
    fun deleteFavoriteMedication(
        @Query("favorite_medication_id") favorite_medication_id: String,
        @Query("user_type") user_type: String
    ): Observable<FavoriteMedicationEntity>

    @DELETE(NetworkConstants.paymentCard)
    fun deleteSavedCard(
        @Query("card_id") card_id: String,
        @Query("user_type") user_type: String
    ): Observable<FavoriteMedicationEntity>

    @GET(NetworkConstants.referral)
    fun referral(
        @Query("referral_status") referral_status: String,
        @Query("user_type") user_type: String
    ): Observable<ReferralEntity>

    @GET(NetworkConstants.notices)
    fun notices(
        @Query("notices_status") notices_status: String,
        @Query("user_type") user_type: String
    ): Observable<NoticeEntity>

    @GET(NetworkConstants.myCharity)
    fun myCharity(): Observable<MyCharityEntity>

    @GET(NetworkConstants.faq)//faq?customer=false&pharmacy=false&charity=false&partner_in_care=false
    fun getFaq(
        @Query("customer") customer: String
        , @Query("pharmacy") pharmacy: String
        , @Query("charity") charity: String
        , @Query("partner_in_care") partner_in_care: String
        , @Query("membership_type") membership_type: String
    ): Observable<HelpEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.notices)
    fun acknowledgeNotices(
        @Field("notice_id") notice_id: String,
        @Field("user_type") user_type: String
    ): Observable<NoticeEntity>

    //    @FormUrlEncoded
    @POST(NetworkConstants.feedback)
    fun feedback(
        @Body backend: JsonObject
//        @Field("rating") rating: String,
//        @Field("description") description: String,
//        @Field("membership_type") membership_type: String
    ): Observable<SignInWithUserDetailEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.task)
    fun markCompleteTask(
        @Field("task_id") task_id: String,
        @Field("user_type") user_type: String
    ): Observable<TaskEntity>

    @GET(NetworkConstants.task)
    fun task(
        @Query("task_status") task_status: String,
        @Query("user_type") user_type: String
    ): Observable<TaskEntity>

    @GET(NetworkConstants.recentPurchases)
    fun recentPurchases(
        @Query("time") time: String,
        @Query("pharmacy") pharmacy: String,
        @Query("medication") medication: String,
        @Query("user_type") user_type: String
    ): Observable<RecentPurchaseEntity>


    @GET(NetworkConstants.profileDetail)
    fun profileDetail(
        @Query("user_type") user_type: String
    ): Observable<ProfileEntity>

    @Multipart
    @POST(NetworkConstants.editProfileImage)
    fun editProfileImage(
        @Part("user_type") user_id: RequestBody,
        @Part("image\"; filename=\"image.jpg") image: RequestBody
    ): Observable<ProfileUpdatedEntity>


    @FormUrlEncoded
    @PUT(NetworkConstants.profileDetail)
    fun updateSecondary(
        @Field("type") type: String,
        @Field("value") value: String,
        @Field("user_type") user_type: String
    ): Observable<VerifySecondaryOtpEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.editProfile)
    fun editProfile(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("apt_suite_number") apt_suite_number: String,
        @Field("postal_code") postal_code: String,
        @Field("user_type") user_type: String
    ): Observable<VerifySecondaryOtpEntity>


    @POST(NetworkConstants.profileDetailVerifyOTP)
    fun verifySecondaryOTP(@Body backend: JsonObject): Observable<SignInWithUserDetailEntity>

    @GET(NetworkConstants.pharmacyNearMe)
    fun pharmacyNearMe(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("distance") distance: String,
        @Query("zipcode") zipcode: String
    ): Observable<NearMeEntity>

    @GET(NetworkConstants.appVersion)
    fun appVersion(
        @Query("membership_type") membership_type: String
    ): Observable<AppVersionEntity>


    @GET(NetworkConstants.appVersion)
    fun appVersion(): Observable<AppVersionEntity>

    @PUT(NetworkConstants.selectCharity)
    fun selectCharity(
        @Query("charity_id") charity_id: String
    ): Observable<SuccessMessageEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.profilePassword)
    fun profilePassword(
        @Field("new_password") new_password: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>


    /*@FormUrlEncoded
    @PUT(NetworkConstants.profilePassword)
    fun profilePassword(
        @Field("password") password: String,
        @Field("new_password") new_password: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>*/

    @FormUrlEncoded
    @PUT(NetworkConstants.profileEnableAuth)
    fun profileEnableAuth(
        @Field("two_factor_auth") two_factor_auth: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>

    @GET(NetworkConstants.profileSecurity)
    fun profileSecurity(
        @Query("user_type") user_type: String
    ): Observable<SecurityDetailEntity>

    @GET(NetworkConstants.store)
    fun storeListing(
        @QueryMap map: HashMap<String, String>
    ): Observable<StoreListEntity>

    @GET(NetworkConstants.storeProductDetail)
    fun storeProductDetail(
        @QueryMap map: HashMap<String, String>
    ): Observable<StoreDetailEntity>

    @GET(NetworkConstants.notificationPreference)
    fun notificationPreferenceGet(
        @Query("user_type") user_type: String
    ): Observable<NotificationEntity>

    @POST(NetworkConstants.myCart)
    fun addtoCart(
        @Body backend: JsonObject
    ): Observable<MyCartEntity>

    @POST(NetworkConstants.getCart)
    fun getCart(
        @Body backend: JsonObject
    ): Observable<CartEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.notificationPreference)
    fun notificationPreferenceSet(
        @Field("promotions") promotions: String,
        @Field("coupon_experience_satisfaction") coupon_experience_satisfaction: String,
        @Field("givebackrx_news") givebackrx_news: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.updateCard)
    fun updateCards(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("card_number") card_number: String,
        @Field("expire_month") expire_month: String,
        @Field("expire_year") expire_year: String,
        @Field("cvv") cvv: String,
        @Field("card_type") card_type: String
    ): Observable<SuccessMessageEntity>

    @FormUrlEncoded
    @PUT(NetworkConstants.disableRecurringPayment)
    fun disableRecurringPayment(
        @Field("feedback") feedback: String,
        @Field("user_type") user_type: String
    ): Observable<SuccessMessageEntity>


    @GET(NetworkConstants.goldSummary)
    fun getStoreSummary(
        @Query("user_type") coupon: String
    ): Observable<OrderSummaryModel>


    @GET(NetworkConstants.goldSummary)
    fun getGoldMembershipSummary(
            @Query("coupon") coupon: String,
            @Query("coupon_type") coupon_type: String
    ): Observable<MemberShipOrderEntity>

    @GET(NetworkConstants.storeSummary)
    fun getOrderSummary(
            @Query("user_type") user_type: String
    ): Observable<StoreOrderSummary>


    @GET(NetworkConstants.storeSummary)
    fun getOrderSummaryTwo(
            @Query("user_type") user_type: String,
            @Query("service_type_id") service_type_id: String
    ): Observable<StoreOrderSummary>



    @POST(NetworkConstants.storePayment)
    fun storePayment(@Body backend: JsonObject): Observable<SuccessMessageEntity>

    @GET(NetworkConstants.storePaymentResponse)
    fun storePaymentResponse(
            @Query("catalog_order_id") coupon: String
    ): Observable<HoldResponseEntity>
}
