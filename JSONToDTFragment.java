package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JSONToDTFragment {	

	public static void main(String[] args) {

		File currentDir = new File(System.getProperty("user.dir"));
		File[] jsonFiles = currentDir.listFiles((dir,fileName) -> fileName.endsWith(".json"));
				
		for(File jsonFile: jsonFiles) {
			
			System.out.println("\nProcessing "+jsonFile.getName()+" file.");
			String dtFragmentContent = getdtFragmentContent(jsonFile);
			
			if(dtFragmentContent.length() != 21) {
				String ramlFileName = jsonFile.getName().replace(".json", ".raml");
				createDTFragmentFile(ramlFileName, dtFragmentContent);
				System.out.println(ramlFileName+ " file is created.");
			}			
		}				
	}	

	private static String getdtFragmentContent(File jsonFile) {
		
		DTFragmentBuilder fragStringBuilder = new DTFragmentBuilder();
		ObjectMapper jsonOm = new ObjectMapper();
		try {
			JsonNode rootNode = jsonOm.readValue(jsonFile, JsonNode.class);
			traverseJson(rootNode, fragStringBuilder);
		} catch (IOException e) {
			System.out.println("Problem occured while reading "+jsonFile.getName()+" file.");	
		}		
		return fragStringBuilder.getResult().toString();
	}

	private static void traverseJson(JsonNode rootNode, DTFragmentBuilder fragStringBuilder) {	
		
		if(rootNode.isObject())
		{
			fragStringBuilder.addObject();
	        Iterator<String> fieldNames = rootNode.fieldNames();

	        while(fieldNames.hasNext()) {
	            String fieldName = fieldNames.next();
	            fragStringBuilder.addKeyName(fieldName);
	            JsonNode node = rootNode.get(fieldName);
	            checkNodeType(node, fragStringBuilder);
	        }
	        fragStringBuilder.backIndent(); //one extra for key
	        fragStringBuilder.backIndent();
	    } 
		else if(rootNode.isArray())
		{
	    	fragStringBuilder.addArray();
	        ArrayNode arrayNode = (ArrayNode) rootNode;
	        
	        JsonNode node = arrayNode.get(0);
	        checkNodeType(node, fragStringBuilder);

	        fragStringBuilder.backIndent();
	    }
	}
		
	private static void checkNodeType(JsonNode node, DTFragmentBuilder fragStringBuilder) {
		JsonNodeType nodeType = node.getNodeType();
        if(nodeType == JsonNodeType.ARRAY || nodeType == JsonNodeType.OBJECT)
        	traverseJson(node, fragStringBuilder);
        else if(nodeType == JsonNodeType.NUMBER)
        	fragStringBuilder.addNumber();
        else if(nodeType == JsonNodeType.BOOLEAN)
        	fragStringBuilder.addBoolean();   
        else if(nodeType == JsonNodeType.NULL)
        	fragStringBuilder.addNull();
        else
        	fragStringBuilder.addString();
        
	}

	private static void createDTFragmentFile(String fileName, String dtFragmentContent) {		
		try {
			 BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			 writer.write(dtFragmentContent);
			 writer.close();
		} catch (IOException e) {
			System.out.println("Error while creating " + fileName + " file");
		}
	}
}
