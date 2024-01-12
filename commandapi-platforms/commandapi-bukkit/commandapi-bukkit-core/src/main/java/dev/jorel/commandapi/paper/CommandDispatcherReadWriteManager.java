package dev.jorel.commandapi.paper;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Since <a href="https://github.com/PaperMC/Paper/pull/3116">PaperMC/Paper#3116</a>, Paper sometimes reads
 * from the Brigadier CommandDispatcher asyncronously. This can cause ConcurrentModificationExceptions if the
 * CommandAPI tries to create a command and write to the dispatcher at the same time.
 * <p>
 * This class handles locking read and write access at appropriate times.
 * <p>
 * See <a href="https://github.com/JorelAli/CommandAPI/pull/501">CommandAPI#501</a> for more details.
 */
public class CommandDispatcherReadWriteManager {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public void runWriteTask(Runnable writeTask) {
		readWriteLock.writeLock().lock();
		try {
			writeTask.run();
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	public <T> T runWriteTask(Supplier<T> writeTask) {
		readWriteLock.writeLock().lock();
		try {
			return writeTask.get();
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

    public Runnable wrapReadTask(Runnable originalTask) {
        return () -> {
            readWriteLock.readLock().lock();
            try {
                originalTask.run();
            } finally {
                readWriteLock.readLock().unlock();
            }
        };
    }
}