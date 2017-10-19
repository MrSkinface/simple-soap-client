package org.exite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.exite.SoapExAPI.SoapExAPIException;
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

    public Worker() {
        configure();
        new Sender();
        new Receiver();

    }

    public static void main(String[] args) {
        new Worker();
    }

    private void configure(){
        try {
            PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
            /*PropertyConfigurator.configure(new FileInputStream(Paths.get(System.getProperty("user.dir")).resolve("log4j.properties").toString()));*/
            conf=(Config) XML.fromXml(Files.readAllBytes(Paths.get(System.getProperty("user.dir")).resolve("config.xml")), Config.class);
            Controller.configure(conf);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SoapExAPIException e) {
            log.error(e.getMessage());
        }
    }
}
