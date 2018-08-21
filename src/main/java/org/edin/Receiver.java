package org.edin;

import org.apache.log4j.Logger;
import org.edin.config.*;
import org.edin.service.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by levitsky on 10.10.17.
 */
public class Receiver implements Runnable {

    private static final Logger log = Logger.getLogger(Receiver.class);

    public Receiver(){
        registerShutdownHook();
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(this.getClass().getSimpleName());
            log.info(Thread.currentThread().getName() + " start");
            /*
            * handling custom inbound files due to separate file's settings
            * */
            for(Doc doc : Worker.conf.inbound.document){
                for (String file : Controller.list(doc.type)){
                    try{
                        List<String> l = new LinkedList<>();
                        l.add(doc.path);
                        save(l, file, Controller.get(file));
                    }catch (Exception ex){
                        ex.printStackTrace();
                        log.error(ex.getMessage());
                    }
                }
            }
            /*
            * handling default inbound
            * */
            if(Worker.conf.inbound.path != null)
            Controller.list().forEach(file->{
                try {
                    save(Worker.conf.inbound.path, file , Controller.get(file));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void save(List<String> paths, String name, byte[] body) {
        try{
            paths.stream().forEach(e->save(Paths.get(e), name , body));
            Controller.remove(name);
            log.info(name + " removed from server");
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void save(Path path, String name, byte[] body) {
        try{
            if(!Files.exists(path))
                Files.createDirectories(path);
            Files.write(path.resolve(name),body);
            log.info(name + " saved to [" + path + "]");
        }catch(Exception e){
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
