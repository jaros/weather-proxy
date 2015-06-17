package com.example

import akka.actor.Actor
import akka.util.Timeout
import spray.client.pipelining._
import spray.http.MediaTypes._
import spray.http._
import spray.httpx.marshalling.ToResponseMarshallable
import spray.routing._
import scala.concurrent.duration._

import scala.concurrent.{Await, ExecutionContext, Future}
import spray.util._



// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  //  implicit val eh = ExceptionHandler.default

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {
  this: MyServiceActor =>

//    implicit val system = context.system
//
//  import system.dispatcher

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit lazy val timeout = Timeout(5 seconds)


  val myRoute =
    path("") {
      parameter('location) { cityName =>
        get {
          respondWithMediaType(`application/json`) {
            complete {
              findWeatherAtLocation(cityName)
            }
          }
        }
      }

    }

  def findWeatherAtLocation(cityName: String): ToResponseMarshallable = {
    //    val response: Future[HttpResponse] = response2(cityName)
    //


    //    s"""{"status" : "$cityName"}"""

    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive

    val response: Future[HttpResponse] = pipeline(Get(s"http://api.openweathermap.org/data/2.5/weather?q=$cityName"))

        val result: HttpResponse = Await.result(response, 5 seconds)
        result.entity.data.asString


//    ""
  }
}