package com.healthtech.misalud.core.network.data.responses

import com.healthtech.misalud.core.models.ExerciseRecord
import com.healthtech.misalud.core.models.HomeScreenData
import com.healthtech.misalud.core.models.MealRecord
import com.healthtech.misalud.core.models.RequestError
import com.healthtech.misalud.core.models.UserModel

class PeopleResponses {
    data class GetUserData (
        val success: Boolean,
        val user: UserModel
    )

    data class PostMealRecord (
        val error: RequestError?,
        val success: Boolean?
    )

    data class GetMealRecords (
        val error: RequestError?,
        val success: Boolean,
        val records: List<MealRecord>
    )

    data class GetExerciseRecords (
        val error: RequestError?,
        val success: Boolean,
        val records: List<ExerciseRecord>
    )

    data class PostExerciseRecord (
        val error: RequestError?,
        val success: Boolean?
    )

    data class GetRecordDays (
        val error: RequestError?,
        val success: Boolean,
        val dates: List<String>
    )

    data class GetHomeScreenData (
        val error: RequestError?,
        val success: Boolean,
        val data: HomeScreenData
    )
}