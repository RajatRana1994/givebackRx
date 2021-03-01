package com.app.givebackrx.listeners

interface CustomSpinnerListener {
    fun onItemClick(item:String)
}

interface SingleListCLickListener {
    fun onSingleListClick(item:Any,position:Int)
}

interface PairListCLickListener {
    fun onFirstListClick(item:Any,position:Int)
    fun onSecondListClick(item:Any,position:Int)
}