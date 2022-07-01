package com.micropos

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Random

class RecordedSimulation extends Simulation {
//	gatling的DSL采用预编译，始终为静态结构，所以只会生成一次，这样生成的随机数始终无效

//	val randomCartId: ThreadLocal[String] = new ThreadLocal[String] {
//		override def initialValue(): String = {
//			val random = Random.nextInt(100000).toString
//			print(random)
//			random
//		}
//	}
//
//	val randomCartItemId: ThreadLocal[String] = new ThreadLocal[String] {
//		override def initialValue(): String = Random.nextInt(1000000).toString
//	}


	val httpProtocol: HttpProtocolBuilder = http
		.baseUrl("http://localhost:8081")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")

	val headers_2: Map[String, String] = Map("Content-Type" -> "application/json")

	//val idFeeder: Iterator[Map[String, Int]] = Iterator.continually(Map("cartId" -> Random.nextInt(1000000), "cartItemId" -> Random.nextInt(1000000)))

	def randomId(): String = Random.nextInt(1000000).toString

	val scn: ScenarioBuilder = scenario("TestPos")
		.exec(http("request_0")
			.get("/api/products"))
		.exec(http("request_1")
			.get("/api/products/1"))
		.exec(http("request_2")
			.post("/api/carts")
			.headers(headers_2)
			.body(StringBody(_ => s"""{"id": ${randomId()},"items":[]}"}""")).asJson
			.check(jsonPath("$.id").saveAs("cartId")))
		.exec(http("request_3")
			.get("/api/carts/${cartId}"))
		.exec(http("request_4")
			.post("/api/carts/${cartId}")
			.headers(headers_2)
			.body(StringBody(_ => s"""      {
													|    "id": ${randomId()},
													|    "amount": 2,
													|    "product": {
													|        "id": 1,
													|        "name": "计算机软件硬件网络工程师运维电气工程师JAVA程序员电脑编程平面设计电子商务师考试IT证书考证培训 黑龙江省 详情咨询客服",
													|        "price": 100,
													|        "image": "https://img14.360buyimg.com/n1/s200x200_jfs/t1/84804/13/28175/280846/62739cf4E5ef93f8c/70ab7ace2fc353ca.jpg"
													|    }
													|}""".stripMargin)).asJson)
		.exec(http("request_5")
			.get("/api/carts/${cartId}/total"))
		.exec(http("request_6")
			.post("/api/carts/${cartId}/total"))

	setUp(scn.inject(rampUsers(500).during(30 seconds)).protocols(httpProtocol))
}
