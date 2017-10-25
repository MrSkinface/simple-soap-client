package org.exite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.exite.config.Config;
import org.exite.service.Controller;
import org.exite.utils.XML;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by levitsky on 10.10.17.
 */
public class Worker {

    private static final Logger log = Logger.getLogger(Worker.class);

    public static Config conf;

    public Worker(String configPath) throws Exception {
        configure(configPath);
        new Thread(new Sender()).start();
        new Thread(new Receiver()).start();
    }

    public static void main(String[] args) throws Exception{
        if(args.length < 1 || args[0] == null){
            throw new Exception("path to [config.xml] must be defined in arguments");
        }else{
            new Worker(args[0]);
        }
    }

    private void configure(String configPath) throws Exception {
        try {
            PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
            /*PropertyConfigurator.configure(new FileInputStream(Paths.get(System.getProperty("user.dir")).resolve("log4j.properties").toString()));*/
            /*conf=(Config) XML.fromXml(Files.readAllBytes(Paths.get(System.getProperty("user.dir")).resolve("config.xml")), Config.class);*/
            conf=(Config) XML.fromXml(Files.readAllBytes(Paths.get(configPath)), Config.class);
            Controller.configure(conf);
        } catch (FileNotFoundException e) {
            log.error("Bad config file: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("Bad config file: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
