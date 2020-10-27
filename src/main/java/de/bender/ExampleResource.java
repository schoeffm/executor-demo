package de.bender;

import de.bender.DemoApplication.MyExecutor;
import org.eclipse.microprofile.context.ManagedExecutor;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        System.out.println(
                "\n- Processor-Count: " + Runtime.getRuntime().availableProcessors() +
                "\n- ForkJoinPool Parallelism: " + ForkJoinPool.commonPool().getParallelism());

        System.out.println(Thread.currentThread().getName());

        executor.newIncompleteFuture()
                .supplyAsync(() -> Thread.currentThread().getName())
                .thenApply(name -> { sleep(5000L); return name; })
                .thenAccept(name -> System.out.println("Finished async processing: " + name));

        System.out.println("Delivering REST-Response");

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
