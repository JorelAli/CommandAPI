package dev.jorel.commandapi.paper;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dev.jorel.commandapi.SafeVarHandle;
import dev.jorel.commandapi.preprocessor.RequireField;

/**
 * Since <a href="https://github.com/PaperMC/Paper/pull/3116">PaperMC/Paper#3116</a>, Paper sometimes reads
 * from the Brigadier CommandDispatcher asyncronously. This can cause ConcurrentModificationExceptions if the
 * CommandAPI tries to create a command and write to the dispatcher at the same time.
 * <p>
 * From Paper build paper-1.15.2-177 to paper-1.18-30, `net.minecraft.server.CommandDispatcher` submits its
 * CommandDispatcher reading tasks into the {@link ForkJoinPool#commonPool()} using {@link ForkJoinPool#execute(Runnable)}.
 * <p>
 * This class intercepts calls to that method to enforce our special read-write access.
 * <p>
 * See <a href="https://github.com/JorelAli/CommandAPI/pull/501">CommandAPI#501</a> for more details.
 */
@RequireField(in = ForkJoinPool.class, name = "common", ofType = ForkJoinPool.class)
public class ForkJoinPoolCommandSendingInterceptor extends ForkJoinPool {
	// Intercept calls to `ForkJoinPool.common().execute(...)` by becoming the common pool
    private static final SafeVarHandle<ForkJoinPool, ForkJoinPool> forkJoinPoolCommon;

    static {
        forkJoinPoolCommon = SafeVarHandle.ofOrNull(ForkJoinPool.class, "common", "common", ForkJoinPool.class);
    }

    private final ForkJoinPool commonPool;
	private final CommandDispatcherReadWriteManager commandDispatcherReadWriteManager;
    private final Class<?> commandDispatcherClass;

    public ForkJoinPoolCommandSendingInterceptor(CommandDispatcherReadWriteManager commandDispatcherReadWriteManager, Class<?> commandDispatcherClass) {
        this.commonPool = ForkJoinPool.commonPool();
		this.commandDispatcherReadWriteManager = commandDispatcherReadWriteManager;
        this.commandDispatcherClass = commandDispatcherClass;

        forkJoinPoolCommon.set(null, this);
    }

    @Override
    public void execute(Runnable task) {
        Class<?> submittingClass = task.getClass().getEnclosingClass();
		System.out.println("Intercepted call to `ForkJoinPool.commonPool().execute(...)");
        System.out.println(submittingClass);
		new IllegalArgumentException().printStackTrace(System.out);
        if(this.commandDispatcherClass.equals(submittingClass)) {
			System.out.println("Found a Dispatcher read task!");
            // If `net.minecraft.server.CommandDispatcher` is submitting the task,
            //  enforce our read-access control
            task = this.commandDispatcherReadWriteManager.wrapReadTask(task);
        }
        commonPool.execute(task);
    }

    // Otherwise, delegate to the original common ForkJoinPool
    @Override
	public int hashCode() {
		return commonPool.hashCode();
	}

    @Override
	public boolean equals(Object obj) {
		return commonPool.equals(obj);
	}

    @Override
	public <T> T invoke(ForkJoinTask<T> task) {
		return commonPool.invoke(task);
	}

    @Override
	public void execute(ForkJoinTask<?> task) {
		commonPool.execute(task);
	}

    @Override
	public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
		return commonPool.submit(task);
	}

    @Override
	public <T> ForkJoinTask<T> submit(Callable<T> task) {
		return commonPool.submit(task);
	}

    @Override
	public <T> ForkJoinTask<T> submit(Runnable task, T result) {
		return commonPool.submit(task, result);
	}

    @Override
	public ForkJoinTask<?> submit(Runnable task) {
		return commonPool.submit(task);
	}

    @Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
		return commonPool.invokeAll(tasks);
	}

    @Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return commonPool.invokeAll(tasks, timeout, unit);
	}

    @Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return commonPool.invokeAny(tasks);
	}

    @Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return commonPool.invokeAny(tasks, timeout, unit);
	}

    @Override
	public ForkJoinWorkerThreadFactory getFactory() {
		return commonPool.getFactory();
	}

    @Override
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return commonPool.getUncaughtExceptionHandler();
	}

    @Override
	public int getParallelism() {
		return commonPool.getParallelism();
	}

    @Override
	public int getPoolSize() {
		return commonPool.getPoolSize();
	}

    @Override
	public boolean getAsyncMode() {
		return commonPool.getAsyncMode();
	}

    @Override
	public int getRunningThreadCount() {
		return commonPool.getRunningThreadCount();
	}

    @Override
	public int getActiveThreadCount() {
		return commonPool.getActiveThreadCount();
	}

    @Override
	public boolean isQuiescent() {
		return commonPool.isQuiescent();
	}

    @Override
	public long getStealCount() {
		return commonPool.getStealCount();
	}

    @Override
	public long getQueuedTaskCount() {
		return commonPool.getQueuedTaskCount();
	}

    @Override
	public int getQueuedSubmissionCount() {
		return commonPool.getQueuedSubmissionCount();
	}

    @Override
	public boolean hasQueuedSubmissions() {
		return commonPool.hasQueuedSubmissions();
	}

    @Override
	public String toString() {
		return commonPool.toString();
	}

    @Override
	public void shutdown() {
		commonPool.shutdown();
	}

    @Override
	public List<Runnable> shutdownNow() {
		return commonPool.shutdownNow();
	}

    @Override
	public boolean isTerminated() {
		return commonPool.isTerminated();
	}

    @Override
	public boolean isTerminating() {
		return commonPool.isTerminating();
	}

    @Override
	public boolean isShutdown() {
		return commonPool.isShutdown();
	}

    @Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return commonPool.awaitTermination(timeout, unit);
	}

    @Override
	public boolean awaitQuiescence(long timeout, TimeUnit unit) {
		return commonPool.awaitQuiescence(timeout, unit);
	}
}
