package com.salesforce.kspout;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class ProductDescriptionBolt implements IRichBolt{
	OutputCollector _collector;
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	public void execute(Tuple input) {
		String URL = input.getString(0);

		String productDescription = WordKafkaUtil.getDescription(URL);
		
		if(productDescription!=null){
			CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
			TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_CURRENT, new StringReader(productDescription.trim()));
			tokenStream = new StopFilter(Version.LUCENE_CURRENT, tokenStream, stopWords);
		    tokenStream = new PorterStemFilter(tokenStream);
		    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		    
			try {
				while(tokenStream.incrementToken()){
					_collector.emit(new Values(charTermAttribute.toString()));
				}
			} catch (IOException e) {
			}
		}
		
		_collector.ack(input);
	}

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
