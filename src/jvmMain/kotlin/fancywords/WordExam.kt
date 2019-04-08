package fancywords

class WordExam(
    override val word: String,
    val definitionChoices: Array<String?>,
    override val correctChoice: Byte
) : Exam {
    override val examType: Byte = EXAM_TYPE_WORD

    //val time: Long get() = System.currentTimeMillis()
}