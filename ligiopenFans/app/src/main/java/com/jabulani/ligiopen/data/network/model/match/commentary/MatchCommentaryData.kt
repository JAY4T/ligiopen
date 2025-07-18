package com.jabulani.ligiopen.data.network.model.match.commentary

import com.jabulani.ligiopen.data.network.model.match.events.CornerEventData
import com.jabulani.ligiopen.data.network.model.match.events.FoulEventData
import com.jabulani.ligiopen.data.network.model.match.events.FreeKickEventData
import com.jabulani.ligiopen.data.network.model.match.events.FullTimeEventData
import com.jabulani.ligiopen.data.network.model.match.events.GoalEventData
import com.jabulani.ligiopen.data.network.model.match.events.GoalKickEventData
import com.jabulani.ligiopen.data.network.model.match.events.HalfTimeEventData
import com.jabulani.ligiopen.data.network.model.match.events.InjuryEventData
import com.jabulani.ligiopen.data.network.model.match.events.KickOffEventData
import com.jabulani.ligiopen.data.network.model.match.events.MatchEventType
import com.jabulani.ligiopen.data.network.model.match.events.OffsideEventData
import com.jabulani.ligiopen.data.network.model.match.events.OwnGoalEventData
import com.jabulani.ligiopen.data.network.model.match.events.PenaltyEventData
import com.jabulani.ligiopen.data.network.model.match.events.SubstitutionEventData
import com.jabulani.ligiopen.data.network.model.match.events.ThrowInEventData
import com.jabulani.ligiopen.data.network.model.player.PlayerDetails
import com.jabulani.ligiopen.data.network.model.club.ClubDetails
import com.jabulani.ligiopen.data.network.model.file.FileData
import kotlinx.serialization.Serializable

@Serializable
data class MatchCommentaryData(
    val matchCommentaryId: Int,
    val postMatchAnalysisId: Int,
    val files: List<FileData>,
    val minute: String?,
    val createdAt: String,
    val updatedAt: String?,
    val archived: Boolean,
    val archivedAt: String?,
    val matchEventType: MatchEventType,
    val mainPlayer: PlayerDetails?,
    val secondaryPlayer: PlayerDetails?,
    val homeClub: ClubDetails,
    val awayClub: ClubDetails,
    val cornerEvent: CornerEventData?,
    val foulEvent: FoulEventData?,
    val freeKickEvent: FreeKickEventData?,
    val fullTimeEvent: FullTimeEventData?,
    val halfTimeEvent: HalfTimeEventData?,
    val goalEvent: GoalEventData?,
    val ownGoalEvent: OwnGoalEventData?,
    val goalKickEvent: GoalKickEventData?,
    val injuryEvent: InjuryEventData?,
    val kickOffEvent: KickOffEventData?,
    val substitutionEvent: SubstitutionEventData?,
    val offsideEvent: OffsideEventData?,
    val penaltyEvent: PenaltyEventData?,
    val throwInEvent: ThrowInEventData?
)
