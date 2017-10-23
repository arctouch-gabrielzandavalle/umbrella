package com.foo.umbrella.data.model

data class CurrentWeatherDisplay(
        val currentObservation: CurrentObservation,
        val dailyForecastList: List<DailyForecast>
)