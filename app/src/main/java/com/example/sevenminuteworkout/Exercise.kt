package com.example.sevenminuteworkout

class Exercise(
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean = false,
    private var isSelected: Boolean = false) {

    fun getId(): Int {
        return id
    }
    fun setId(value: Int) {
        this.id = value
    }
    fun getName(): String {
        return name;
    }
    fun setName(value: String) {
        name = value
    }
    fun getImage(): Int {
        return image
    }
    fun setImage(value: Int) {
        image = value
    }
    fun getIsCompleted(): Boolean {
        return isCompleted
    }
    fun setIsCompleted(value: Boolean) {
        isCompleted = value
    }
    fun getIsSelect(): Boolean {
        return isSelected
    }
    fun setIsSelected(value: Boolean) {
        isSelected = value
    }
}