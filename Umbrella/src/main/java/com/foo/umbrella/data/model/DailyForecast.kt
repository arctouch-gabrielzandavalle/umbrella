package com.foo.umbrella.data.model

data class DailyForecast(
        var dayOfWeek: String,
        var hourlyForecast: List<ForecastCondition>
)