package com.srs.transformer;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * GasFinder returns the current average price per location or nationwide. 
 *
 */
public class gasfinder extends AbstractMessageTransformer{

	private static final Logger log = LoggerFactory.getLogger(gasfinder.class);
	
	/**
	 * 
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		
		Double gasPrice = 0D;
		
		if (message.getPayload() instanceof org.mule.transport.NullPayload){
			gasPrice = getNationalAveragePrice();
		}else{
			String location = (String) message.getPayload();
			gasPrice = getPricePerLocation(location);
		}
		
		return gasPrice;
	
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 * @throws TransformerException
	 */
	private Double getPricePerLocation(String location) throws TransformerException{
		
		Double gasPrice = 0D;
		log.info("Location in Java: " + location);
		
		Document doc;
		try {
			doc = Jsoup.connect("http://gasprices.aaa.com/?state=TX").get();
			Elements searchResults = doc.getElementsContainingOwnText(location);
			Element gasTag = searchResults.get(0);
			
			gasPrice = Double.parseDouble(gasTag.attr("data-cost"));
			
			System.out.println("SOUP got us: " + gasTag.attr("data-cost"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error detected!");
			e.printStackTrace();
			throw new TransformerException(null);
		}
		
		return gasPrice;
		
	}
	
	/**
	 * 
	 * @return
	 * @throws TransformerException
	 */
	private Double getNationalAveragePrice() throws TransformerException{
		
		Double gasPrice = 0D;
		log.info("Gasfinder getNationalAveragePrice called");
		
		Document doc;
		try {
			doc = Jsoup.connect("http://gasprices.aaa.com").get();
			//Elements newsHeadlines = doc.select("#mp-itn b a");
			Elements searchResults = doc.getElementsByClass("numb");
			//Elements searchResults = doc.getElementsContainingOwnText(location);
			Element gasTag = searchResults.get(0);

			//gasPrice = Double.parseDouble(gasTag.attr("data-cost"));
			String gasPriceStr = gasTag.text();
			gasPrice = Double.parseDouble(gasPriceStr.substring(1));
			log.info("SOUP got us: " + gasPrice);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error detected!");
			e.printStackTrace();
			throw new TransformerException(null);
		}
		
		return gasPrice;
		
	}
	
}
