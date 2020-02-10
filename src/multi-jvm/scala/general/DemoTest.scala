package general

import loci._
import loci.transmitter.rescala._
import loci.serializer.upickle._
import loci.communicator.tcp._
import rescala.default._
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._

class DemoTestMultiJvmNode1 extends FlatSpec {
	s"Sender" should "send" in {
		val rt = multitier start new Instance[DemoTest.Sender](
			listen[DemoTest.Receiver] {
				TCP(54321)
			}
		)
		Await.result(rt.terminated, 10 seconds)
	}
}

class DemoTestMultiJvmNode2 extends FlatSpec {
	s"Receiver" should "receive" in {
		val rt = multitier start new Instance[DemoTest.Receiver](
			connect[DemoTest.Sender] {
				TCP("localhost", 54321)
			}
		)
		Await.result(rt.terminated, 10 seconds)
	}
}

@multitier object DemoTest {
	@peer type Sender <: {type Tie <: Single[Receiver]}
	@peer type Receiver <: {type Tie <: Single[Sender]}

	val ping = on[Sender] {
		Evt[Unit]()
	}
	val pong = on[Receiver] {
		Evt[Unit]()
	}

	on[Receiver] {
		ping.asLocal observe { _ =>
			println("ping")
			pong.fire
			multitier.terminate()
		}
	}

	on[Sender] {
		pong.asLocal observe { _ =>
			println("pong")
			multitier.terminate()
			System.exit(0) // Seems like the TCP listener prevents termination?
		}
	}

	def main() = on[Sender] {
		ping.fire(())
	}
}