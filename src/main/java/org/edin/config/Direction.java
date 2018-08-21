package org.edin.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by levitsky on 10.10.17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Direction {

    @XmlElement
    public String arcPath;

    @XmlElement
    public List<String> path;

    @XmlElement
    public List<Doc>document;

    @Override
    public String toString() {
        return "Direction{" +
                "document=" + document +
                '}';
    }
}
