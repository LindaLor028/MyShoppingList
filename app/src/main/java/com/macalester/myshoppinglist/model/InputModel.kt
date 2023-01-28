package com.macalester.myshoppinglist.model

object InputModel {

    fun validateInput(a: String) : Boolean {
        return a.isNullOrEmpty()
    }

}