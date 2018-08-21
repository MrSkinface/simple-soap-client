package org.edin.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

/**
 * Created by levitsky on 10.10.17.
 */
public class XML {

    public static Object fromXml(byte[]body,Class<? extends Object> c) {
        try {
            JAXBContext jc=JAXBContext.newInstance(c);
            Unmarshaller unm=jc.createUnmarshaller();
            return unm.unmarshal(new ByteArrayInputStream(body));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
