package fancywords

interface Exam {
    val word: String
    val examType: Byte
    val correctChoice: Byte
    //val time: Long

    class InvalidExamTypeException : Exception()
}