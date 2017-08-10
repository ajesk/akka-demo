import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by aaron on 6/1/17.
  */
class Greeting(val name: String = "fred") extends Actor {
  override def receive: Receive = {
    case "hello" => println(s"$name: fu fam")
    case _       => println(s"$name: omg underscore")
  }
}

object GreeterTest extends App {
  val system: ActorSystem = ActorSystem("System")
  val greetingActor = system.actorOf(Props(new Greeting()), name = "disgruntledgreeter")
  greetingActor ! "hello"
  greetingActor ! "..."
}

