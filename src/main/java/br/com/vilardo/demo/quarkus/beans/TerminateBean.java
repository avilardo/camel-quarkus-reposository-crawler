package br.com.vilardo.demo.quarkus.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Handler;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("TerminateBean")
@RegisterForReflection
public class TerminateBean {

	@Handler
	public void terminate() {
		final Thread thread = new Thread(this.exit());
		thread.start();
	}

	private Runnable exit() {
		return () -> {
			try {
				Thread.sleep(3000);

			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

			System.exit(0);

		};
	}

}