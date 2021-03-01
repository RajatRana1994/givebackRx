package com.app.givebackrx.data.entity

data class DashboardEntity(
    val auth:Auth,
    val `data`: DashboardData,
    val message: String,
    val success: Boolean
)
/*{"success":true,"message":"Dashboard","data":{"member_type":"Silver","payment_type":"","subscription_end_date":"","subscription_days_left":"","number_of_donars":0,
"task_to_complete":0,"referral_made":2,"notice_reviewed_count":0,"charity_logo":"https://res.cloudinary.com/hhpia9n3q/image/upload/v1598237017/Profile/Z1B_YhFmm.png",
"card_detail":{"member_id":"NP00001004","charity_name":"Yashika Charity Foundation","bin":"004527","pcn":"NP00001004","group":"40409"}},
"auth":{"new_jwt_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTG9naW5JZCI6IloxQl9ZaEZtbSIsImVtYWlsIjoieWFzaGlrYS5jeW50ZXhhQGdtYWlsLmNvbSIsInBob25lIjoiMTIxMTExMjMzIiwibWVtYmVyc2hpcF90eXBlIjoiU2lsdmVyIiwiaWF0IjoxNjAwMjY4MDkxfQ.-HdSJ4RishV1__BRG9F2-IrCLdnXlGp2ppShX3ojaAY"}}*/
data class DashboardData(
    val active_task_count: Int?,
    val card_detail: CardDetail,
    val charity: DashboardCharity?,
    val charity_logo: String,
    val saving_amount: String,
    val notice_reviewed_count: Int?,
    val referral_made_count: Int?
)

data class DashboardCharity(
    val charity_id: String?,
    val charity_logo: String?,
    val charity_name: String?,
    val description: String?
)