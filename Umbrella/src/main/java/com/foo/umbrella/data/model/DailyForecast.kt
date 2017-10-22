package com.foo.umbrella.data.model

data class DailyForecast(
        val dayOfWeek: String,
        val hourlyForecast: List<ForecastCondition>
)