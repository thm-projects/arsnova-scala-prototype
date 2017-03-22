package de.thm.arsnova.models

case class SessionMotd(
                        id: Option[SessionMotdId],
                        sessionId: SessionId,
                        startdate: String,
                        enddate: String,
                        title: String,
                        text: String
                      )

case class GlobalMotd(
                       id: Option[GlobalMotdId],
                       startdate: String,
                       enddate: String,
                       title: String,
                       text: String,
                       audience: String
                     )
