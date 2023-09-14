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
import com.jiachian.nbatoday.AWAY_TEAM
import com.jiachian.nbatoday.HOME_TEAM
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.data.local.team.NBATeam
import java.lang.reflect.Type

val testGeneralGson: Gson = GsonBuilder()
    .registerTypeAdapter(NBATeam::class.java, TestNBATeamDeserializer())
    .create()

val testNBATeamGson: Gson = GsonBuilder()
    .registerTypeAdapter(NBATeam::class.java, TestNBATeamTypeAdapter())
    .create()

class TestNBATeamTypeAdapter : JsonDeserializer<NBATeam>, JsonSerializer<NBATeam> {

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
        return if (teamId == HOME_TEAM_ID) HOME_TEAM else AWAY_TEAM
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

class TestNBATeamDeserializer : JsonDeserializer<NBATeam> {

    companion object {
        private const val TEAM_ID = "teamId"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): NBATeam {
        val jsonObject = jsonElement.asJsonObject
        val teamId = jsonObject.get(TEAM_ID).asInt
        return NBATeam.getTeamById(teamId) ?: if (teamId == HOME_TEAM_ID) HOME_TEAM else AWAY_TEAM
    }
}
