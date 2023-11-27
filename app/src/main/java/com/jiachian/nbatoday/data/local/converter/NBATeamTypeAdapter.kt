package com.jiachian.nbatoday.data.local.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import java.lang.reflect.Type

val typeAdapterGson: Gson = GsonBuilder()
    .registerTypeAdapter(NBATeam::class.java, NBATeamTypeAdapter())
    .create()

class NBATeamTypeAdapter : JsonDeserializer<NBATeam>, JsonSerializer<NBATeam> {

    companion object {
        private const val DATA = "data"
        private const val TEAM_ID = "teamId"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): NBATeam {
        val jsonObject = jsonElement.asJsonObject
        val data = jsonObject.get(DATA).asJsonObject
        val teamId = data.get(TEAM_ID).asInt
        return NBATeam.getTeamById(teamId) ?: teamOfficial
    }

    override fun serialize(
        jsonElement: NBATeam,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement))
        return jsonObject
    }
}
