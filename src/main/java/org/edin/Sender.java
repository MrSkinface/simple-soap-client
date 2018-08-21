package org.edin;

import org.apache.log4j.Logger;
import org.edin.config.*;
import org.edin.exceptions.SoapException;
import org.edin.service.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by levitsky on 10.10.17.
 */
public class Sender implements Runnable {

    private static final Logger log = Logger.getLogger(Sender.class);

    public Sender() {
        registerShutdownHook();
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.getClass().getSimpleName());
        log.info(Thread.currentThread().getName() + " start");
        /*
        * handling custom outbound files due to separate file's settings
        * */
        for(Doc doc : Worker.conf.outbound.document){
            try{
                list(doc).forEach(name -> {
                    try{
                        if(!Files.isDirectory(name)){
                            if(doc.folder==null){
                                Controller.send(name.getFileName().toString(),Files.readAllBytes(name));
                                log.info("[" + name.getFileName().toString() + "] send successfully");
                            } else{
                                Controller.upload(name.getFileName().toString(), Files.readAllBytes(name), doc.folder);
                                log.info("[" + name.getFileName().toString() + "] uploaded successfully to ["+doc.folder+"]");
                            }
                            archive(name, Paths.get(Worker.conf.outbound.arcPath));
                            log.info("[" + name.getFileName().toString() + "] moved from [" + doc.path + "] to [" + Worker.conf.outbound.arcPath + "]");
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        log.error(ex.getMessage());
                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
                log.error(ex.getMessage());
            }
        }
        /*
        * handling default outbound
        * */
        if(Worker.conf.outbound.path != null)
        Worker.conf.outbound.path.stream().forEach(path -> {
            list(Paths.get(path)).forEach(e -> {
                try {
                    if(!Files.isDirectory(e)){
                        Controller.send(e.getFileName().toString(), Files.readAllBytes(e));
                        log.info("[" + e.getFileName().toString() + "] send successfully");
                        archive(e, Paths.get(Worker.conf.outbound.arcPath));
                        log.info("[" + e.getFileName().toString() + "] moved from [" + Worker.conf.outbound.path + "] to [" + Worker.conf.outbound.arcPath + "]");
                    }
                } catch (SoapException e1) {
                    e1.printStackTrace();
                    log.error(e1.getMessage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    log.error(e1.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log.error(e1.getMessage());
                }
            });
        });

    }

    private void archive(Path from, Path to) throws Exception {
        try{
            if(!Files.exists(to))
                Files.createDirectories(to);
            Files.write(to.resolve(from.getFileName()), Files.readAllBytes(from));
            Files.delete(from);
        }catch(Exception e){
            e.printStackTrace();
            Exception ex = new Exception("error while moving to arc : " + e.getMessage());
            log.error(ex.getMessage());
            throw ex;
        }
    }

    private List<Path> list(Path path) {
        try{
            if(!Files.exists(path))
                Files.createDirectories(path);
            return Files.list(path).collect(Collectors.toList());
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new LinkedList<>();
        }
    }

    private List<Path> list(Path path, String filter){
        return list(path).stream().filter(e -> e.getFileName().toString().toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
    }

    private List<Path> list (Doc doc){
        if(doc.type != null){
            log.info("Searching ["+doc.type+"] in ["+doc.path+"]");
            return list(Paths.get(doc.path), doc.type);
        }else{
            log.info("Searching files in ["+doc.path+"]");
            return list(Paths.get(doc.path));
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
