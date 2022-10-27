package orhan.uckulac.a7minutesworkout

class ExerciseModel (
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean,
    private var isSelected: Boolean,
){
    // ID getter and setter
    fun getId(): Int{
        return id
    }
    fun setId(id: Int){
        this.id = id
    }

    // Name getter and setter
    fun getName(): String {
        return name
    }
    fun setName(name: String){
        this.name = name
    }

    // Image getter and setter
    fun getImage(): Int {
        return image
    }
    fun setImage(image: Int){
        this.image = image
    }

    // isCompleted getter and setter
    fun setIsCompleted(): Boolean {
        return isCompleted
    }
    fun setIsCompleted(isCompleted: Boolean){
        this.isCompleted = isCompleted
    }

    // isSelected getter and setter
    fun setIsSelected(): Boolean {
        return isSelected
    }
    fun setIsSelected(isSelected: Boolean){
        this.isSelected = isSelected
    }

}