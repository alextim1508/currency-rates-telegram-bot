package com.alextim.currencyrates.cbr.parser;


import com.alextim.currencyrates.cbr.exception.CurrencyRateParsingException;
import com.alextim.currencyrates.cbr.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CurrencyRateParserXml implements CurrencyRateParser {

    @Override
    public List<CurrencyRate> parse(String ratesAsString) {
        log.debug("Starting to parse XML response from CBR. Length: {}", ratesAsString != null ? ratesAsString.length() : 0);
        List<CurrencyRate> rates = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (StringReader reader = new StringReader(ratesAsString)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("Valute");
                log.debug("Found {} 'Valute' elements in the XML.", list.getLength());

                for (var i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String numCode = element.getElementsByTagName("NumCode").item(0).getTextContent().trim();
                        String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent().trim();
                        String nominal = element.getElementsByTagName("Nominal").item(0).getTextContent().trim();
                        String name = element.getElementsByTagName("Name").item(0).getTextContent().trim();
                        String value = element.getElementsByTagName("Value").item(0).getTextContent().trim();

                        CurrencyRate rate = CurrencyRate.builder()
                                .numCode(numCode)
                                .charCode(charCode)
                                .nominal(nominal)
                                .name(name)
                                .value(value)
                                .build();
                        rates.add(rate);
                    }
                }
                log.info("Successfully parsed {} currency rates from CBR XML.", rates.size());
            }
        } catch (Exception ex) {
            log.error("XML parsing error. First 200 chars of XML: {}", ratesAsString != null ? ratesAsString.substring(0, Math.min(200, ratesAsString.length())) : "null", ex);
            throw new CurrencyRateParsingException("Failed to parse CBR XML response", ex);
        }
        return rates;
    }
}