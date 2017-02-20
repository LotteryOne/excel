package com.tappe.akka.learn.on;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

/**
 * Created by BuleSky on 2017/2/9.
 */
public class HelloMsg extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Sender.Msg.DONE) {
            greeter.tell(Sender.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

    ActorRef greeter;

    @Override
    public void preStart() throws Exception {
        greeter = getContext().actorOf(Props.create(Sender.class), "senders");
        System.out.println("senders path:" + greeter.path());
        greeter.tell(Sender.Msg.GREET, getSelf());

    }

    public static void main(String[] args) {
        //, ConfigFactory.load("src/sampleConf.conf")
        ActorSystem system = ActorSystem.create("hello", ConfigFactory.load("sampleConf.conf"));
        ActorRef a = system.actorOf(Props.create(HelloMsg.class), "hello world");
        System.out.println("Hello world path:::" + a.path());
    }
}
