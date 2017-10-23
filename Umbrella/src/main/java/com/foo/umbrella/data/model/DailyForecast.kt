package com.foo.umbrella.data.model

data class DailyForecast(
        val day: ForecastCondition,
        val hourlyForecast: List<ForecastCondition>,
        val minTemperature: ForecastCondition?,
        val maxTemperature: ForecastCondition?
)