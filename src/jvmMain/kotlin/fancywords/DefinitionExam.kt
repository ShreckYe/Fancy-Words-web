package fancywords

class DefinitionExam(
    override val word: String,
    val definition: String,
    val wordChoices: Array<String?>,
    override val correctChoice: Byte
) : Exam {
    override val examType: Byte get() = EXAM_TYPE_DEFINITION
}