package com.jaxzin.spigotmc.maven;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;


/**
 * SpigotStopMojo - stops a running instance of spigot.
 */
@Mojo(
    name = "stop",
    defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST
)
public class SpigotStopMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
       if(SpigotStartMojo.spigotProcess == null)return;
    	getLog().info("Shutting down Spigot...");
        final OutputStreamWriter out = new OutputStreamWriter(SpigotStartMojo.spigotProcess.getOutputStream());
        PrintWriter writer = new PrintWriter(out);
        writer.println("stop");
        writer.flush();
        writer.close();
        SpigotStartMojo.spigotProcess.destroyForcibly();
        try {
			SpigotStartMojo.spigotProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        SpigotStartMojo.spigotProcess = null;
        getLog().info("Shut down Spigot.");
    }
}
