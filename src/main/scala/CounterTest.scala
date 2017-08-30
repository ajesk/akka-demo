import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case object Start
case object Stop
case object Increment
case object Decrement

class Counter extends Actor {
  var count: Int = 0

  override def receive: Receive = {
    case Increment =>
      count += 1
      println(count)
    case Decrement =>
      count -= 1
      println(count)
  }
}

class Randomizer(counter: ActorRef) extends Actor {
  var continue = true

  override def receive: Receive = {
    case Start =>
      while (continue) {
        // Change this number to start to move the needle
        if (math.random < .5)
          counter ! Increment
        else
          counter ! Decrement
        Thread.sleep(1000)
      }
    case Stop =>
      context.stop(self)
  }
}

object CounterTest extends App {
  val system = ActorSystem("CounterSystem")
  val counter = system.actorOf(Props[Counter], name = "count")
  val randomizer = system.actorOf(Props(new Randomizer(counter)), name = "random")
  // start them going
  randomizer ! Start
  Thread.sleep(25000)
  randomizer ! Stop
}
