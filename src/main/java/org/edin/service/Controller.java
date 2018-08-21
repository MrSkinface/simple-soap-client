package org.edin.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.edin.config.Config;
import org.edin.exceptions.SoapException;
import org.service.edi.soap.*;

import javax.xml.ws.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by levitsky on 10.10.17.
 */
public class Controller {

    private static ServiceWs service;
    private static EdiLogin user;

    private static ObjectFactory factory;

    public static void configure(Config conf) throws SoapException {
        try{
            Service srv = new ServiceWsService();
            service = srv.getPort(ServiceWs.class);
            user = new EdiLogin();
            user.setLogin(conf.login);
            user.setPass(DigestUtils.md5Hex(conf.password));
            factory = new ObjectFactory();
        }catch(Exception e){
            throw new SoapException(e.getMessage());
        }
    }

    public static List<String> list(String filter) throws SoapException {
        return list().stream().filter(e -> e.toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
    }
    public static boolean remove(String fileName) throws SoapException {
        ArchiveDocRequest request = factory.createArchiveDocRequest();
        request.setUser(user);
        request.setFileName(fileName);
        ArchiveDocResponse response = service.archiveDoc(request);
        EdiResponse result = response.getResult();
        if(result.getErrorCode()!=0){
            throw new SoapException(result.getErrorMessage());
        }
        return true ;
    }
    public static boolean send(String fileName, byte[]content) throws SoapException {
        SendDocRequest request = factory.createSendDocRequest();
        request.setUser(user);
        request.setFileName(fileName);
        request.setContent(factory.createSendDocRequestContent(content));
        SendDocResponse response = service.sendDoc(request);
        EdiResponse result = response.getResult();
        if(result.getErrorCode()!=0){
            throw new SoapException(result.getErrorMessage());
        }
        return true ;
    }
    public static boolean upload(String fileName, byte[]content, String folder) throws SoapException {
        UploadDocRequest request = factory.createUploadDocRequest();
        request.setUser(user);
        request.setFileName(fileName);
        request.setContent(factory.createUploadDocRequestContent(content));
        request.setRemoteFolder(folder);
        UploadDocResponse response = service.uploadDoc(request);
        EdiResponse result = response.getResult();
        if(result.getErrorCode()!=0){
            throw new SoapException(result.getErrorMessage());
        }
        return true ;
    }
    public static byte[] get(String fileName) throws SoapException {
        GetDocRequest request = factory.createGetDocRequest();
        request.setUser(user);
        request.setFileName(fileName);
        GetDocResponse response = service.getDoc(request);
        EdiFile file = response.getResult();
        if(file.getErrorCode()!=0){
            throw new SoapException(file.getErrorMessage());
        }
        return file.getContent();
    }
    public static List<String>list() throws SoapException {
        GetListRequest request = factory.createGetListRequest();
        request.setUser(user);
        GetListResponse response = service.getList(request);
        EdiFileList list = response.getResult();
        if(list.getErrorCode()!=0){
            throw new SoapException(list.getErrorMessage());
        }
        return  list.getList();
    }
}
