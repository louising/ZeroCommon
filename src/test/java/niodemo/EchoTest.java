package niodemo;


public class EchoTest {
}

/*
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EchoTest {
    Process server;
    EchoClient client;

    @Before
    public void setup() throws IOException, InterruptedException {
        //
    }

    @Test
    public void givenServerClient_whenServerEchosMessage_thenCorrect() {
        //
    }

    //@Test
    public void whenWakeUpCalledOnSelector_thenBlockedThreadReturns() throws Exception {
        Pipe pipe = Pipe.open();
        Selector selector = Selector.open();
        SelectableChannel channel = pipe.source();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        List<String> invocationStepsTracker = Collections.synchronizedList(new ArrayList<>());

        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            invocationStepsTracker.add(">> Count down");
            latch.countDown();
            try {
                invocationStepsTracker.add(">> Start select");
                selector.select();
                invocationStepsTracker.add(">> End select");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        invocationStepsTracker.add(">> Start await");
        latch.await();
        invocationStepsTracker.add(">> End await");

        invocationStepsTracker.add(">> Wakeup thread");
        selector.wakeup();
        //clean up
        channel.close();

        //assertThat(invocationStepsTracker).containsExactly(">> Start await", ">> Count down", ">> Start select", ">> End await", ">> Wakeup thread", ">> End select");
    }

    @After
    public void teardown() throws IOException {
        
    }    
}
 */