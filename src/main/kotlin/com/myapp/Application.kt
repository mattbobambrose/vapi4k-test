package com.myapp

import com.vapi4k.api.model.enums.OpenAIModelType
import com.vapi4k.dsl.assistant.ToolCall
import com.vapi4k.server.Vapi4k
import com.vapi4k.utils.json.JsonElementUtils.stringValue
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
  embeddedServer(
    factory = CIO,
    // Use the PORT environment variable if it exists, otherwise default to 8080
    port = System.getenv("PORT")?.toInt() ?: 8080,
    host = "0.0.0.0",
    module = Application::module,
  )
    .start(wait = true)
}

fun Application.module() {
  install(Vapi4k) {
    vapi4kApplication {
      onAssistantRequest { request ->
        assistant {
          openAIModel {
            modelType = OpenAIModelType.GPT_4_TURBO

            tools {
              serviceTool(WeatherOutside())

              manualTool {
                name = "manualWeatherLookup"
                description = "Look up the weather for a city and state"
                parameters {
                  parameter {
                    name = "city"
                    description = "The city to look up"
                  }
                  parameter {
                    name = "state"
                    description = "The state to look up"
                  }
                }
                onInvoke { args ->
                  val city = args.stringValue("city")
                  val state = args.stringValue("state")
                  result = "The weather in $city, $state is cloudy"
                }
              }
            }
          }
        }
      }
    }
  }
}

class WeatherOutside {
  @ToolCall("Look up the weather for a city and state")
  fun weatherForCityAndState(
    city: String,
    state: String,
  ): String {
    return "The weather in $city, $state is sunny"
  }
}