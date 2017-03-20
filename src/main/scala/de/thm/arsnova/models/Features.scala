package de.thm.arsnova.models

case class Features(
                     id: Option[FeaturesId],
                     sessionId: SessionId,
                     slides: Boolean,
                     flashcards: Boolean,
                     peerGrading: Boolean,
                     peerInstruction: Boolean,
                     comments: Boolean,
                     tileView: Boolean,
                     jitt: Boolean,
                     learningProgress: Boolean,
                     feedback: Boolean,
                     liveQuestions: Boolean
                   )