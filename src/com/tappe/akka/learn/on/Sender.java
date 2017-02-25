package com.tappe.akka.learn.on;

import akka.actor.UntypedActor;

/**
 * Created by BuleSky on 2017/2/9.
 */
public class Sender extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Throwable {

        if (message == Msg.GREET) {
            System.out.println("Hello i gotted you ");
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(message);
        }
    }


    public static enum Msg {
        GREET, DONE;
    }
}
