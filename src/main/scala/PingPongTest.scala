import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by aaron on 6/1/17.
  */
case object PingMessage
case object PongMessage
case object StartMessage
case object StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def increment(): Unit = {
    count += 1
    println(s"ping $count")
  }

  override def receive: Receive = {
    case StartMessage =>
      increment()
      pong ! PingMessage
    case PongMessage =>
      increment()
      if (count > 999) {
        sender ! StopMessage
        println("ping stopped")
        context.stop(self)
      } else {
        sender ! PingMessage
      }
  }
}

class Pong extends Actor {
  override def receive: Receive = {
    case PingMessage =>
      println(" pong")
      sender ! PongMessage
    case StopMessage =>
      println("pong stopped")
      context.stop(self)
  }
}


object PingPongTest extends App{
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")
  // start them going
  ping ! StartMessage
}
