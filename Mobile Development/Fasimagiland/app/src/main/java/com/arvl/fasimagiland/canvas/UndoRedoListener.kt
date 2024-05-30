package com.arvl.fasimagiland.canvas

interface UndoRedoListener {
    fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean)
}