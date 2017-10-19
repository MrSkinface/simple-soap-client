package org.exite;

import org.apache.log4j.Logger;
import org.exite.config.*;
import org.exite.service.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by levitsky on 10.10.17.
 */
public class Sender implements Runnable {

    private static final Logger log = Logger.getLogger(Sender.class);

    public Sender() {
        registerShutdownHook();
        new Thread(this).start();
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.getClass().getSimpleName());
        log.info(Thread.currentThread().getName() + " start");
        for(Doc doc : Worker.conf.outbound.document){
            try{
                File f = new File(doc.path);
                if(!Files.exists(Paths.get(doc.path)))
                    Files.createDirectories(Paths.get(doc.path));
                for(String name : f.list()){
                    if(name.toLowerCase().startsWith(doc.type.toLowerCase())){
                        try{
                            Controller.send(name,Files.readAllBytes(Paths.get(doc.path).resolve(name)));
                            log.info("[" + name + "] send successfully");
                            Files.delete(Paths.get(doc.path).resolve(name));
                            log.info("["+name+"] removed from ["+doc.path+"]");
                        }catch (Exception ex){
                            ex.printStackTrace();
                            log.error(ex.getMessage());
                        }
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
                log.error(ex.getMessage());
            }
        }
    }
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Sender end");
            }
        });
    }
}
