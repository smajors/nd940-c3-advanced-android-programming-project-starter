package com.udacity.button


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
    object Undefined : ButtonState()
}