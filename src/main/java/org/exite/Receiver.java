package org.exite;

import org.apache.log4j.Logger;
import org.exite.SoapExAPI.SoapExAPIException;
import org.exite.config.*;
import org.exite.service.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by levitsky on 10.10.17.
 */
public class Receiver implements Runnable {

    private static final Logger log = Logger.getLogger(Receiver.class);

    public Receiver(){
        registerShutdownHook();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(this.getClass().getSimpleName());
            log.info(Thread.currentThread().getName() + " start");
            for(Doc doc : Worker.conf.inbound.document){
                for (String file : Controller.list(doc.type)){
                    try{
                        if(!Files.exists(Paths.get(doc.path)))
                            Files.createDirectories(Paths.get(doc.path));
                        Files.write(Paths.get(doc.path).resolve(file),Controller.get(file));
                        log.info(file + " saved to [" + doc.path + "]");
                        Controller.remove(file);
                        log.info(file + " removed from server");
                    }catch (Exception ex){
                        ex.printStackTrace();
                        log.error(ex.getMessage());
                    }
                }
            }
        } catch (SoapExAPIException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Receiver end");
            }
        });
    }
}
