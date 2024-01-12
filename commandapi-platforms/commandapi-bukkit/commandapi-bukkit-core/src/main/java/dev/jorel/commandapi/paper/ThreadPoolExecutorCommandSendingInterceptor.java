package dev.jorel.commandapi.paper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dev.jorel.commandapi.SafeVarHandle;

/**
 * Since <a href="https://github.com/PaperMC/Paper/pull/3116">PaperMC/Paper#3116</a>, Paper sometimes reads
 * from the Brigadier CommandDispatcher asyncronously. This can cause ConcurrentModificationExceptions if the
 * CommandAPI tries to create a command and write to the dispatcher at the same time.
 * <p>
 * From Paper build paper-1.18-31 to paper-1.19.2-117, `net.minecraft.server.CommandDispatcher` submits its
 * CommandDispatcher reading tasks into the `net.minecraft.server.MCUtil.asyncExecutor` using {@link ThreadPoolExecutor#execute(Runnable)}.
 * <p>
 * From Paper build paper-1.19-118 to present, tasks are submitted using the 
 * `net.minecraft.server.CommandDispatcher.COMMAND_SENDING_POOL` object instead.
 * <p>
 * This class intercepts calls to that method to enforce our special read-write access.
 * <p>
 * See <a href="https://github.com/JorelAli/CommandAPI/pull/501">CommandAPI#501</a> for more details.
 */
public class ThreadPoolExecutorCommandSendingInterceptor extends ThreadPoolExecutor {
	// Intercept calls to `ThreadPoolExecutor#execute(...)` by becoming the thread pool

    private final ThreadPoolExecutor originalPool;
	private final CommandDispatcherReadWriteManager commandDispatcherReadWriteManager;
    private final Class<?> commandDispatcherClass;

    public ThreadPoolExecutorCommandSendingInterceptor(CommandDispatcherReadWriteManager commandDispatcherReadWriteManager, 
                                                       Class<?> commandDispatcherClass, SafeVarHandle<?, ThreadPoolExecutor> originalLocation) {
        // These fields don't matter since we'll delegate to originalPool, but they must be valid settings
        super(0, 1, 0, TimeUnit.NANOSECONDS, new LinkedBlockingDeque<>());
        // Conviently, both fields are static
        this.originalPool = originalLocation.get(null);
		this.commandDispatcherReadWriteManager = commandDispatcherReadWriteManager;
        this.commandDispatcherClass = commandDispatcherClass;

        originalLocation.set(null, this);
    }

    @Override
    public void execute(Runnable task) {
        Class<?> submittingClass = task.getClass().getEnclosingClass();
		System.out.println("Intercepted call to `ThreadPoolExecutor#execute(...)");
        System.out.println(submittingClass);
		new IllegalArgumentException().printStackTrace(System.out);
        if(this.commandDispatcherClass.equals(submittingClass)) {
			System.out.println("Found a Dispatcher read task!");
            // If `net.minecraft.server.CommandDispatcher` is submitting the task,
            //  enforce our read-access control
            task = this.commandDispatcherReadWriteManager.wrapReadTask(task);
        }
        originalPool.execute(task);
    }

    // Otherwise, delegate to the original ThreadPoolExecutor
    @Override
    public int hashCode() {
        return originalPool.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return originalPool.equals(obj);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return originalPool.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return originalPool.submit(task, result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return originalPool.submit(task);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return originalPool.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return originalPool.invokeAny(tasks, timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return originalPool.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return originalPool.invokeAll(tasks, timeout, unit);
    }

    @Override
    public void shutdown() {
        originalPool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return originalPool.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return originalPool.isShutdown();
    }

    @Override
    public boolean isTerminating() {
        return originalPool.isTerminating();
    }

    @Override
    public boolean isTerminated() {
        return originalPool.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return originalPool.awaitTermination(timeout, unit);
    }

    @Override
    public void setThreadFactory(ThreadFactory threadFactory) {
        originalPool.setThreadFactory(threadFactory);
    }

    @Override
    public ThreadFactory getThreadFactory() {
        return originalPool.getThreadFactory();
    }

    @Override
    public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
        originalPool.setRejectedExecutionHandler(handler);
    }

    @Override
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return originalPool.getRejectedExecutionHandler();
    }

    @Override
    public void setCorePoolSize(int corePoolSize) {
        originalPool.setCorePoolSize(corePoolSize);
    }

    @Override
    public int getCorePoolSize() {
        return originalPool.getCorePoolSize();
    }

    @Override
    public boolean prestartCoreThread() {
        return originalPool.prestartCoreThread();
    }

    @Override
    public int prestartAllCoreThreads() {
        return originalPool.prestartAllCoreThreads();
    }

    @Override
    public boolean allowsCoreThreadTimeOut() {
        return originalPool.allowsCoreThreadTimeOut();
    }

    @Override
    public void allowCoreThreadTimeOut(boolean value) {
        originalPool.allowCoreThreadTimeOut(value);
    }

    @Override
    public void setMaximumPoolSize(int maximumPoolSize) {
        originalPool.setMaximumPoolSize(maximumPoolSize);
    }

    @Override
    public int getMaximumPoolSize() {
        return originalPool.getMaximumPoolSize();
    }

    @Override
    public void setKeepAliveTime(long time, TimeUnit unit) {
        originalPool.setKeepAliveTime(time, unit);
    }

    @Override
    public long getKeepAliveTime(TimeUnit unit) {
        return originalPool.getKeepAliveTime(unit);
    }

    @Override
    public BlockingQueue<Runnable> getQueue() {
        return originalPool.getQueue();
    }

    @Override
    public boolean remove(Runnable task) {
        return originalPool.remove(task);
    }

    @Override
    public void purge() {
        originalPool.purge();
    }

    @Override
    public int getPoolSize() {
        return originalPool.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return originalPool.getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return originalPool.getLargestPoolSize();
    }

    @Override
    public long getTaskCount() {
        return originalPool.getTaskCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return originalPool.getCompletedTaskCount();
    }

    @Override
    public String toString() {
        return originalPool.toString();
    }
}
