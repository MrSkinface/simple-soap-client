package org.edin.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by levitsky on 10.10.17.
 */
@XmlType
@XmlRootElement(name="config")
public class Config {

    @XmlAttribute
    public String wsdl;
    @XmlElement
    public String login;
    @XmlElement
    public String password;
    @XmlElement
    public Direction inbound;
    @XmlElement
    public Direction outbound;

    @Override
    public String toString() {
        return "Config {" +
                "wsdl='" + wsdl + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", inbound=" + inbound +
                ", outbound=" + outbound +
                '}';
    }
}
