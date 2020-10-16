package com.elcom.gateway.restcore;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class XMLConverter {

    public static <T> String toXML(Object obj, Class<T> actualObject)
            throws ISRestCoreException {

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(actualObject);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();

            jaxbMarshaller.marshal(obj, sw);

            return sw.toString();

        } catch (JAXBException e) {
            throw new ISRestCoreException(e.getMessage());
        }

    }

    public static <T> T toObject(String xml, Class<T> actualObject)
            throws ISRestCoreException {

        try {

            T obj = (T) JAXB.unmarshal(new StringReader(removeXmlStringNamespaceAndPreamble(xml)), actualObject);

            return obj;
        } catch (Exception e) {
            throw new ISRestCoreException(e.getMessage());
        }
    }

    private static String removeXmlStringNamespaceAndPreamble(String xmlString) {
        return xmlString.replaceAll("(<\\?[^<]*\\?>)?", ""). /* remove preamble */
                replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
                .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
                .replaceAll("(</)(\\w+:)(.*?>)", "$1$3") /* remove closing tags prefix */
                .replaceAll("xsi:nil=\"true\"", "");
        /* remove xsi:nil=true value */
    }
}
