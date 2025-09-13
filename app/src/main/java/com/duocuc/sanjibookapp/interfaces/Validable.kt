package com.duocuc.sanjibookapp.interfaces

interface Validable {
    fun isValidEmail(): Boolean
    fun isValidPassword(): Boolean
    fun validate(): Map<String, String?>
}