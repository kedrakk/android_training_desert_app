package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKES=2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel :ViewModel(){

    private val _quantity=MutableLiveData<Int>()
    val quantity:LiveData<Int> = _quantity

    private val _flavour=MutableLiveData<String>()
    val flavour:LiveData<String> = _flavour

    private val _date=MutableLiveData<String>()
    val date:LiveData<String> = _date

    private val _price=MutableLiveData<Double>()
    val price:LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    fun setQuantity(numbersOfCupcakes:Int){
        _quantity.value=numbersOfCupcakes
        updatePrice()
    }

    fun setFlavour(desiredFlavour:String){
        _flavour.value=desiredFlavour
    }

    fun setDate(pickupDate:String){
        _date.value=pickupDate
        updatePrice()
    }

    fun hasNoFlavourSet():Boolean{
        return _flavour.value.isNullOrEmpty()
    }

    private fun getPickUpOption():List<String>{
        val option= mutableListOf<String>()
        val formatter= SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar=Calendar.getInstance()
        repeat(4){
            option.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return option
    }

    val dateOptions=getPickUpOption()

    fun resetOrder(){
        _quantity.value=0
        _flavour.value=""
        _date.value=dateOptions[0]
        _price.value=0.0
    }

    init {
        resetOrder()
    }

    private fun updatePrice(){
        var calculatedPrice=(quantity.value?:0)* PRICE_PER_CUPCAKES
        if(dateOptions[0]==_date.value){
            calculatedPrice+= PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value=calculatedPrice
    }
}