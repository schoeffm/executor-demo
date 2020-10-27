package de.bender;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApplicationScoped
@ApplicationPath("rest")
public class DemoApplication extends Application {
    @Produces
    @ApplicationScoped
    @MyExecutor
    ManagedExecutor executor = ManagedExecutor.builder()
            .propagated(ThreadContext.TRANSACTION)
            .maxAsync(10)
            .maxQueued(25)
            .build();

    void disposeExecutor(@Disposes @MyExecutor ManagedExecutor exec) {
        exec.shutdownNow();
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
    public @interface MyExecutor {}
}
