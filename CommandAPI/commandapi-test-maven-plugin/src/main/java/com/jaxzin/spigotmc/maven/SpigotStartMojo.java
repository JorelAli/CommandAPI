package com.jaxzin.spigotmc.maven;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * <p>
 * This goal is similar to the spigotmc:run goal, EXCEPT that it is designed to
 * be bound to an execution inside your pom, rather than being run from the
 * command line.
 * </p>
 * <p>
 * When using it, be careful to ensure that you bind it to a phase in which all
 * necessary generated files and classes for the bukkit plugin will have been
 * created. If you run it from the command line, then also ensure that all
 * necessary generated files and classes for the bukkit plugin already exist.
 * </p>
 *
 */
@Mojo(name = "start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST, requiresDependencyResolution = ResolutionScope.TEST, executionStrategy = "phase='validate'")
public class SpigotStartMojo extends AbstractMojo {

	@Parameter(property = "start.version", defaultValue = "")
	private String versions;
	
	@Parameter(property = "start.works", defaultValue = "")
	private String works;
	
	@Parameter(property = "start.error", defaultValue = "")
	private String error;
	
	@Parameter(property = "start.filename", defaultValue = "")
	private String filename;
	
	@Parameter(property = "start.foldername", defaultValue = "")
	private String foldername;
	
	@Parameter(property = "start.usepaper", defaultValue = "")
	private String usepaper;
	
	@Parameter(property = "start.waitingphrase", defaultValue = "")
	private String waitingphrase;
	
	public static final long SERVER_TIMEOUT = 4; // Timeout in minutes
	public static final Map<String, String> PAPER_DOWNLOADS;
	
	static {
		PAPER_DOWNLOADS = new HashMap<>();
		// Legacy versions
		PAPER_DOWNLOADS.put("1.8.8", "https://papermc.io/api/v1/paper/1.8.8/443/download");
		PAPER_DOWNLOADS.put("1.9.4", "https://papermc.io/api/v1/paper/1.9.4/773/download");
		PAPER_DOWNLOADS.put("1.10.2", "https://papermc.io/api/v1/paper/1.10.2/916/download");
		PAPER_DOWNLOADS.put("1.11.2", "https://papermc.io/api/v1/paper/1.11.2/1104/download");
		PAPER_DOWNLOADS.put("1.12.2", "https://papermc.io/api/v1/paper/1.12.2/1618/download");
		PAPER_DOWNLOADS.put("1.13.2", "https://papermc.io/api/v1/paper/1.13.2/655/download");
		PAPER_DOWNLOADS.put("1.14.4", "https://papermc.io/api/v1/paper/1.14.4/243/download");
		
		// Currently in development
		PAPER_DOWNLOADS.put("1.15.2", "https://papermc.io/api/v1/paper/1.15.2/391/download");
		PAPER_DOWNLOADS.put("1.16.4", "https://papermc.io/api/v1/paper/1.16.4/318/download");
		
	}

	public static Process spigotProcess;
	private File baseFolder = null;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (spigotProcess != null && spigotProcess.isAlive()) {
					try {
						new SpigotStopMojo().execute();
					} catch (MojoExecutionException | MojoFailureException e) {
						e.printStackTrace();
					}
				}
			}
		});
		if (versions == null || versions.isEmpty())
			throw new MojoFailureException("No Version(s) configured!");
		if (works == null || works.isEmpty())
			throw new MojoFailureException("No Success lookup message configured!");
		if (error == null || error.isEmpty())
			throw new MojoFailureException("No Error lookup message configured!");
		if (filename == null || filename.isEmpty())
			throw new MojoFailureException("No Plugin filename configured!");

		if (new File("").getName().equalsIgnoreCase(foldername)) { // Still a stupid hack <-- But very appreciated!
			baseFolder = new File("pom.xml").getParentFile();
		} else {
			File folder = new File(foldername);
			if (folder.exists() && folder.isDirectory()) {
				baseFolder = folder;
			}
		}

		if (baseFolder == null) {
			throw new MojoFailureException("Unable to find the projects folder!");
		}

		String[] targetVersions = versions.replace("\n", "").split(",");
		for (int i = 0; i < targetVersions.length; i++)
			targetVersions[i] = targetVersions[i].trim();
		getLog().info("Testing the following Versions: " + Arrays.toString(targetVersions));

		File spigotWorkingDir = new File(baseFolder, "target/it/spigotmc");
		spigotWorkingDir.mkdirs();
		// Delete worlds
		getLog().info("Deleting worlds!");
		deleteFolder(new File(spigotWorkingDir, "world"));
		deleteFolder(new File(spigotWorkingDir, "world_nether"));
		deleteFolder(new File(spigotWorkingDir, "world_the_end"));
		// Copy plugin
		File pluginFolder = new File(spigotWorkingDir, "plugins");
		pluginFolder.mkdirs();

		File pluginfile = new File(new File(baseFolder, "target"), filename);
		if (!pluginfile.exists()) {
			throw new MojoFailureException("Plugin file not found at '" + pluginfile.getAbsolutePath() + "'");
		}
		try {
			Files.copy(pluginfile.toPath(), new File(pluginFolder, pluginfile.getName()).toPath());
		} catch (IOException e) {
			throw new MojoFailureException("Error moving Plugin file '" + pluginfile.getAbsolutePath() + "' to '"
					+ new File(pluginFolder, pluginfile.getName()).getAbsolutePath() + "' because: " + e.getMessage(),
					e);
		}

		Map<String, Exception> errors = new HashMap<>();

		for (String version : targetVersions) {
			version = version.trim();
			try {
				testVersion(version);
			} catch (Exception ex) {
				errors.put(version, ex);
				getLog().error("Error in version '" + version + "'!", ex);
			}
		}
		deleteFolder(spigotWorkingDir);

		if (!errors.isEmpty()) {
			throw new MojoFailureException(errors.size() + " Version(s) had problems!");
		}

	}

	private void testVersion(String version) throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting Spigot '" + version + "' for testing...");
		try {
			// Create the working dir for spigot to run
			File spigotWorkingDir = new File(baseFolder, "target/it/spigotmc");
			spigotWorkingDir.mkdirs();

			// Accept the EULA so Spigot will run
			File eulaFile = new File(spigotWorkingDir, "eula.txt");
			PrintWriter out = new PrintWriter(eulaFile);
			out.println("eula=true");
			out.close();

			// Get spigot
			String url = "https://cdn.getbukkit.org/spigot/spigot-" + version + ".jar";
			if(usepaper.equals("true")) {
				url = PAPER_DOWNLOADS.get(version);
			}
			if (version.startsWith("http")) {
				url = version;
				version = "custom";
			}
			
			// Get the file to run
			File outFile;
			if(usepaper.equals("true")) {
				String paperRevision = PAPER_DOWNLOADS.get(version);
				paperRevision = paperRevision.substring(0, paperRevision.length() - "/download".length());
				paperRevision = paperRevision.substring(paperRevision.lastIndexOf("/") + 1, paperRevision.length());
				outFile = new File(spigotWorkingDir, "paper-" + paperRevision + ".jar");
			} else {
				outFile = new File(spigotWorkingDir, "spigot-" + version + ".jar");
			}
			if (outFile.exists()) {
				outFile.delete();
			}
			
			// Download it
			URLConnection con = new URL(url).openConnection();
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.121 Safari/537.36 OPR/67.0.2245.46");
			try (BufferedInputStream in = new BufferedInputStream(con.getInputStream());
					FileOutputStream fileOutputStream = new FileOutputStream(outFile.getAbsolutePath())) {
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
			} catch (IOException e) {
				throw e;
			}

			final String[] args;
			// These commands need to have the flag DIReallyKnowWhatIAmDoingISwear which
			// prevents Spigot from checking for the latest version
			// because I really don't need these tests to spend 20 seconds doing nothing
			if (System.getProperty("os.name").contains("Windows")) {
				args = new String[] { "cmd.exe", "/C",
						"java -jar -DIReallyKnowWhatIAmDoingISwear=a " + outFile.getAbsolutePath() + " nogui" };
			} else {
				args = new String[] { "/bin/bash", "-c",
						"java -jar -DIReallyKnowWhatIAmDoingISwear=a " + outFile.getAbsolutePath() + " nogui" };
			}
			spigotProcess = new ProcessBuilder(args).directory(spigotWorkingDir).redirectErrorStream(true).start();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(spigotProcess.getOutputStream()));
			try {
				writer.write("stop\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(spigotProcess.getInputStream()));
			AtomicReference<Status> status = new AtomicReference<SpigotStartMojo.Status>(Status.WAITING);
			long start = System.currentTimeMillis();
			Thread watcher = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						String read = null;
						while (spigotProcess.isAlive()) {
							read = in.readLine();
							if (read != null) {
								System.out.println("Spigot: " + read);
								if (read.contains(works)) {
									spigotProcess.destroyForcibly();
									status.set(Status.OK);
									return;
								}
								if (read.contains(error)) {
									status.set(Status.ERROR);
									return;
								}
								if (read.contains("FAILED TO BIND TO PORT")) {
									status.set(Status.ERROR);
									return;
								}
								if (read.contains("This crash report")) {
									status.set(Status.ERROR);
									return;
								}
								// TODO: This shouldn't be the point where we start waiting.
								// I need this to start waiting after all of the scheduled tasks
								// from Bukkit have ended
								if (read.contains(waitingphrase)) {
									status.set(Status.WAITING);
									return;
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			watcher.start();
			// 4 minutes
			while (spigotProcess.isAlive() && status.get() == Status.WAITING
					&& (System.currentTimeMillis() - start) < SERVER_TIMEOUT * 1000 * 60) {
				Thread.sleep(1000);
			}
			System.out.println("Status: " + status.get());
			if (status.get() == Status.WAITING) {
				throw new MojoFailureException("Nothing happened!");
			}
			if (status.get() == Status.ERROR) {
				throw new MojoFailureException("Plugin did not start successfully!");
			}
			new SpigotStopMojo().execute();
		} catch (Throwable t) {
			new SpigotStopMojo().execute();
			throw new MojoFailureException("Unable to start Spigot server.", t);
		}
	}

	private void deleteFolder(File folder) {
		if (folder.exists()) {
			for (File f : folder.listFiles()) {
				if (f.isFile()) {
					f.delete();
				} else {
					deleteFolder(f);
				}
			}
			folder.delete();
		}
	}

	private static enum Status {
		WAITING, ERROR, OK
	}

}
