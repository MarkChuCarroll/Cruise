package org.goodmath.cruise;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

import org.goodmath.cruise.interp.CruiseRuntimeException;
import org.goodmath.cruise.interp.Engine;
import org.goodmath.cruise.parser.CruiseParser;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", mixinStandardHelpOptions = true, version = "0.0", description = "Run a cruise program")
public class Run implements Callable<Integer> {
  @Option(names = { "-t", "--trace" }, description = "print trace information describing the execution")
  private boolean trace = false;

  @Parameters(index = "0", description = "the name of the source file to run")
  private File source;

  @Override
  public Integer call() throws Exception {
    var file = source.getAbsoluteFile();
    if (!file.exists()) {
      System.err.println("File " + file + " does not exist");
      return 1;
    }
    FileInputStream in = new FileInputStream(source);
    CruiseParser parser = new CruiseParser(in);
    Engine engine = parser.program();
    engine.trace = trace;
    try {
      engine.run();
      return 0;
    } catch (CruiseRuntimeException e) {
      System.err.println("Runitme error: " + e);
      return 1;
    }
  }

}
