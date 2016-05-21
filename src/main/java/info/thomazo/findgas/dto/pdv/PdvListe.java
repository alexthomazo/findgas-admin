
package info.thomazo.findgas.dto.pdv;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pdv" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="adresse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ville" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="latitude" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="longitude" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="cp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pdv"
})
@XmlRootElement(name = "pdv_liste")
public class PdvListe {

    @XmlElement(required = true)
    protected List<PdvListe.Pdv> pdv;

    /**
     * Gets the value of the pdv property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pdv property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPdv().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PdvListe.Pdv }
     * 
     * 
     */
    public List<PdvListe.Pdv> getPdv() {
        if (pdv == null) {
            pdv = new ArrayList<PdvListe.Pdv>();
        }
        return this.pdv;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="adresse" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ville" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="latitude" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="longitude" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="cp" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "adresse",
        "ville"
    })
    public static class Pdv {

        @XmlElement(required = true)
        protected String adresse;
        @XmlElement(required = true)
        protected String ville;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "latitude")
        protected String latitude;
        @XmlAttribute(name = "longitude")
        protected String longitude;
        @XmlAttribute(name = "cp")
        protected String cp;

        /**
         * Obtient la valeur de la propriété adresse.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdresse() {
            return adresse;
        }

        /**
         * Définit la valeur de la propriété adresse.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdresse(String value) {
            this.adresse = value;
        }

        /**
         * Obtient la valeur de la propriété ville.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVille() {
            return ville;
        }

        /**
         * Définit la valeur de la propriété ville.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVille(String value) {
            this.ville = value;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété latitude.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * Définit la valeur de la propriété latitude.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLatitude(String value) {
            this.latitude = value;
        }

        /**
         * Obtient la valeur de la propriété longitude.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLongitude() {
            return longitude;
        }

        /**
         * Définit la valeur de la propriété longitude.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLongitude(String value) {
            this.longitude = value;
        }

        /**
         * Obtient la valeur de la propriété cp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCp() {
            return cp;
        }

        /**
         * Définit la valeur de la propriété cp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCp(String value) {
            this.cp = value;
        }

    }

}
