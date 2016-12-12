import models._

trait TestData {
  val FOUR = 4
  val FIVE = 5
  val SIX = 6
  val SEVEN = 7

  val testUsers = Seq(
    User(Some(1), "user1", "password1"),
    User(Some(2), "user2", "password2"),
    User(Some(3), "user3", "password3")
  )

  val testSessionsForUser1 = Seq(
    Session(Some(1), "11111111", 1, "session1", "s1"),
    Session(Some(2), "22222222", 1, "session2", "s2")
  )

  val testSessionsForUser2 = Seq(
    Session(Some(3), "33333333", 2, "session3", "s3"),
    Session(Some(FOUR), "44444444", 2, "session4", "s4")
  )

  val testSessions = testSessionsForUser1 ++ testSessionsForUser2

  val questionId = 5
  val pointsForRightAnswer: Int = 10
  val pointsForWrongAnswer: Int = -10


  val testAnswerOptions = Seq(
    AnswerOption(Some(1), Some(questionId), true, "rightAnswer1", pointsForRightAnswer),
    AnswerOption(Some(2), Some(questionId), true, "rightAnswer2", pointsForRightAnswer),
    AnswerOption(Some(3), Some(questionId), false, "falseAnswer1", pointsForWrongAnswer),
    AnswerOption(Some(FOUR), Some(questionId), false, "falseAnswer2", pointsForWrongAnswer),
    AnswerOption(Some(FIVE), Some(questionId), false, "falseAnswer3", pointsForWrongAnswer)
  )

  val preparationQuestions = Seq(
    Question(Some(1), 1, "subject1", "content1", "preparation", "freetext", FormatAttributes(Map()), None),
    Question(Some(2), 1, "subject2", "content2", "preparation", "freetext", FormatAttributes(Map()), None),
    Question(Some(3), 1, "subject3", "content3", "preparation", "flashcard",
      FormatAttributes(Map("backside" -> "backside3")), None),
    Question(Some(FOUR), 1, "subject4", "content4", "preparation", "flashcard",
      FormatAttributes(Map("backside" -> "backside4")), None),
    Question(Some(FIVE), 1, "subject5", "content5", "preparation", "mc",
      FormatAttributes(Map()), Some(testAnswerOptions))
  )

  val liveQuestions = Seq(
    Question(Some(SIX), 1, "subject6", "content6", "live", "freetext", FormatAttributes(Map()), None),
    Question(Some(SEVEN), 1, "subject7", "content7", "live", "flashcard",
      FormatAttributes(Map("backside" -> "backside7")), None)
  )

  val testQuestions = preparationQuestions ++ liveQuestions

  val testFreetextAnswersForQuestionOne = Seq(
    FreetextAnswer(Some(1), 1, 1, "freetextAnswer1", "freetextText1"),
    FreetextAnswer(Some(2), 1, 1, "freetextAnswer2", "freetextText2")
  )

  val testFreetextAnswers = testFreetextAnswersForQuestionOne ++
    Seq(FreetextAnswer(Some(3), 2, 1, "freetextAnswer3", "freetextText3"))

  val testChoiceAnswers = Seq(
    ChoiceAnswer(Some(1), questionId, 1, 1),
    ChoiceAnswer(Some(2), questionId, 1, 2),
    ChoiceAnswer(Some(3), questionId, 1, 3),
    ChoiceAnswer(Some(FOUR), questionId, 1, 3),
    ChoiceAnswer(Some(FIVE), questionId, 1, FOUR)
  )

  val testComments = Seq(
    Comment(Some(1), 1, 1, false, "subject1", "text1", "1317574085000"),
    Comment(Some(2), 1, 1, true, "subject2", "text2", "1317574095000"),
    Comment(Some(3), 1, 1, true, "subject2", "text2", "1317574085000")
  )

  val testFeatures = Seq(
    Features(Some(1), 1, true, true, true, true, true, true, true, true, true, true),
    Features(Some(2), 2, true, false, false, false, false, false, false, false, false, false)
  )
}
