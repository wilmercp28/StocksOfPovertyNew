package com.example.stocksofpoverty.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class MutableStateAdapter : JsonSerializer<MutableState<*>>, JsonDeserializer<MutableState<*>> {
    override fun serialize(src: MutableState<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(src.value)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MutableState<*> {
        val valueType = (typeOfT as ParameterizedType).actualTypeArguments[0]

        val value: Any = context.deserialize(json, valueType)
        return mutableStateOf(value)
    }
}

class MutableStateDoubleInstanceCreator : InstanceCreator<MutableState<Double>> {
    override fun createInstance(type: Type?): MutableState<Double> {
        return mutableStateOf(0.0)
    }
}

fun createDynamicGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(MutableState::class.java, MutableStateAdapter())
        .registerTypeAdapter(object : TypeToken<MutableState<Double>>() {}.type, MutableStateDoubleInstanceCreator())
        .create()
}