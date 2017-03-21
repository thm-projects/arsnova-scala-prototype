package de.thm.arsnova.models

case class Session(
                    id: Option[SessionId],
                    keyword: String,
                    userId: UserId,
                    title: String,
                    shortName: String,
                    lastOwnerActivity: String,
                    creationTime: String,
                    active: Boolean,
                    feedbackLock: Boolean,
                    flipFlashcards: Boolean,
                    features: Option[Features]
                  )
