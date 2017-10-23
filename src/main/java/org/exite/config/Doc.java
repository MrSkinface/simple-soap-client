package org.exite.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by levitsky on 10.10.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Doc {
    @XmlElement
    public String type;
    @XmlElement
    public String path;
    @XmlElement
    public String folder;

    @Override
    public String toString() {
        return "Doc{" +
                "type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", folder='" + folder + '\'' +
                '}';
    }
}
