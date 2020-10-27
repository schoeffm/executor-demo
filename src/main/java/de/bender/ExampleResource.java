package de.bender;

import de.bender.DemoApplication.MyExecutor;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ForkJoinPool;

@Path("/hello")
public class ExampleResource {

    @Inject
    @MyExecutor
    ManagedExecutor executor;

    Logger logger = LoggerFactory.getLogger(Exception.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        logger.info("\n- Processor-Count: {}" +
                    "\n- ForkJoinPool Parallelism: {}",
                Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.commonPool().getParallelism());

        logger.info(Thread.currentThread().getName());
        executor.newIncompleteFuture()
                .supplyAsync(() -> Thread.currentThread().getName())
                .thenApply(name -> { sleep(5000L); return name; })
                .thenAccept(name -> logger.info("Finished async processing: {}", name));

        logger.info("Delivering REST-Response");

        return "triggered";
    }



    void sleep(long pauseTime) {
        try {
            Thread.sleep(pauseTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
