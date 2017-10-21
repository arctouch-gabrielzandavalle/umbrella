package com.foo.umbrella.ui.home

import android.util.Log
import com.foo.umbrella.data.ApiServicesProvider
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomePresenter(private val view: HomeContracts.View) : HomeContracts.Presenter {

    val apiServicesProvider = ApiServicesProvider(view.getContext());

    override fun loadForecastForZip() {
        apiServicesProvider.weatherService.forecastForZipObservable("94016")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    view.showForecastForZip(it.response().body())
                    Log.e("TAG", it.response().toString())
                }
    }

}