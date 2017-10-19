package org.exite.service;

import org.exite.SoapExAPI.ISoapExAPI;
import org.exite.SoapExAPI.SoapExAPI;
import org.exite.SoapExAPI.SoapExAPIException;
import org.exite.config.Config;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by levitsky on 10.10.17.
 */
public class Controller {
    private static ISoapExAPI soap;

    public static void configure(Config conf) throws SoapExAPIException {
        try{
            soap = new SoapExAPI(conf.login,conf.password);
        }catch(MalformedURLException e){
            throw new SoapExAPIException(e.getMessage());
        }
    }

    public static List<String> list(String filter) throws SoapExAPIException {
        return list().stream().filter(e -> e.toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
    }
    public static boolean remove(String fileName) throws SoapExAPIException {
        return soap.archiveDoc(fileName);
    }
    public static boolean send(String fileName, byte[]content) throws SoapExAPIException {
        return soap.sendDoc(fileName, content);
    }
    public static byte[] get(String fileName) throws SoapExAPIException {
        return soap.getDoc(fileName);
    }
    public static List<String>list() throws SoapExAPIException{
        return  soap.getList();
    }
}
