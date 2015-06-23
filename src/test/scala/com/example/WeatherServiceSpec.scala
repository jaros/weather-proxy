package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import scala.concurrent.duration._
import spray.http._
import StatusCodes._

import scala.concurrent.duration.FiniteDuration

class WeatherServiceSpec extends Specification with Specs2RouteTest with WeatherService {
  def actorRefFactory = system

  "MyService" should {

    implicit val routeTestTimeout = new FiniteDuration(5, SECONDS)

    "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return weather forecast for GET request with location parameter" in {
      Get("/?location=zurich") ~> myRoute ~> check {
        responseAs[String] must contain("coord")
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/?location=zurich") ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
